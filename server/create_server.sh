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

#######################################
# Create oCIS server
#######################################

OCIS_VERSION="$OCIS_VERSION" BASE_URL="$BASE_URL" OCIS_PWD="$OCIS_PWD" docker-compose -f ./docker-compose.yml up -d

#######################################
# Wait for server to be ready
#######################################

echo "Waiting 10 secs for server to be fully ready..."
sleep 10

#######################################
# Retrieve applications once
#######################################

apps_response=$(curl -sk $AUTH "$BASE_URL/graph/v1.0/applications")

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
# Get Space Admin role ID
#######################################

SPACE_ADMIN_ID=$(echo "$apps_response" | jq -r '
  .value[]
  | .appRoles[]
  | select(.displayName=="Space Admin")
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

ALICE_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "alice",
    "displayName": "Alice",
    "mail": "alice@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

BOB_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "bob",
    "displayName": "Bob",
    "mail": "bob@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

CHARLES_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "charles",
    "displayName": "Charles",
    "mail": "charly@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

USER1_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "user1",
    "displayName": "John Smith",
    "mail": "user1@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

USER2_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "user2",
    "displayName": "Mark Philips",
    "mail": "user2@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

USER3_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "user3",
    "displayName": "user3",
    "mail": "user3@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

USER4_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/users" \
  $JSON_HEADER \
  -d '{
    "onPremisesSamAccountName": "user4",
    "displayName": "user4",
    "mail": "user4@own.com",
    "passwordProfile": { "password": "a" }
  }' | jq -r '.id')

#######################################
# Create a group and get its ID
#######################################

TEST_ID=$(curl -sk $AUTH -X POST "$BASE_URL/graph/v1.0/groups" \
  $JSON_HEADER \
  -d '{
    "displayName": "test"
  }' | jq -r '.id')

#######################################
# Add BOB to TEST
#######################################

curl -sk $AUTH -X POST \
  "$BASE_URL/graph/v1.0/groups/$TEST_ID/members/\$ref" \
  $JSON_HEADER \
  -d "{
    \"@odata.id\": \"$BASE_URL/graph/v1.0/users/$BOB_ID\"
  }"

#######################################
# Assign Admin role to ALICE
#######################################

curl -sk $AUTH -X POST \
  "$BASE_URL/graph/v1.0/users/$ALICE_ID/appRoleAssignments" \
  $JSON_HEADER \
  -d "{
    \"appRoleId\": \"$ADMIN_ID\",
    \"principalId\": \"$ALICE_ID\",
    \"resourceId\": \"$RESOURCE_ID\"
  }" >/dev/null

#######################################
# Assign Admin role to USER1
#######################################

curl -sk $AUTH -X POST \
  "$BASE_URL/graph/v1.0/users/$USER1_ID/appRoleAssignments" \
  $JSON_HEADER \
  -d "{
    \"appRoleId\": \"$ADMIN_ID\",
    \"principalId\": \"$USER1_ID\",
    \"resourceId\": \"$RESOURCE_ID\"
  }" >/dev/null

#######################################
# Assign Space Admin role to USER2
#######################################

curl -sk $AUTH -X POST \
  "$BASE_URL/graph/v1.0/users/$USER2_ID/appRoleAssignments" \
  $JSON_HEADER \
  -d "{
    \"appRoleId\": \"$SPACE_ADMIN_ID\",
    \"principalId\": \"$USER2_ID\",
    \"resourceId\": \"$RESOURCE_ID\"
  }" >/dev/null
