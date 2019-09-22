[![Travis Status](https://img.shields.io/travis/damianszczepanik/build-history-manager-plugin/master.svg?label=Travis)](https://travis-ci.org/damianszczepanik/build-history-manager-plugin)
[![Appveyor Status](https://img.shields.io/appveyor/ci/damianszczepanik/build-history-manager-plugin/master?label=AppVeyor)](https://ci.appveyor.com/project/damianszczepanik/build-history-manager-plugin)
[![Shippable Status](https://img.shields.io/shippable/5d7ce249bf5b4f00078e2eb4/master?label=Shippable)](https://app.shippable.com/github/damianszczepanik/build-history-manager-plugin/dashboard)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3c7da31c6c194731aee1aafa28dca98e)](https://app.codacy.com/manual/damianszczepanik/build-history-manager-plugin/dashboard)
[![Coverage Status](https://codecov.io/gh/damianszczepanik/build-history-manager-plugin/branch/master/graph/badge.svg?label=Unit%20tests%20coverage)](https://codecov.io/github/damianszczepanik/build-history-manager-plugin)
[![Codebean Status](https://codebeat.co/badges/321eff32-cbd0-4719-9ddd-c90cf1433e14)](https://codebeat.co/projects/github-com-damianszczepanik-build-history-manager-plugin-master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=damianszczepanik_build-history-manager-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=damianszczepanik_build-history-manager-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=damianszczepanik_build-history-manager-plugin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=damianszczepanik_build-history-manager-plugin)

[![Vulnerabilities](https://snyk.io/test/github/damianszczepanik/build-history-manager-plugin/badge.svg)](https://app.snyk.io/org/damianszczepanik/project/115e1c04-215d-48f9-bb9f-606711f95147)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=damianszczepanik_build-history-manager-plugin&metric=security_rating)](https://sonarcloud.io/dashboard?id=damianszczepanik_build-history-manager-plugin)

# Build History Manager Plugin
[Jenkins](https://jenkins.io/) plugin that allows to build complex rules to define which builds should be removed from the history and which preserved.

## Usage
The motivation of creating this plugin is to deliver powerful tool that allows to define rules that are built from two types of objects:

### Conditions
[Condition](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/Condition.java) filters out builds which should be performed by [actions](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/Action.java). So plugin can filter builds by:
- build [result](https://javadoc.jenkins-ci.org/hudson/model/Result.html)
- variables for which you can use [Token Macro](https://wiki.jenkins.io/display/JENKINS/Token+Macro+Plugin) plugin

### Actions
[Actions](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/Action.java) defines how the build filtered by above condition) should be modified. Plugin can:
- mark the build to be [kept forever]([https://javadoc.jenkins.io/hudson/model/Run.html#keepLog--)
- [delete](https://javadoc.jenkins.io/hudson/model/Run.html#delete--) the build
- [delete](https://javadoc.jenkins.io/hudson/model/Run.html#deleteArtifacts--) artifacts

There is possibility to build complex rules. Each [rule](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/Rule.java) can define more than single condition and action.
Plugin is based on [BuildDiscarder](https://javadoc.jenkins.io/jenkins/model/BuildDiscarder.html) class.

### Use cases
Using conditions and actions there is easy to realize following scenarios:
- Delete builds which are [unstable](https://javadoc.jenkins.io/hudson/model/Result.html#UNSTABLE) or [aborted](https://javadoc.jenkins.io/hudson/model/Result.html#ABORTED) if they are not valuable.
- Keep only last build per [result](https://javadoc.jenkins.io/hudson/model/Result.html). So the history contain at most four builds, for aborted, unstable, failure and success.
- Keep builds only from `master` branch if the project builds all branches including feature branches


## Code quality
Once you developed your new feature or improvement you should test it by providing several unit or integration tests.

![codecov.io](https://codecov.io/gh/damianszczepanik/build-history-manager-plugin/branch/master/graphs/tree.svg)

## Contribution
Interested in contributing to deliver new condition or action?  Great! Start [here](https://github.com/damianszczepanik/build-history-manager-plugin).
