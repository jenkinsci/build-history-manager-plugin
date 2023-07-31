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
This is a [Jenkins](https://jenkins.io/) plugin that allows the building of simple but powerful rules
to define which builds should be removed from the history and which ones should be preserved.

The rules use conditionals to filter (select) completed builds from the build history and actions to operate on the builds selected by the conditionals.

## Rules

Users can add Build History Manager rules to Jenkins jobs. The rules are composed of three types of objects:

1. Built-in conditions that control the applications of rules to builds,
2. Optional [Condition](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/Condition.java)s that are ANDed together to filter (select) builds,
3. Optional [Action](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/Action.java)s that are applied to builds when all the conditions are met.

### Built-in conditions

The plugin has three built-in conditions that control the flow of operations. Users cannot remove these conditions.
The first one is build-in to the plugin for all rules, and the other two are configurable on a per-rule basis.

The built-in conditions are:

1. A global check for the "Keep this build forever" state, and when it returns true, rules are not applied to the build.
   This currently cannot be controlled nor configured by the user.
2. A per-rule `matchAtMost` counter (`Process this rule at most (times)` in the UI) that limits the number of times a rule can be applied
   (default is `-1`, meaning there is no limit);
3. A per-rule `continueAfterMatch` boolean (`Proceed to the next rule if the conditions of this rule are met` in the UI)
   that causes the plugin to apply the next rule to the same build after the current rule has been applied
   (default is `true`, meaning to continue to apply rules to the build being processed).

### Optional Conditions

Users can add a list of [Condition](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/Condition.java)s to each rule,
in any order as needed.
The conditions are checked in the order they are defined.

The conditions are:

- [Build age range](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/BuildAgeRangeCondition.java)
- [Build cause](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/CauseCondition.java)
- [Build number range](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/BuildNumberRangeCondition.java)
- [Build result](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/BuildResultCondition.java)
- [Match every build](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/MatchEveryBuildCondition.java)
- [Token Macro](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/conditions/TokenMacroCondition.java)

Providing the built-in conditions allow, the condition checks have the following effects:

1. As soon as one of the conditions does not match, the plugin ignores the Actions.
2. When all the conditions of the rule are met, the plugin applies the rule's Actions.
3. When no optional conditions are specified, the plugin automatically applies the rule's Actions.

### Optional Actions

Users can add a list of [Action](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/Action.java)s to each rule,
in any order as needed.
The actions are applied in the order they are defined.

The actions are:

- [Change build description](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/ChangeBuildDescriptionAction.java)
- [Delete log file](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/DeleteLogFileAction.java) 
- [Delete artifacts](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/DeleteArtifactsAction.java)
- [Delete build](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/DeleteBuildAction.java)

If the first action is to delete the build, the other actions may still be applied but have no real effect.

## Operation

The plugin starts by initializing its internal rules counters to zero.

Then the plugin loops through all the completed builds in the build history starting from the most recent,
and processes each build with possibly every rule (depending on the conditions) by looping through the rule list
once for each completed build.

1. For each completed build, the following happens:
    1. If the build is marked as keep forever, the build is ignored and the loop move on to the completed build.
    2. For each rule, the following happens to the build currently operated on:
        1. If the rule match counter equals the `matchAtMost` value,
           stop processing the current rule and move on to the next rule (next iteration of for each rule)
        2. If there are no optional conditions, or if all the optional conditions are matched, the following happens:
            1. The rule counter is incremented by one.
            2. All the actions are performed.
            3. If `continueAfterMatch` is `true`,
               the next rule is applied to the same build, and goes to the next iteration of the for each rule loop
            4. If `continueAfterMatch` is `false`,
               the plugin stops applying rules to this build, and goes to the next iteration of the "for each completed build" loop.
        3. If one or more optional condition are not met,
           go to the next completed build (next iteration of the "for each completed build" loop).

__Notes__:

1. If the `matchAtMost` value is set to zero, the rule is effectively disabled.
   This is useful if the user wants to disable a rule while keeping its other configuration values.
2. Once the `matchAtMost` value is reached, the rule is effectively disabled and is no longer applied.
3. It may not make sense to continue to apply rules after a build is deleted, but the plugin handles this case gracefully.
4. Having no condition is a way to unconditionally apply actions to builds, for example to delete all the builds.
5. Having no action is a way to ignore builds and keep them in the build history.

## Use cases
By using conditions and actions, it becomes straightforward to achieve a number of scenarios, including:
- Delete [unstable](https://javadoc.jenkins.io/hudson/model/Result.html#UNSTABLE)
  or [aborted](https://javadoc.jenkins.io/hudson/model/Result.html#ABORTED)
  builds from the build history if they do not provide any value.
- Keep only the last builds depending on their [result](https://javadoc.jenkins.io/hudson/model/Result.html),
  so the history contains the most recent builds with the specified result(s): aborted, unstable, failure, or success.
- Keep builds only from `master` branch if the project builds all branches including feature branches
- Remove any builds with a [build number](https://javadoc.jenkins-ci.org/hudson/model/Run.html#getNumber--)
  lower than the specified value to easily discard all old builds at once.

## Examples

### Keep 5 most recent builds, delete the rest

The following configuration allows to save the last 5 builds, while deleting the rest of the build history:

![feature overview page](./.README/configuration.png)

### Retain most recent broken build

The following configuration has two rules.
The first rule ensures that the latest build with a `failure` [result](https://javadoc.jenkins-ci.org/hudson/model/Result.html)
is not deleted. The second rule deletes all builds which are not `success`.
In other words, it keeps the most recent broken build and all stable builds.

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
                    BuildResult(matchNotBuilt: true, matchAborted: true, matchFailure: true, matchUnstable: true)
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

### Remove builds based on a parameter

The following configuration has three rules.
The first rule uses the token macro condition to test the value of a parameter.
It removes builds where the string value of `ENABLE_HISTORY` is "false".
The second rule preserves the most recent 24 builds that do not match the first rule.
The third rule deletes the remaining build.
Consequently, these three rules work together to preserve the last 24 builds where `ENABLE_HISTORY` is true.

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

## Wiki
Please refer to the [Wiki](https://github.com/jenkinsci/build-history-manager-plugin/wiki)
for more detailed information.
Additionally, make sure to review the provided guidance on
[avoiding issues](https://github.com/jenkinsci/build-history-manager-plugin/wiki/Building-good-rules)
while creating rules.

It is possible to create complex rules with multiple conditions and actions.
Each [rule](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/Rule.java)
can define multiple conditions and actions.
The plugin entry point is the [BuildHistoryManager](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/BuildHistoryManager.java) class,
which extends from the Jenkins core [BuildDiscarder](https://javadoc.jenkins.io/jenkins/model/BuildDiscarder.html) class.
The [Rule.perform()](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/Rule.java)
method serves as the core function responsible for processing conditions and actions.

## Troubleshooting
The plugin is called by the Jenkins core when the build is completed.
It is not tied to any particular run such as the last completed run, which could potentially be deleted by certain actions.
To assist with troubleshooting and analysis, the plugin logs helpful messages to the [Jenkins logs](https://www.jenkins.io/doc/book/system-administration/viewing-logs/).

## Test & debug
For debugging purposes, you can use the
[ChangeBuildDescriptionAction](./src/main/java/pl/damianszczepanik/jenkins/buildhistorymanager/model/actions/ChangeBuildDescriptionAction.java) action.
This action allows you to update the build description,
making it convenient to test and debug conditions before applying actual deletions as actions.

## Code quality
Once you have developed a new feature or improvement,
it is essential to conduct thorough testing by implementing multiple
[unit](https://en.wikipedia.org/wiki/Unit_testing) or [integration](https://en.wikipedia.org/wiki/Integration_testing) tests.
This ensures the reliability and functionality of the implemented changes. 

![codecov.io](https://codecov.io/gh/jenkinsci/build-history-manager-plugin/branch/master/graphs/tree.svg)

## Release notes
Please refer to the [release notes](https://github.com/jenkinsci/build-history-manager-plugin/releases)
for the changelog and specific details about the changes made in each version.

## Contribution
If you come across an issue, you can contribute by either sending a pull request to resolve it or filing a bug report.
This applies similarly if you come across a missing `Action` or `Condition`.
In addition, it is important to remember the following steps:

- Conduct tests on your local Jenkins instance to ensure the changes work as expected.
- Include new unit tests following the [given -> when -> then](https://pl.wikipedia.org/wiki/Behavior-driven_development) approach to verify the behavior of the changes.
- Remember to perform integration tests to ensure the changes integrate smoothly with the overall system.
- Update the wiki documentation to reflect the changes made.
