## Why

The nightly CI pipeline provisions the oCIS backend by SSHing into an external VM via Ansible,
creating a hard dependency on infrastructure secrets and a remote server that is a single point
of failure. Moving provisioning onto the GitHub Actions runner itself removes that dependency,
simplifies the secret surface, and eliminates the separate cleanup job.

## What Changes

- **Remove** `ocisbackend.yml` — the Ansible/SSH provisioning workflow is no longer needed.
- **Remove** `cleanup.yml` — container cleanup becomes implicit when the runner job exits.
- **Remove** `create_ocis` and `cleanup` jobs from `e2e.yml`; remove all SSH secrets from it.
- **Add** Docker Compose startup and `create_server.sh` execution as steps inside `e2etests.yml`,
  running on the same runner as the Android emulator.
- **Add** a proper readiness poll to `create_server.sh`, replacing the current fixed `sleep 10`,
  to ensure oCIS is accepting connections before provisioning begins.
- **Add** oCIS container log capture as a new CI artifact, uploaded regardless of job outcome.
- **Verify** TLS compatibility: resolve whether the qaRelease APK accepts self-signed certs from
  oCIS, and apply a fallback (HTTP mode or emulator trust store) if it does not.
- **Verify** whether Tika is exercised by the CI tag filter; exclude it from the CI Compose
  profile if not, to reduce runner resource pressure.

No Gherkin scenarios, step definitions, page objects, or test harness Java code are modified.

## Capabilities

### New Capabilities
_(none — pure CI infrastructure change)_

### Modified Capabilities
_(none — no spec-level behavior changes; `skip_specs: true` set in `.openspec.yaml`)_

## Impact

- **Workflows**: `e2etests.yml`, `e2e.yml` modified; `ocisbackend.yml`, `cleanup.yml` deleted.
- **Scripts**: `server/create_server.sh` modified (readiness check only).
- **Compose**: `server/docker-compose.yml` reviewed; Tika service may be excluded for CI.
- **Secrets**: `SSH_PRIVATE_KEY`, `SSH_HOST`, `SSH_USER`, `OCIS_URL` removed from the
  repository secret set; `OCIS_PWD` and `OCIS_VERSION` retained, passed as inputs/secrets
  to `e2etests.yml`.
- **No impact** on test scenarios, Java source, or any other workflow.
