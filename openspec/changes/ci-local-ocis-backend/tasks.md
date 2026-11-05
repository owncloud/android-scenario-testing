## 1. Prerequisite investigations (must complete before any workflow change)

- [x] 1.1 Verify whether the qaRelease APK network security config accepts self-signed TLS
      certificates. Check `owncloud/android` repo for `network_security_config.xml` or
      equivalent. Record the finding and choose Option A (HTTP mode) or Option B (emulator
      trust store) as documented in `design.md – Decisions §4`.
      FINDING: `owncloudApp/src/main/res/xml/network_security_config.xml` sets
      `cleartextTrafficPermitted="true"` and trusts user-added CAs. When an untrusted
      self-signed cert is encountered, the app shows an acceptance dialog in the login view —
      it does not hard-reject it. The test suite automation already handles clicking that dialog.
      No emulator trust store setup or cert extraction needed.
      **Option B (HTTPS with self-signed cert) selected.** `BASE_URL=https://10.0.2.2:9200`.
- [x] 1.2 Verify whether `OCIS_INSECURE=true` allows oCIS to serve plain HTTP on port 9200,
      or whether it only affects oCIS's own outbound TLS. Check oCIS documentation or test
      locally. Required only if Option A was selected in 1.1.
      FINDING: oCIS only operates over HTTPS — plain HTTP is not supported. `OCIS_INSECURE=true`
      only disables TLS certificate validation for oCIS's own outbound calls; it does not
      disable the HTTPS listener. `PROXY_TLS=false` would disable HTTPS but break oCIS.
      **Option A (plain HTTP) is not viable.** Option B confirmed as the correct path.
- [x] 1.3 Verify whether any scenario covered by the CI tag filter
      (`not @ignore and not @noocis and not @noci`) depends on Tika / full-text search.
      Grep feature files for relevant tags or step text. Record finding: Tika needed or not.
      FINDING: Zero feature files reference search, full-text, or Tika. Tika is not exercised
      by any CI scenario. **Tika must be excluded from the CI Compose profile.**

## 2. Update `create_server.sh`

- [x] 2.1 Replace the `sleep 10` wait with a poll loop that checks the oCIS capabilities
      endpoint (`/ocs/v1.php/cloud/capabilities`) until it returns HTTP 200. Set the timeout
      to at least 120 seconds to cover cold image pulls. Exit with a non-zero code and a
      clear error message if the timeout is exceeded.

## 3. Update `server/docker-compose.yml` (conditional on investigation results)

- [x] 3.1 If Option A (HTTP) was confirmed viable in 1.2: add or document the configuration
      change needed to make oCIS serve plain HTTP. Update `docker-compose.yml` accordingly.
      SKIPPED — Option A not selected (oCIS is HTTPS-only).
- [x] 3.2 If Tika is not needed (1.3): add a CI-specific Compose override file
      (`docker-compose.ci.yml`) that removes the Tika service, so the default
      `docker-compose.yml` remains unchanged for local use.
      DONE — Tika confirmed unused across the entire test harness (all "search" references
      are UI user/group/space name lookups, not full-text content search). Tika removed
      directly from `server/docker-compose.yml` (tika service + three env vars); no CI
      overlay needed. `docker-compose.ci.yml` deleted.

## 4. Update `e2etests.yml`

- [x] 4.1 Add a step to start the backend: run `docker compose up -d` using the appropriate
      Compose file(s) based on task 3 results. Set `OCIS_VERSION`, `OCIS_PWD`, and
      `BASE_URL` as env vars. Place this step before `Start Appium`.
      SUPERSEDED — `create_server.sh` already calls `docker compose up -d` internally;
      a separate step would start the container twice. Folded into 4.2.
- [x] 4.2 Add a step to provision the backend: call `./server/create_server.sh` with
      `BASE_URL` and `OCIS_VERSION` as arguments and `OCIS_PWD` as an env var. Place this
      step before `Start Appium` (replaces 4.1 — the script handles both start and provision).
      NOTE: script uses a relative path to docker-compose.yml so it is run from `./server/`
      (matching how Ansible used it via chdir).
- [x] 4.3 If Option B (emulator trust store) was selected in 1.1: add a step to extract
      the oCIS self-signed cert and install it into the emulator trust store before the
      app is launched. Place this step after 4.2 and before `Run Emulator & Tests`.
      NO-OP — Option B selected, but the test suite already handles the cert acceptance
      dialog via automation. No trust store manipulation needed.
- [x] 4.4 Add a step (with `if: always()`) to capture oCIS container logs:
      run `docker compose logs ocis` and save the output to a file.
- [x] 4.5 Add an upload step (with `if: always()`) to publish the oCIS log file as a
      separate CI artifact named `ocis-logs`. Pin the action SHA following the project's
      OSPO policy. Used same SHA as existing upload-artifact steps in the workflow.
- [x] 4.6 Expose `OCIS_VERSION` and `OCIS_PWD` as inputs/secrets on `e2etests.yml`
      (workflow_call interface) so `e2e.yml` can pass them through.
      Also removed `oc_server_url` secret — URL is now fixed to `https://10.0.2.2:9200`
      as a job-level env var (no longer caller-supplied).

## 5. Update `e2e.yml`

- [ ] 5.1 Remove the `create_ocis` job (call to `ocisbackend.yml`) and all SSH secrets
      passed to it (`SSH_PRIVATE_KEY`, `SSH_HOST`, `SSH_USER`, `OCIS_URL`).
- [ ] 5.2 Remove the `cleanup` job (call to `cleanup.yml`) and its SSH secrets.
- [ ] 5.3 Pass `OCIS_VERSION` and `OCIS_PWD` to `e2etests.yml` via its updated interface.
- [ ] 5.4 Remove the `needs` dependency on `create_ocis` and `cleanup` from the `tests` job.

## 6. Delete obsolete workflows

- [ ] 6.1 Delete `.github/workflows/ocisbackend.yml`.
- [ ] 6.2 Delete `.github/workflows/cleanup.yml`.

## 7. Verification

- [ ] 7.1 Run `./gradlew clean compileJava compileTestJava` to confirm no Java compilation
      is broken (expected: no changes to Java source, but confirms baseline).
- [ ] 7.2 Trigger the updated `e2etests.yml` workflow manually (or via `e2e.yml`) against a
      real emulator run and confirm: backend starts, provisioning completes, tests pass,
      and the `ocis-logs` artifact appears in the run summary.
