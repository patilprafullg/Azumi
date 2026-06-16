#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
# shellcheck source=/dev/null
eval "$(bash "$SCRIPT_DIR/read-local-deploy-config.sh")"

APP_ROOT="$APP_ROOT"
TOMCAT_VERSION="10.1.34"
TOMCAT_ARCHIVE="apache-tomcat-$TOMCAT_VERSION.tar.gz"
DOWNLOAD_URL="https://archive.apache.org/dist/tomcat/tomcat-10/v$TOMCAT_VERSION/bin/$TOMCAT_ARCHIVE"
RUNTIME_DIR="$TOMCAT_HOME"
ARCHIVE_PATH="$APP_ROOT/tomcat/$TOMCAT_ARCHIVE"

if [ -x "$RUNTIME_DIR/bin/catalina.sh" ]; then
  echo "Tomcat runtime already present at $RUNTIME_DIR"
  exit 0
fi

mkdir -p "$(dirname "$ARCHIVE_PATH")"

echo "Downloading Tomcat $TOMCAT_VERSION..."
if command -v curl >/dev/null 2>&1; then
  curl -L -o "$ARCHIVE_PATH" "$DOWNLOAD_URL"
elif command -v wget >/dev/null 2>&1; then
  wget -O "$ARCHIVE_PATH" "$DOWNLOAD_URL"
else
  echo "Install curl or wget to download Tomcat." >&2
  exit 1
fi

echo "Extracting Tomcat..."
EXTRACT_ROOT="$APP_ROOT/tomcat"
mkdir -p "$EXTRACT_ROOT"
tar -xzf "$ARCHIVE_PATH" -C "$EXTRACT_ROOT"

EXTRACTED="$EXTRACT_ROOT/apache-tomcat-$TOMCAT_VERSION"
if [ -d "$RUNTIME_DIR" ]; then
  rm -rf "$RUNTIME_DIR"
fi
mv "$EXTRACTED" "$RUNTIME_DIR"

echo "Tomcat installed to $RUNTIME_DIR"
