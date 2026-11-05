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
# Configuration
#######################################

BASE_URL="$1"
OCIS_VERSION="$2"
AUTH="-u admin:$OCIS_PWD"
JSON_HEADER="-H Content-Type:application/json"

# BASE_URL may use 10.0.2.2 (Android emulator alias for host loopback).
# curl calls run on the host, so replace it with localhost.
LOCAL_URL="${BASE_URL//10.0.2.2/localhost}"

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
# Retrieve applications once
#######################################

apps_response=$(curl -sk $AUTH "$LOCAL_URL/graph/v1.0/applications")

#######################################
# Get Admin role ID from the target application
#######################################

ADMIN_ID=$(echo "$apps_response" | jq -r '
  .value[]
  | .appRoles[]
  | select(.displayName=="Admin")
  | .id
')

#######################################
# Get resource ID
#######################################

RESOURCE_ID=$(echo "$apps_response" | jq -r '
  .value[]
  | select(.displayName=="ownCloud Infinite Scale")
  | .id
')

#######################################
# Create users and store their IDs
#######################################

ALICE_ID=$(curl -sk $AUTH -X POST "$LOCAL_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "alice",
    "displayName": "Alice",
    "mail": "alice@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

BOB_ID=$(curl -sk $AUTH -X POST "$LOCAL_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "bob",
    "displayName": "Bob",
    "mail": "bob@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

CHARLES_ID=$(curl -sk $AUTH -X POST "$LOCAL_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "charles",
    "displayName": "Charles",
    "mail": "charly@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

#######################################
# Create a group and get its ID
#######################################

TEST_ID=$(curl -sk $AUTH -X POST "$LOCAL_URL/graph/v1.0/groups" \
  $JSON_HEADER \
  -d '{
    "displayName": "test"
  }' | jq -r '.id')

#######################################
# Add BOB to TEST
#######################################

curl -sk $AUTH -X POST \
  "$LOCAL_URL/graph/v1.0/groups/$TEST_ID/members/\$ref" \
  $JSON_HEADER \
  -d "{
    \"@odata.id\": \"$LOCAL_URL/graph/v1.0/users/$BOB_ID\"
  }"

#######################################
# Assign Admin role to ALICE
#######################################

curl -sk $AUTH -X POST \
  "$LOCAL_URL/graph/v1.0/users/$ALICE_ID/appRoleAssignments" \
  $JSON_HEADER \
  -d "{
    \"appRoleId\": \"$ADMIN_ID\",
    \"principalId\": \"$ALICE_ID\",
    \"resourceId\": \"$RESOURCE_ID\"
  }" >/dev/null
