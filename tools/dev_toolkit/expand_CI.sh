#!/bin/sh
DIR="/home/glebmillenium/virtualspace"
TIME=$(date +%d-%m-%Y\ %H:%M:%S);
git --git-dir=$DIR/.git fetch origin >> $DIR/tools/dev_toolkit/CI_$TIME.log;
/usr/bin/make -f $DIR/server/manager/GlebMakefile >> $DIR/tools/dev_toolkit/CI_$TIME.log;
echo "\nTesting Results:\n" >> $DIR/tools/dev_toolkit/CI_$TIME.log;

gcov -o $DIR/server/manager/build/Debug/GNU-Linux/ $DIR/server/manager/ConnectorDB.cpp >> CI_$TIME.log;

