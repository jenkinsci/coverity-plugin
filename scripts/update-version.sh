#! /bin/bash
#
# Script to update versions
#

if [[ $# -ne 1 ]];then
   echo "Usage: $0 <toversion>"
   exit 1
fi

TOVERSION=$1

REPO=$(git rev-parse --show-toplevel)
cd $REPO

# update version (with -SNAPSHOT added)
mvn versions:set -DnewVersion="$TOVERSION-SNAPSHOT"


exit 0
