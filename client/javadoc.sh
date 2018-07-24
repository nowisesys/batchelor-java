#!/bin/sh
#
# Generate javadoc for the client library API.
#
# Author: Anders Lövgren
# Date:   2009-05-06

ANT_COMMAND="ant"
ANT_TARGET="jar"

JAVADOC="javadoc"
FOA_JAR="../../foa-java/dist/foa-java-1.2.*.jar"
ENCODING="utf8"

DESTDIR="dist/api"
SRCDIR="../common/src src build/generated-sources/jax-ws"

JARS=${FOA_JAR}

if ! [ -d dist ]; then
  ${ANT_COMMAND} ${ANT_TARGET}
fi

${JAVADOC} -classpath ${JARS} -d ${DESTDIR} -encoding ${ENCODING} -Xdoclint:none $(find ${SRCDIR} -type f | egrep '\.java$')
