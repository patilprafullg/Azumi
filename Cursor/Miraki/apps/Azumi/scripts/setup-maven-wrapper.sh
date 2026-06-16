#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
WRAPPER_VERSION="3.3.2"
DISTRIBUTION_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper-distribution/$WRAPPER_VERSION/maven-wrapper-distribution-$WRAPPER_VERSION-bin.zip"
ZIP_PATH="$APP_ROOT/maven-wrapper.zip"

echo "Downloading Maven Wrapper $WRAPPER_VERSION..."
if command -v curl >/dev/null 2>&1; then
  curl -L -o "$ZIP_PATH" "$DISTRIBUTION_URL"
elif command -v wget >/dev/null 2>&1; then
  wget -O "$ZIP_PATH" "$DISTRIBUTION_URL"
else
  echo "Install curl or wget to download files." >&2
  exit 1
fi

if command -v unzip >/dev/null 2>&1; then
  unzip -o "$ZIP_PATH" -d "$APP_ROOT"
else
  # try using python
  python3 - <<PY
import zipfile,sys
with zipfile.ZipFile(r"$ZIP_PATH") as z:
    z.extractall(r"$APP_ROOT")
PY
fi
rm -f "$ZIP_PATH"

cat > "$APP_ROOT/.mvn/wrapper/maven-wrapper.properties" <<EOF
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip
wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar
EOF

if [ ! -f "$APP_ROOT/mvnw" ]; then
  echo "Maven wrapper files were not extracted as expected." >&2
  exit 1
fi
chmod +x "$APP_ROOT/mvnw"

echo "Maven wrapper installed."
