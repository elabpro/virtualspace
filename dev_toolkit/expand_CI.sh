#!/bin/sh
DIR="/home/glebmillenium/virtualspace"
date +%d-%m-%Y\ %H:%M:%S >> $DIR/dev_toolkit/CI.log;
git --git-dir=$DIR/.git fetch origin >> $DIR/dev_toolkit/CI.log;
/usr/bin/make -f $DIR/server/manager/GlebMakefile >> $DIR/dev_toolkit/CI.log;
