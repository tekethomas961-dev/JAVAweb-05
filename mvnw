#!/bin/sh

BASE_DIR="${MAVEN_BASEDIR:-$(dirname "$0")}"
WRAPPER_JAR="$BASE_DIR/.mvn/wrapper/maven-wrapper.jar"
JAVA_EXE="${JAVA_HOME:+$JAVA_HOME/bin/}java"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Couldn't find $WRAPPER_JAR, downloading it ..."
  mkdir -p "$BASE_DIR/.mvn/wrapper"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar" -o "$WRAPPER_JAR"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar" -O "$WRAPPER_JAR"
  else
    echo "Neither curl nor wget is available to download Maven Wrapper." >&2
    exit 1
  fi
fi

exec "$JAVA_EXE" -classpath "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
