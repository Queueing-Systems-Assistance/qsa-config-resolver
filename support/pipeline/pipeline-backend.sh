#!/bin/bash

#### Init
cd "${TRAVIS_BUILD_DIR}" || exit

#### Build Backend
printf "Build Backend "
./gradlew clean build || exit
echo "[OK]"