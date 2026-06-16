#!/usr/bin/env bash
set -euo pipefail

# Usage: build.sh [--run]
RUN=false
if [ "${1:-}" = "--run" ] || [ "${1:-}" = "-r" ]; then
  RUN=true
fi

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
# load settings
# shellcheck source=/dev/null
eval "$(bash "$SCRIPT_DIR/read-local-deploy-config.sh")"
# shellcheck source=/dev/null
. "$SCRIPT_DIR/resolve-toolchain.sh"

APP_ROOT="$APP_ROOT"
DATA_DIR="$APP_ROOT/${DATABASE_PATH#./}"
mkdir -p "$DATA_DIR"

JAVA_HOME_RES=""
if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_HOME_RES="$JAVA_HOME"
else
  JAVA_HOME_RES=$(resolve_java_home) || true
fi
if [ -z "$JAVA_HOME_RES" ]; then
  echo "JAVA_HOME not found. Please install JDK 17+ and set JAVA_HOME." >&2
  exit 1
fi
export JAVA_HOME="$JAVA_HOME_RES"

MVN_CMD=$(resolve_maven_command "$APP_ROOT") || true
if [ -z "$MVN_CMD" ]; then
  echo "Maven not found. Install Maven or run setup-maven-wrapper.sh" >&2
  exit 1
fi

echo "Building AzumiDesigns..."
echo "  Port: $PORT"
echo "  Context: $CONTEXT_PATH"
echo "  JAVA_HOME: $JAVA_HOME"

pushd "$APP_ROOT" >/dev/null

"$MVN_CMD" clean package -Dtomcat.port="$PORT" -Dtomcat.contextPath="$CONTEXT_PATH" -q

popd >/dev/null

echo "Build succeeded: target/azumi.war"

if [ "$RUN" = true ]; then
  echo "Starting embedded Tomcat on port $PORT..."
  export MAVEN_OPTS="-Dazumi.database.path=$DATA_DIR"
  "$MVN_CMD" tomcat10:run -Dtomcat.port="$PORT" -Dtomcat.contextPath="$CONTEXT_PATH"
fi
