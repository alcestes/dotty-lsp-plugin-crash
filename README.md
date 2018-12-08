# Crashing the Dotty Language Server 0.11.0-RC1

This project allows to replicate https://github.com/lampepfl/dotty/issues/5579

## Description

The `build.sbt` file defines two projects:

  * `plugin` implements a simple Dotty compiler plugin;

  * `crash` uses the plugin above.

The project dependencies ensure that `plugin` is built and packaged
before `crash`.

Everything works as intended from the command line: `sbt compile`
logs a message when a certain code annotation is found.

## How to replicate the problem

Run `sbt launchIDE`: after a while, you will get the error message

```
The Dotty server crashed 5 times in the last 3 minutes. The server will not be restarted
```

The Output panel of vscode reports more details, available here:
https://gist.github.com/alcestes/391c5b14e0b8a2cfcd13eefcc47fefab

## How to avoid the crash

If `build.sbt` is changed and the compiler plugin is _not_ loaded,
then the Dotty Language Server works perfectly.
