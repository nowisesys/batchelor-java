#!/bin/sh
#
# Create binary release package.
#
# Author: Anders Lövgren
# Date:   2018-07-25

path=dist
file=batchelor-java-1.0.5.zip

mkdir dist/lib

cp -a explorer/dist/*.jar dist
cp -a explorer/dist/lib/* dist/lib

cp -a common/dist/*.jar dist

cp -a client/dist/*.jar dist
cp -a client/dist/lib/* dist/lib

( cd dist && zip -r batchelor-java-1.0.5.zip * )

echo "Created binary distribution file $path/$name"
