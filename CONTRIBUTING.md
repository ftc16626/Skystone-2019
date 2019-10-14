# FTC Team 16626

Thank you for contributing to the team's code base! However, before submitting your contribution,
please make sure to read the following guidelines carefully.

**🚨 Please be sure to check out the [branch workflow](#branch-workflow) and [commit style guide](#commit-style-guide), prior to contributing 🚨**

## Setting up a local environment

### 1. **Clone**

Clone the project [on GitHub](https://github.com/ftc16626/Skystone-2019).

```
$ git clone git@github.com:ftc16626/Skystone-2019.git
$ cd Skystone-2019
```

### 2. **Build**

Develop and build the project using Android Studio. This GitHub repo contains the
default code styling format to be used by Intellij in [.idea/codeStyles](.idea/codeStyles).
Intellij should now be able to format your code. Please do so before every commit. It is good
practice to do so regularly (Intellij lacks a format on save option so please do so manually).
If you have any complaints, please see the [footnote at the bottom of this page](#on-code-formatting).

### 3. **Branch**

Checkout the `dev` branch to start contributing. Any major features should have their own branch.
Never make a pull request directly to the `master` branch. All PR's doing so will be rejected.

When contributing to `dev`

```
$ git checkout -b dev
```

When creating a new feature branch

```
$ git checkout -b feature-myname -t upstream/dev
```

Check out the [branch workflow](#branch-workflow) for further details.

## Making Changes

### 4. Commit

Please keep your changes grouped logically within individual commits. This makes it easier to review
changes that are split across multiple commits.

```
$ git add my/changed/files
$ git commit
```

Please adhere to the [commit style guide](#commit-style-guide)

### 5. Rebase

It is preferred to use `git rebase` instead of `git merge` to synchronize your work with the main
repository, once you have committed your changes.

```
$ git fetch upstream
$ git rebase upstream/dev
```

This ensures that your working branch has the latest changes.
If you'd like to read on why rebase is preferred, please refer to
[Atlassian's great tutorial](https://www.atlassian.com/git/tutorials/merging-vs-rebasing)

### 6. Test

Please make sure the changed code builds in Android Studio.

### 7. Push

Ensure that your code is properly formatted (press ctrl+alt+L) in Android studio for formatting.
Ensure that your commits follow the [style guide](#commit-style-guide)
Ensure that you are on the correct branch (follow the [branch workflow](#branch-workflow)
Once done, push your changes to the `dev` branch.

```
$ git push origin feature-myname
```

### 8. Pull request

From within GitHub, open a new pull request.
Be sure to detail what changes have been made.

# Branch Workflow

The project will adhere to the [Git Workflow by Vincent Driessen](https://nvie.com/posts/a-successful-git-branching-model/).
Atlassian provides a great article for a high level read on the Git Workflow
([https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow).

To surmise the branch workflow:

1. Contributors should not (usually) open pull requests to the `master` branch. `master` serves as a snapshot
   of the latest, stable code release to be used at competitions.
2. Development should be done on the `dev` branch.
   - The admin will merge `dev` to `master` when necessary.
3. Major features (new drive-train code, new autonomous code, etc.) should have their own branch (off
   of the `dev` branch). - These branches should be named `feature-myname`. - This is encouraged so that major features can be developed in isolation, without side-effects. - Feature branches will be merged into `dev` when necessary.
4. Any direct pull requests to the `dev` branch should be minor features or bug fixes.
5. Pull requests can be made to the `master` branches in the event of a `hotfix`.
   - Hotfixes are critical errors that must be corrected in production environments (like if the code
     is broken during a competition).
   - Hotfix branches should be named `hotfix-fixname`
   - The `hotfix` branch should be merged into both `dev` and `master`.

# Commit Style Guide

The commit style guide is adapter from [Electron's commit message guidelines](https://electronjs.org/docs/development/pull-requests#commit-message-guidelines).

A commit message should describe what changed and why. It should follow the [semantic commit messages](https://conventionalcommits.org/)
to ensure consistency and a streamlined release process.
Commits **must** have a title and semantic prefix prior to merging.
Examples of commit messages:

- `fix: don't overwrite prevent_default if default wasn't prevented`
- `feat(MainActivity.java): add app.isPackaged() method`
- `refactor(RefactoredFile.kt): fix teertho's mistakes`

A common appending the prefix indicates a specific file change. This should be used if only a single
file was changed.

Common prefixes:

- **fix**: A bug fix
- **feat**: A new feature
- **docs**: Documentation changes
- **build**: Changes that affect the build system (gradle)
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **style**: Changes that do no affect the meaning of the code (linting)

# On Code Formatting

This project will adhere to the code style included within the project (located in
[.idea/codeStyles](.idea/codeStyles)). The code formatting options is based on [Google's Java
Codestyle Guidelines](https://github.com/google/styleguide) and
[Kotlin's default coding conventions](https://kotlinlang.org/docs/reference/coding-conventions.html).
This means that by default, the Java files will use 2 spaces per tab and Kotlin files will use 4
spaces. The purpose of adhering to the Google style guide is to be able to use consistent formatting.
This is important when committing to git so git blame does not log unnecessary changes based on
differing formatting options. You may use whatever tab style you want on your own but please
format your code based on the project guidelines (press `ctrl+alt+L`) before every commit.
