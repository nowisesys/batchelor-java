#!/bin/sh
#
# Generate javadoc for the client library API.
#
# Author: Anders L�vgren
# Date:   2009-05-06

ANT_COMMAND="ant"
ANT_TARGET="jar"

JAVADOC="javadoc"
FOA_JAR="../../FOA/dist/foa-java-1.0.*.jar"
ENCODING="utf8"

DESTDIR="dist/api"
SRCDIR="../CommonLibrary/src src build/generated/wsimport/client"

JARS=${FOA_JAR}

if ! [ -d dist ]; then
  ${ANT_COMMAND} ${ANT_TARGET}
fi

${JAVADOC} -classpath ${JARS} -d ${DESTDIR} -encoding ${ENCODING} $(find ${SRCDIR} -type f | egrep '\.java$')
