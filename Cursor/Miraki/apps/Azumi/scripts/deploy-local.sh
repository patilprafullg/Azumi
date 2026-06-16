#!/usr/bin/env bash
set -euo pipefail

# Usage: deploy-local.sh [--build]
BUILD=false
if [ "${1:-}" = "--build" ] || [ "${1:-}" = "-b" ]; then
  BUILD=true
fi

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
# load settings
# shellcheck source=/dev/null
eval "$(bash "$SCRIPT_DIR/read-local-deploy-config.sh")"
# shellcheck source=/dev/null
. "$SCRIPT_DIR/resolve-toolchain.sh"

APP_ROOT="$APP_ROOT"

if [ "$BUILD" = true ] || [ ! -f "$WAR_SOURCE" ]; then
  "$SCRIPT_DIR/build.sh"
fi

"$SCRIPT_DIR/install-tomcat.sh"

RUNTIME_DIR="$TOMCAT_HOME"
WEBAPPS_DIR="$RUNTIME_DIR/webapps"
CONTEXT_DIR="$WEBAPPS_DIR/azumi"

mkdir -p "$WEBAPPS_DIR"

if [ -d "$CONTEXT_DIR" ]; then
  rm -rf "$CONTEXT_DIR"
fi
if [ -f "$WAR_TARGET" ]; then
  rm -f "$WAR_TARGET"
fi

cp -f "$WAR_SOURCE" "$WAR_TARGET"

SERVER_XML="$RUNTIME_DIR/conf/server.xml"
if [ -f "$SERVER_XML" ]; then
  # replace port="..." for the Connector on port 8080 -> use PORT
  # This is a simple replace of first occurrence of port="8080" and similar
  sed -i "s/port=\"[0-9]\+\"/port=\"$PORT\"/" "$SERVER_XML"
fi

SETENV_PATH="$RUNTIME_DIR/bin/setenv.sh"
LOG_DIR="$APP_ROOT/logs"
DATA_DIR="$APP_ROOT/${DATABASE_PATH#./}"
mkdir -p "$LOG_DIR" "$DATA_DIR"
cat > "$SETENV_PATH" <<EOF
#!/usr/bin/env bash
export JAVA_OPTS="-Dazumi.database.path=$DATA_DIR -Dazumi.logging.level=$LOG_LEVEL -Dazumi.logging.file=$LOG_FILE"
EOF
chmod +x "$SETENV_PATH"

echo "Deployed azumi.war to Tomcat."
echo "Context path: $CONTEXT_PATH"
echo "Login: http://localhost:$PORT$CONTEXT_PATH/login"
