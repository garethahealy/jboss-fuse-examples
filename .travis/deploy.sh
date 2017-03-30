#!/usr/bin/env bash

if [[ "${TRAVIS_BRANCH}" = "master" ]] && [[ "${TRAVIS_PULL_REQUEST}" = "false" ]];
then
  echo "About to deploy..."
  mvn deploy -B -Prelease-ossrh --settings .travis/deploy-settings.xml
fi
