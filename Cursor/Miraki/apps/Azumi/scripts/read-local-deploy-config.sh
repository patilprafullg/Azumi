#!/usr/bin/env bash
# Outputs exported shell variables for local deploy settings
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
CONFIG_PATH="${1:-$APP_ROOT/config/local-deploy.json}"
if [ ! -f "$CONFIG_PATH" ]; then
  echo "Config not found: $CONFIG_PATH" >&2
  exit 1
fi
python3 - <<PY "$CONFIG_PATH" "$APP_ROOT"
import json,sys,os
p=sys.argv[1]
app_root=sys.argv[2]
cfg=json.load(open(p))
server=cfg.get('server',{})
logging=cfg.get('logging',{})
database=cfg.get('database',{})
print(f"export APP_ROOT='{app_root}'")
print(f"export PORT='{server.get('port',8080)}'")
print(f"export CONTEXT_PATH='{server.get('contextPath','/')}'")
print(f"export RELOAD_ON_SAVE='{server.get('reloadOnSave',False)}'")
print(f"export DATABASE_PATH='{database.get('path','./data/local-db')}'")
print(f"export LOG_LEVEL='{logging.get('level','INFO')}'")
print(f"export LOG_FILE='{logging.get('file','logs/azumi.log')}'")
print(f"export TOMCAT_HOME='{os.path.join(app_root,'tomcat','runtime')}'")
print(f"export WAR_SOURCE='{os.path.join(app_root,'target','azumi.war')}'")
print(f"export WAR_TARGET='{os.path.join(app_root,'tomcat','runtime','webapps','azumi.war')}'")
PY

# shell will eval the exports from python when this script is sourced
