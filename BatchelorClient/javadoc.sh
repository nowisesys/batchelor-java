#!/bin/sh
#
# Generate javadoc for the client library API.
#
# Author: Anders Lövgren
# Date:   2009-05-06

ANT_COMMAND="ant"
ANT_TARGET="jar"

JAVADOC="javadoc"
FOA_JAR="../../FOA/dist/foa-java-1.0.*.jar"
ENCODING="utf8"

DESTDIR="dist/api"
# NetBeans 6.5:
# SRCDIR="../CommonLibrary/src src build/generated/wsimport/client"
# NetBeans 6.7.1:
SRCDIR="../CommonLibrary/src src build/generated-sources/jax-ws"

JARS=${FOA_JAR}

if ! [ -d dist ]; then
  ${ANT_COMMAND} ${ANT_TARGET}
fi

${JAVADOC} -classpath ${JARS} -d ${DESTDIR} -encoding ${ENCODING} $(find ${SRCDIR} -type f | egrep '\.java$')
