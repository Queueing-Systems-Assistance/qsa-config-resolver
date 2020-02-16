#!/bin/bash

#### Init
cd "${TRAVIS_BUILD_DIR}" || exit

#### Build
printf "Build Config Domain"
./gradlew clean build || exit
echo "[OK]"