#!/bin/bash
set -x

#######################################
# Help / Usage
#######################################

help() {
  cat <<EOF
Usage:
  $0 <BASE_URL> <OCIS_VERSION>

  Required: set the environment variable OCIS_PWD to the admin password

Example:
  $0 https://192.168.1.10:9200 7.3.0

Arguments:
  BASE_URL       Base URL where oCIS backend will be reachable
  OCIS_VERSION   oCIS Docker image tag/version to build

Options:
  -h, --help     Show this help message and exit
EOF
}

if [[ "$1" == "-h" || "$1" == "--help" ]]; then
  help
  exit 0
fi

if [[ -z "$1" || -z "$2" ]]; then
  help
  exit 1
fi

#######################################
# Helpers
#######################################

check_id() {
  local name="$1"
  local id="$2"
  if [ -z "$id" ] || [ "$id" = "null" ]; then
    echo "ERROR: $name ID is empty or null — provisioning failed"
    exit 1
  fi
  echo "$name ID: $id"
}

api_post() {
  local label="$1"
  local url="$2"
  local body="$3"
  local response
  response=$(curl -sk $AUTH -X POST "$url" $JSON_HEADER -d "$body")
  echo "[$label] response: $response" >&2
  echo "$response"
}

#######################################
# Configuration
#######################################

BASE_URL="$1"
OCIS_VERSION="$2"
OCIS_PWD="${OCIS_PWD:-ownCloud1!}"
AUTH="-u admin:$OCIS_PWD"
JSON_HEADER="-H Content-Type:application/json"

# BASE_URL may use 10.0.2.2 (Android emulator alias for host loopback).
# curl calls run on the host, so replace it with localhost.
LOCAL_URL="${BASE_URL//10.0.2.2/localhost}"

echo "BASE_URL: $BASE_URL"
echo "LOCAL_URL: $LOCAL_URL"
echo "OCIS_VERSION: $OCIS_VERSION"

#######################################
# Create oCIS server
#######################################

OCIS_VERSION="$OCIS_VERSION" BASE_URL="$BASE_URL" OCIS_PWD="$OCIS_PWD" docker compose -f ./docker-compose.yml up -d

#######################################
# Wait for server to be ready
#######################################

echo "Waiting for oCIS to be ready..."
TIMEOUT=120
ELAPSED=0
until [ "$(curl -sk -o /dev/null -w "%{http_code}" "$LOCAL_URL/ocs/v1.php/cloud/capabilities")" = "200" ]; do
  if [ "$ELAPSED" -ge "$TIMEOUT" ]; then
    echo "ERROR: oCIS did not become ready within ${TIMEOUT}s"
    exit 1
  fi
  sleep 2
  ELAPSED=$((ELAPSED + 2))
done
echo "oCIS is ready (${ELAPSED}s)"

#######################################
# Verify BASE_URL is reachable (data service connectivity check)
#######################################

echo "Verifying connectivity via BASE_URL ($BASE_URL)..."
BASE_HTTP_CODE=$(curl -sk -o /dev/null -w "%{http_code}" "$BASE_URL/ocs/v1.php/cloud/capabilities")
if [ "$BASE_HTTP_CODE" != "200" ]; then
  echo "ERROR: oCIS not reachable at $BASE_URL (HTTP $BASE_HTTP_CODE)"
  echo "Internal data service calls will fail — check network configuration"
  exit 1
fi
echo "BASE_URL connectivity OK"

#######################################
# Wait for Graph API (auth-basic service needs extra time after capabilities)
#######################################

echo "Waiting for Graph API to be ready..."
GRAPH_TIMEOUT=60
GRAPH_ELAPSED=0
apps_response=""
until apps_response=$(curl -sk $AUTH "$LOCAL_URL/graph/v1.0/applications") && \
      echo "$apps_response" | jq -e '.value' >/dev/null 2>&1; do
  if [ "$GRAPH_ELAPSED" -ge "$GRAPH_TIMEOUT" ]; then
    echo "ERROR: Graph API did not become ready within ${GRAPH_TIMEOUT}s"
    echo "Last response: $apps_response"
    exit 1
  fi
  echo "Graph API not ready yet (${GRAPH_ELAPSED}s), response: $apps_response"
  sleep 3
  GRAPH_ELAPSED=$((GRAPH_ELAPSED + 3))
done
echo "Graph API ready (${GRAPH_ELAPSED}s after capabilities)"
echo "Applications response: $apps_response"

#######################################
# Get Admin role ID from the target application
#######################################

ADMIN_ID=$(echo "$apps_response" | jq -r '
  .value[]
  | .appRoles[]
  | select(.displayName=="Admin")
  | .id
')
check_id "Admin role" "$ADMIN_ID"

#######################################
# Get resource ID
#######################################

RESOURCE_ID=$(echo "$apps_response" | jq -r '
  .value[]
  | select(.displayName=="ownCloud Infinite Scale")
  | .id
')
check_id "Resource" "$RESOURCE_ID"

#######################################
# Create users and store their IDs
#######################################

ALICE_RESPONSE=$(api_post "Create Alice" "$LOCAL_URL/graph/v1.0/users" '{
    "onPremisesSamAccountName": "alice",
    "displayName": "Alice",
    "mail": "alice@own.com",
    "passwordProfile": { "password": "a" }
  }')
ALICE_ID=$(echo "$ALICE_RESPONSE" | jq -r '.id')
check_id "Alice" "$ALICE_ID"

BOB_RESPONSE=$(api_post "Create Bob" "$LOCAL_URL/graph/v1.0/users" '{
    "onPremisesSamAccountName": "bob",
    "displayName": "Bob",
    "mail": "bob@own.com",
    "passwordProfile": { "password": "a" }
  }')
BOB_ID=$(echo "$BOB_RESPONSE" | jq -r '.id')
check_id "Bob" "$BOB_ID"

CHARLES_RESPONSE=$(api_post "Create Charles" "$LOCAL_URL/graph/v1.0/users" '{
    "onPremisesSamAccountName": "charles",
    "displayName": "Charles",
    "mail": "charly@own.com",
    "passwordProfile": { "password": "a" }
  }')
CHARLES_ID=$(echo "$CHARLES_RESPONSE" | jq -r '.id')
check_id "Charles" "$CHARLES_ID"

#######################################
# Create a group and get its ID
#######################################

TEST_RESPONSE=$(api_post "Create group test" "$LOCAL_URL/graph/v1.0/groups" '{
    "displayName": "test"
  }')
TEST_ID=$(echo "$TEST_RESPONSE" | jq -r '.id')
check_id "Group test" "$TEST_ID"

#######################################
# Add BOB to TEST
#######################################

echo "Adding Bob to group test..."
ADD_BOB_RESPONSE=$(curl -sk $AUTH -X POST \
  "$LOCAL_URL/graph/v1.0/groups/$TEST_ID/members/\$ref" \
  $JSON_HEADER \
  -d "{
    \"@odata.id\": \"$LOCAL_URL/graph/v1.0/users/$BOB_ID\"
  }")
echo "[Add Bob to test] response: $ADD_BOB_RESPONSE"

#######################################
# Assign Admin role to ALICE
#######################################

echo "Assigning Admin role to Alice..."
ROLE_RESPONSE=$(curl -sk $AUTH -X POST \
  "$LOCAL_URL/graph/v1.0/users/$ALICE_ID/appRoleAssignments" \
  $JSON_HEADER \
  -d "{
    \"appRoleId\": \"$ADMIN_ID\",
    \"principalId\": \"$ALICE_ID\",
    \"resourceId\": \"$RESOURCE_ID\"
  }")
echo "[Assign Admin role to Alice] response: $ROLE_RESPONSE"

#######################################
# Summary
#######################################

echo "--- Provisioning complete ---"
echo "Alice ID:   $ALICE_ID"
echo "Bob ID:     $BOB_ID"
echo "Charles ID: $CHARLES_ID"
echo "Group test ID: $TEST_ID"
echo "Admin role ID: $ADMIN_ID"
echo "Resource ID:   $RESOURCE_ID"
