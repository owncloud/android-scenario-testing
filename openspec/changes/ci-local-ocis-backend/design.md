## Context

See `proposal.md – Why` for motivation.

Currently `ocisbackend.yml` is a `workflow_call` reusable workflow. GitHub Actions runs each
called workflow as an independent job on its own runner. The Android emulator runs in
`e2etests.yml` on a separate runner. A container started in `ocisbackend.yml` is therefore
unreachable from `e2etests.yml` — they do not share a network, filesystem, or loopback
interface.

The Android emulator reaches the host machine via the fixed alias `10.0.2.2`. This only works
when the container is published on the same host as the emulator, i.e. in the same job.

`create_server.sh` is the self-contained provisioning script: it starts Docker Compose, waits
for oCIS to be ready, then provisions users and roles via the Graph API. Ansible's sole role
is to copy the script to the remote host and run it there.

## Goals / Non-Goals

**Goals:**
- Run oCIS on the same GitHub Actions runner as the emulator so `10.0.2.2:9200` is reachable.
- Preserve the exact provisioning state produced by `create_server.sh` (users, group, roles).
- Replace the fragile `sleep 10` with a reliable readiness check before provisioning.
- Capture oCIS container logs as a separate CI artifact on every run.
- Keep `OCIS_PWD` and `OCIS_VERSION` configurable without SSH secrets.

**Non-Goals:**
- Modifying test scenarios, step definitions, page objects, or any Java test harness code.
- Supporting the remote Ansible-based flow in parallel (it is removed, not kept as a fallback).
- Changing the oCIS configuration or the provisioned data set.

## Decisions

### 1. Merge backend startup into `e2etests.yml`

Docker Compose and `create_server.sh` are added as steps in `e2etests.yml`, before the
`Run Emulator & Tests` step. `ocisbackend.yml` and `cleanup.yml` are deleted.

**Alternative considered:** Keep `ocisbackend.yml` as a separate workflow and pass the
container state between jobs via an artifact or service container.
**Rejected:** GitHub Actions service containers are tied to their own job and are not exposed
to other jobs. Sharing a running container across jobs is not supported on GitHub-hosted
runners. Merging into a single job is the only viable path.

### 2. Reuse `create_server.sh` without inlining its logic

`create_server.sh` is called directly as a workflow step. Its provisioning logic (user
creation, group, role assignment) is not copied into the YAML.

**Alternative considered:** Inline the Graph API calls as `run:` steps in the workflow.
**Rejected:** This duplicates the provisioning logic and creates a maintenance divergence
between local (`create_server.sh`) and CI execution paths.

### 3. Replace `sleep 10` with a poll loop in `create_server.sh`

The fixed 10-second wait before Graph API calls is replaced with a loop that polls the oCIS
capabilities endpoint until it returns HTTP 200 or a timeout is reached. The timeout must be
long enough to cover a cold pull of the oCIS image on a fresh runner.

**Alternative considered:** Add a separate readiness step in the workflow YAML, before
calling `create_server.sh`.
**Rejected:** The readiness check is logically part of the server startup sequence; keeping
it in `create_server.sh` means it also applies when the script is run outside CI (locally,
manually). Splitting it creates a gap where the script can be invoked directly without the
guard.

### 4. TLS handling — UNRESOLVED prerequisite

oCIS starts with HTTPS and a self-signed certificate. The Java test harness uses an unsafe
HTTP client that skips TLS verification. Whether the qaRelease Android APK also accepts
self-signed certificates must be verified before the rest of the implementation proceeds.

Two fallbacks are defined:
- **Option A (preferred):** oCIS configured to serve plain HTTP. This avoids any cert trust
  issue on the Android side. Must be validated against oCIS documentation — `OCIS_INSECURE`
  affects oCIS's own outbound TLS but may not disable its inbound HTTPS listener.
- **Option B:** Pass the self-signed cert to the emulator trust store before launching the
  app. More complex but does not require changes to oCIS configuration.

This decision is deferred to implementation; it must be resolved as the first task.

### 5. Tika exclusion for CI — UNRESOLVED prerequisite

`server/docker-compose.yml` includes a Tika service for full-text search. GitHub-hosted
runners have limited RAM (7 GB); the emulator already claims 3 GB. Whether any scenario in
the standard CI tag filter (`not @ignore and not @noocis and not @noci`) exercises full-text
search must be verified. If no scenario requires Tika, it should be excluded from the Compose
profile used in CI to reduce resource pressure.

This is resolved during the first implementation task (TLS verification) so both checks can
be done together before any workflow YAML is changed.

## Risks / Trade-offs

| Risk | Mitigation |
|---|---|
| qaRelease APK rejects self-signed cert → all requests fail | Verify first; apply Option A or B before any other change |
| oCIS + Tika + emulator exhaust runner RAM → flaky tests | Verify Tika necessity; exclude if unused in CI tag filter |
| Cold image pull makes readiness timeout too short | Set poll timeout to ≥ 120 s to cover first-pull latency |
| `create_server.sh` provisioning fails silently if poll loop exits too early | Poll loop must fail the script (exit 1) on timeout so the job fails fast with a clear error |
| oCIS container logs lost on runner teardown | Always upload container logs as a CI artifact, even on success |

## Migration Plan

1. Verify TLS and Tika prerequisites (see Open Questions).
2. Update `create_server.sh`: replace `sleep 10` with a poll loop.
3. Update `e2etests.yml`: add Docker Compose startup, `create_server.sh` call, and log capture step.
4. Update `e2e.yml`: remove `create_ocis` and `cleanup` jobs; remove SSH secrets.
5. Delete `ocisbackend.yml` and `cleanup.yml`.
6. Verify `server/docker-compose.yml` env var substitution works unchanged when run locally on the runner.

Rollback: the deleted workflows are recoverable from git history. No database migration or
persistent state is involved — the runner is ephemeral.

## Open Questions

- Does the qaRelease APK network security config accept self-signed certificates? *(Blocks TLS decision — must be answered first)*
- Does `OCIS_INSECURE=true` allow oCIS to serve plain HTTP, or does it only affect its own outbound TLS? *(Shapes Option A feasibility)*
- Is any scenario in the `not @ignore and not @noocis and not @noci` filter dependent on Tika / full-text search? *(Shapes Compose profile for CI)*
