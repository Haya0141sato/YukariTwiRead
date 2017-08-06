#!/bin/sh

id=
if [ ${#} -eq 1 ]
then
id=${1}
fi
rlwrap java -jar `dirname ${0}`/ytr.jar ${id}
