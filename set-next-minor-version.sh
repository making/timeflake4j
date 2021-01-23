#!/bin/bash

CURRENT_VERSION=$(grep '<version>' pom.xml | head -n 1 | sed -e 's|<version>||g' -e 's|</version>||g' -e 's| ||g' |  tr -d '\t' | sed 's/-SNAPSHOT$//')
MAJOR_VERSION=$(echo ${CURRENT_VERSION} | awk -F '.' '{print $1}' )
MINOR_VERSION=$(echo ${CURRENT_VERSION} | awk -F '.' '{print $2}' )
PATCH_VERSION=$(echo ${CURRENT_VERSION} | awk -F '.' '{print $3}' )
NEXT_MINOR_VERSION="${MAJOR_VERSION}.$((${MINOR_VERSION} + 1)).0-SNAPSHOT"

./mvnw versions:set -DnewVersion="${NEXT_MINOR_VERSION}" -DallowSnapshots -DgenerateBackupPoms=false
git add pom.xml
git commit -m "Bump to ${NEXT_MINOR_VERSION}"