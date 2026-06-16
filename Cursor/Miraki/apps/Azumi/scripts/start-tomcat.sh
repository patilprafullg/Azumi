#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
# shellcheck source=/dev/null
eval "$(bash "$SCRIPT_DIR/read-local-deploy-config.sh")"
. "$SCRIPT_DIR/resolve-toolchain.sh"

if [ ! -x "$TOMCAT_HOME/bin/catalina.sh" ]; then
  echo "Tomcat not installed. Run scripts/deploy-local.sh first." >&2
  exit 1
fi

JAVA_HOME_RES="${JAVA_HOME:-}"
if [ -z "$JAVA_HOME_RES" ]; then
  JAVA_HOME_RES=$(resolve_java_home) || true
fi
if [ -n "$JAVA_HOME_RES" ]; then
  export JAVA_HOME="$JAVA_HOME_RES"
fi

pushd "$TOMCAT_HOME/bin" >/dev/null
echo "Starting Tomcat on port $PORT..."
./catalina.sh run
popd >/dev/null
