## Change: Move oCIS backend provisioning to the CI runner

CI infrastructure change — no Gherkin scenarios involved.

### Problem

The current CI pipeline provisions the oCIS backend by SSHing into an external VM via Ansible.
Ansible is used purely as a transport layer: it copies `server/docker-compose.yml` and
`server/create_server.sh` to the remote VM and runs the script there.
The script itself does the real work: it starts the oCIS container via Docker Compose, waits
for it to be ready, and provisions the test data (users Alice, Bob, Charles; a test group;
the Admin role assignment for Alice) through the Graph API.

This creates a hard dependency on external infrastructure and secrets (SSH key, remote host,
remote user), makes the pipeline fragile when that VM is unavailable, and requires a separate
cleanup job after each run.

### Target solution

Run `create_server.sh` directly on the same GitHub Actions runner that hosts the Android
emulator, passing `https://10.0.2.2:9200` as `BASE_URL`. The provisioning result is identical
to the current remote setup because the same script runs — Ansible is only removed as the
transport mechanism.

The pipeline becomes self-contained: the backend starts and is fully provisioned before the
emulator boots, and the container is discarded automatically when the job ends (no cleanup job
needed).

Before the emulator launches, the pipeline must confirm that the backend is up and accepting
connections — not just that the container has started. If it is not reachable within a
reasonable timeout, the job must fail with a clear diagnostic.

### Output artifacts

The existing artifacts (HTML report, execution logs, video recordings) are unaffected.

A new artifact must be added: oCIS container logs, captured at the end of the job regardless
of outcome and uploaded as a separate artifact. With a local container that is discarded when
the job ends, these logs are the only way to diagnose backend-side failures after the fact.

### Constraints

- The oCIS container must run on the same runner as the emulator. The Android emulator
  reaches the runner host via `10.0.2.2`, so backend startup and emulator launch must be
  co-located in the same job.
- `create_server.sh` already handles provisioning (docker compose + Graph API calls); it must
  be reused as-is. No duplication or reimplementation of its logic.
- The SSH-based provisioning and cleanup workflows (`ocisbackend.yml`, `cleanup.yml`) and the
  corresponding jobs in `e2e.yml` (`create_ocis`, `cleanup`) are no longer needed and should
  be removed.
- `OCIS_PWD` and `OCIS_VERSION` must remain configurable (currently passed as secrets/inputs).

### Risks and required mitigations

**TLS / self-signed certificate**
oCIS serves HTTPS with a self-signed cert issued for the `BASE_URL` hostname. The Java test
harness already skips TLS verification for its own API calls, but the Android app running on
the emulator must also accept this cert. Before implementation, verify whether the qaRelease
APK network security config permits self-signed certificates. If it does not, define and
implement a fallback — either configure oCIS to serve plain HTTP (if supported with
`OCIS_INSECURE=true`) or add the cert to the emulator trust store before launching the app.
This must be resolved before the rest of the implementation proceeds.

**Runner resource pressure**
GitHub-hosted `ubuntu-latest` runners have 7 GB RAM and 2 CPUs. The emulator already
requests 3 GB RAM. Running oCIS and Tika on the same runner may cause resource contention
that manifests as flaky tests or emulator slowdowns. Verify whether Tika is exercised by any
scenario in the CI tag filter — if not, it should be excluded from the Docker Compose setup
used in CI to reduce resource usage.

**Readiness check fragility**
`create_server.sh` currently waits a fixed 10 seconds before making Graph API calls to
provision users. On a cold runner pulling the oCIS image for the first time this may not be
sufficient: if oCIS is not ready, the provisioning calls return errors silently and tests fail
with authentication errors that are hard to diagnose. The readiness wait in `create_server.sh`
must be replaced with a poll loop that verifies the backend is actually responding before
provisioning begins.
