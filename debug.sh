#!/bin/sh

id=
if [ ${#} -eq 1 ]
then
id=${1}
fi
twcp="lib/twitter4j/twitter4j-stream-4.0.4.jar:lib/twitter4j/twitter4j-core-4.0.4.jar:."

rlwrap java -cp ${twcp}  yukaritwiread.twitter.Main ${id}
