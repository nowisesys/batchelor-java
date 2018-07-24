#!/bin/sh
#
# Create binary release package.
#
# Author: Anders Lövgren
# Date:   2018-07-25

path=dist
file=batchelor-java-1.0.6.zip

if [ -d dist ]; then
  rm -rf dist/*
fi

mkdir -p dist/lib

cp -a explorer/dist/*.jar dist
cp -a explorer/dist/lib/* dist/lib

cp -a common/dist/*.jar dist

cp -a client/dist/*.jar dist
cp -a client/dist/lib/* dist/lib

( cd dist && zip -r $file * )

echo "Created binary distribution file $path/$file"
