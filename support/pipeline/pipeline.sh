#!/bin/bash

#### Init
cd "${TRAVIS_BUILD_DIR}" || exit
git config --global user.email "builds@travis-ci.com" || exit
git config --global user.name "Travis CI" || exit

# Update branch name
BRANCH_NAME=${TRAVIS_BRANCH}
GITHUB_ENDPOINT="https://${GITHUB_OWN_TOKEN}@github.com/${TRAVIS_REPO_SLUG}"
RELEASE_VERSION=${PROJECT_MAJOR_VERSION}.${PROJECT_MINOR_VERSION}.${TRAVIS_BUILD_NUMBER}

# Print variables
echo "BRANCH_NAME=${BRANCH_NAME}"
echo "GITHUB_ENDPOINT=${GITHUB_ENDPOINT}"
echo "RELEASE_VERSION=${RELEASE_VERSION}"

# Build
echo "Build QSA Config Domain"
./gradlew clean build -Prelease.version="${RELEASE_VERSION}" || exit

if [[ "${BRANCH_NAME}" == "master" ]]; then
  # Publish
  echo "Publish package to GitHub Packages"
  ./gradlew publish -Prelease.version="${RELEASE_VERSION}" || exit
  # Create git tag & push to GitHub
  git tag -a -f "${RELEASE_VERSION}" -m "${RELEASE_VERSION}" || exit
  git push -f "${GITHUB_ENDPOINT}" refs/tags/"${RELEASE_VERSION}" || exit
fi
