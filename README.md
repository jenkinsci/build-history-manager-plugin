[![Travis Status](https://img.shields.io/travis/damianszczepanik/build-history-manager-plugin/master.svg?label=Travis%20bulid)](https://travis-ci.org/damianszczepanik/build-history-manager-plugin)
[![Appveyor Status](https://ci.appveyor.com/api/projects/status/hdaaavqcrk9gpnuh?branch=master&svg=true&label=Appveyor%20build)](https://ci.appveyor.com/project/damianszczepanik/build-history-manager-plugin)
[![Shippable Status](https://api.shippable.com/projects/5d7ce249bf5b4f00078e2eb4/badge?branch=master&label=Shippable%20build)](https://app.shippable.com/github/damianszczepanik/build-history-manager-plugin/dashboard)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3c7da31c6c194731aee1aafa28dca98e)](https://app.codacy.com/manual/damianszczepanik/build-history-manager-plugin/dashboard)
[![Coverage Status](https://codecov.io/gh/damianszczepanik/build-history-manager-plugin/branch/master/graph/badge.svg?label=Unit%20tests%20coverage)](https://codecov.io/github/damianszczepanik/build-history-manager-plugin)
[![Coverage Status](https://codebeat.co/badges/321eff32-cbd0-4719-9ddd-c90cf1433e14)](https://codebeat.co/projects/github-com-damianszczepanik-build-history-manager-plugin-master)
[![Vulnerabilities](https://snyk.io/test/github/damianszczepanik/build-history-manager-plugin/badge.svg)](https://app.snyk.io/org/damianszczepanik/project/115e1c04-215d-48f9-bb9f-606711f95147)

# Build History Manager Plugin

[Jenkins](https://jenkins.io/) plugin that allows to define which builds should be removed from the history and which preserved.

## Concept
The motivation of creating this plugin is to deliver powerful plugin that allows to define rules:
- which build should be processed (eg by status, branch)
- how the build should be processed (eg. delete)