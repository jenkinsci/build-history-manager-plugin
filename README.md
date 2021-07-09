[![Build Status](https://ci.jenkins.io/job/Plugins/job/build-history-manager-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/build-history-manager-plugin/job/master/)
[![Appveyor status](https://ci.appveyor.com/api/projects/status/cjto87m99168m6ea/branch/master?svg=true)](https://ci.appveyor.com/project/damianszczepanik/build-history-manager-plugin/branch/master)

[![Coverage Status](https://codecov.io/gh/jenkinsci/build-history-manager-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/jenkinsci/build-history-manager-plugin)
[![Codebeat badge](https://codebeat.co/badges/1b4fcf87-3eb3-4b57-a0fa-ad7258fda8ac)](https://codebeat.co/projects/github-com-jenkinsci-build-history-manager-plugin-master)
[![Sonarqube Status](https://sonarcloud.io/api/project_badges/measure?project=damianszczepanik_build-history-manager-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=damianszczepanik_build-history-manager-plugin)
[![Vulnerabilities](https://snyk.io/test/github/jenkinsci/build-history-manager-plugin/badge.svg)](https://app.snyk.io/org/damianszczepanik/project/aab2b0cc-41d6-41e7-a909-fbc9d09dc98d)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3370/badge)](https://bestpractices.coreinfrastructure.org/en/projects/3370)

[![Installs](https://img.shields.io/jenkins/plugin/i/build-history-manager.svg)](https://plugins.jenkins.io/build-history-manager)
[![Version](https://img.shields.io/jenkins/plugin/v/build-history-manager)](https://github.com/jenkinsci/build-history-manager-plugin/releases)

# Build History Manager Plugin
[Jenkins](https://jenkins.io/) plugin that allows to build complex rules to define which builds should be removed from the history and which preserved.

## Usage
The motivation of creating this plugin is to deliver powerful tool that allows to define rules that are built from two types of objects:

### Examples

#### Retain most recent broken build

Following configuration has two rules. First one makes sure that the newest build with `failure` status is not deleted.
Second deletes all builds which are not `success`. In other words it keeps the most recent broken build and all stables.
```groovy
pipeline {
    agent any

    options {
        buildDiscarder(BuildHistoryManager([
            [
                conditions: [
                    BuildResult(matchFailure: true)
                ],
                matchAtMost: 1,
                continueAfterMatch: false
            ],
            [
                conditions: [
                    BuildResult(matchAborted: true, matchFailure: true, matchUnstable: true)
                ],
                actions: [DeleteBuild()]
            ]
        ]))
    }

    stages {
        stage('Demo') {
            steps {
                echo "Hello!"
            }
        }
    }
}
```

#### Remove builds based on a parameter

Following configuration has three rules. The first uses the token macro condition to test
the value of a parameter. It removes jobs where the string value of `ENABLE_HISTORY` is "false". The second rule will preserve the last 24 builds that do not match the first rule. The third rule deletes the remainder of jobs. Thus the three rules work together to preserve the last 24 builds where `ENABLE_HISTORY` is true.

```groovy
pipeline {
    agent any

    options {
        buildDiscarder(BuildHistoryManager([
            [
                conditions: [
                    TokenMacro(template: '"${ENABLE_HISTORY}"', value: '"false"')
                ],
                actions: [DeleteBuild()],
                continueAfterMatch: false
            ],
            [
                matchAtMost: 24,
                continueAfterMatch: false
            ],
            [
                actions: [DeleteBuild()]
            ]
        ]))
    }

    parameters {
        booleanParam(
          name: 'ENABLE_HISTORY',
          defaultValue: true,
          description: 'Check to preserve build.'
        )
    }

    stages {
        stage('Demo') {
            steps {
                echo "Hello!"
            }
        }
    }
}
```


### Conditions
Following simple configuration allows to save last 5 builds, rest will be deleted:
[Condition](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/Condition.java) filters out builds which should be performed by [actions](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/Action.java). So plugin can filter builds by:
- build [result](https://javadoc.jenkins-ci.org/hudson/model/Result.html)
- variables for which you can use [Token Macro](https://plugins.jenkins.io/token-macro) plugin

### Actions
[Actions](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/Action.java) defines how the build filtered by above condition) should be modified. Plugin can:
- mark the build to be [kept forever]([https://javadoc.jenkins.io/hudson/model/Run.html#keepLog--)
- [delete](https://javadoc.jenkins.io/hudson/model/Run.html#delete--) the build
- [delete](https://javadoc.jenkins.io/hudson/model/Run.html#deleteArtifacts--) artifacts

### Wiki
Read [Wiki](https://github.com/jenkinsci/build-history-manager-plugin/wiki) for more details.
Check also information how to [avoid problems](https://github.com/jenkinsci/build-history-manager-plugin/wiki/Building-good-rules) when creating rules.

There is possibility to build complex rules. Each [rule](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/Rule.java) can define more than single condition and action.
Plugin starts as [BuildDiscarder](https://javadoc.jenkins.io/jenkins/model/BuildDiscarder.html) class. Core method that is responsible for processing conditions and actions are stored in [Rule.perform()](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/Rule.java) method.

### Troubleshooting
Plugin performs when the builds end. It is not connected to any particular run (like last completed job) which might be deleted by some actions. It logs helpful messages to [Jenkins logs](https://www.jenkins.io/doc/book/system-administration/viewing-logs/).

### Configuration
![feature overview page](./.README/configuration.png)

### Use cases
Using conditions and actions there is easy to realize following scenarios:
- Delete builds which are [unstable](https://javadoc.jenkins.io/hudson/model/Result.html#UNSTABLE) or [aborted](https://javadoc.jenkins.io/hudson/model/Result.html#ABORTED) if they are not valuable from the history/audit point of view.
- Keep only last build per [result](https://javadoc.jenkins.io/hudson/model/Result.html). So the history contain the most recent builds for result aborted, unstable, failure and success.
- Keep builds only from `master` branch if the project builds all branches including feature branches
- Remove builds which have [build number](https://javadoc.jenkins-ci.org/hudson/model/Run.html#getNumber--) lower than given value to easily drop all old builds at once.

## Code quality
Once you developed your new feature or improvement you should test it by providing several [unit](https://en.wikipedia.org/wiki/Unit_testing) or [integration](https://en.wikipedia.org/wiki/Integration_testing) tests.

![codecov.io](https://codecov.io/gh/jenkinsci/build-history-manager-plugin/branch/master/graphs/tree.svg)

## Release notes
Check [release notes](https://github.com/jenkinsci/build-history-manager-plugin/releases) for changelog details.

## Contribution
If you find the issue you can send pull request to fix it or file the bug.
The same about missing `Action` or `Condition`
Remember about:
- doing tests on your local Jenkins instance
- adding new unit tests according to [given -> when -> then](https://pl.wikipedia.org/wiki/Behavior-driven_development) approach.
- remember about integration tests and wiki update
