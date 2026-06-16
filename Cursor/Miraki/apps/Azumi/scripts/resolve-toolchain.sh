#!/usr/bin/env bash
set -euo pipefail

# Resolve Java home: prefer $JAVA_HOME, otherwise search common JVM locations
resolve_java_home() {
  if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then
    echo "$JAVA_HOME"
    return 0
  fi
  # Common locations on Linux
  candidates=(/usr/lib/jvm /usr/java /opt/java /opt)
  for base in "${candidates[@]}"; do
    if [ -d "$base" ]; then
      for j in "$base"/*; do
        if [ -x "$j/bin/java" ]; then
          echo "$j"
          return 0
        fi
      done
    fi
  done
  # fallback to which java
  if command -v java >/dev/null 2>&1; then
    java_path=$(readlink -f "$(command -v java)")
    echo "$(dirname "$(dirname "$java_path")")"
    return 0
  fi
  echo "" >&2
  return 1
}

# Resolve Maven command: prefer ./mvnw in app root, otherwise use mvn from PATH
resolve_maven_command() {
  local app_root="$1"
  if [ -x "$app_root/mvnw" ]; then
    echo "$app_root/mvnw"
    return 0
  fi
  if command -v mvn >/dev/null 2>&1; then
    echo "mvn"
    return 0
  fi
  echo "" >&2
  return 1
}
