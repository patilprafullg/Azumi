#!/usr/bin/env bash
set -euo pipefail

# Usage: run-local.sh
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
# forward args to deploy-local.sh if needed
"$SCRIPT_DIR/deploy-local.sh" "$@"
"$SCRIPT_DIR/start-tomcat.sh"
