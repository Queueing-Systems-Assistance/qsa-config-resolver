#!/bin/bash

#### Init
cd "${TRAVIS_BUILD_DIR}" || exit

#### Build
printf "Build Config Assembler"
./gradlew clean build || exit
echo "[OK]"