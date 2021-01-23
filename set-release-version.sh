#!/bin/bash

CURRENT_VERSION=$(grep '<version>' pom.xml | head -n 1 | sed -e 's|<version>||g' -e 's|</version>||g' -e 's| ||g' |  tr -d '\t' | sed 's/-SNAPSHOT$//')

./mvnw versions:set -DnewVersion=${CURRENT_VERSION} -DgenerateBackupPoms=false
git add pom.xml
git commit -m "Bump to ${CURRENT_VERSION}"