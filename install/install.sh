#!/bin/bash

function assertBinaryExists() {
    which $1 &> /dev/null || ( echo "ERROR: Missing required binary: $1" && exit 5 )
}

assertBinaryExists tar

if [[ "$(id -u)" != "0" ]]; then
    echo "ERROR: You need to be superuser to run installation script"
    exit 1
fi

cd $(dirname $(basename $0))

ZMAN_ARCHIVE=zman-assembly/target/assembly/zman-assembly.tar.gz

if ! [[ -e $ZMAN_ARCHIVE ]]; then
    echo "ERROR: You need to build project first!"
    echo 'INFO: Use "mvn clean install" to build project'
    exit 2
fi

cp $ZMAN_ARCHIVE /opt

[[ -e /etc/systemd/system/zman.service ]] && systemctl stop zman

rm -rf /opt/zman/bin &> /dev/null

cd /opt
tar zxf zman.tar.gz

cp -f zman/zman.service /etc/systemd/system
systemctl daemon-reload
