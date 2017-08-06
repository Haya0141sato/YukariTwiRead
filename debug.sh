#!/bin/sh

id=
if [ ${#} -eq 1 ]
then
id=${1}
fi
twcp="lib/twitter4j-stream-4.0.4.jar:lib/twitter4j-core-4.0.4.jar:."

#twuserac="-Dtwitter4j.oauth.accessToken=813711369308553216-GMvyesBiI6ldh245TYXQEjdmmGMfouF -Dtwitter4j.oauth.accessTokenSecret=pl3tW19iXhp5K52hQuqs41mWCgVQ03N551ppAWs7YdjK0"

rlwrap java -cp ${twcp}  yukaritwiread.twitter.Main ${id}
