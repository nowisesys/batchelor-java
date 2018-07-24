#!/bin/bash
#
# This script will build splitted jar-files as a complement to the 
# big batchelor-client-x.y.z.jar library.
#
# Author: Anders Lövgren
# Date:   2009-05-13

cwd="`pwd`"
version="1.0.6"
distdir="$cwd/dist"
classes="$cwd/build/classes"

# Build small, service specific libraries:
( cd $classes
  for service in rest soap; do
    echo "jar cf ${distdir}/batchelor-${service}-client-${version}.jar"
    jar cf ${distdir}/batchelor-${service}-client-${version}.jar `find . -type f | grep ${service}`
    if [ "$service" == "soap" ]; then
      jar uf ${distdir}/batchelor-${service}-client-${version}.jar META-INF/BatchelorSoapService.xml META-INF/wsit-client.xml
    fi
    # Generate index:
    jar i ${distdir}/batchelor-${service}-client-${version}.jar
  done
)
  
