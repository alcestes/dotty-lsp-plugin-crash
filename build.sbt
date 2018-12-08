val dottyVersion = "0.11.0-RC1"

lazy val plugin = project.in(file("plugin"))
  .settings(
    name := "crash-plugin",
    version := "0.0.1",
    scalaVersion := dottyVersion,

    libraryDependencies ++= Seq(
      "ch.epfl.lamp" %% "dotty-compiler" % scalaVersion.value % "provided"
    ),
  )

// Options to load plugin
val pluginOpts = (Keys.`package` in (plugin, Compile)) map { (jar: File) =>
    val addPlugin = "-Xplugin:" + jar.getAbsolutePath
    val addPluginLogging = "-Ylog:crashPlugin+"
    // Taken from https://github.com/retronym/boxer
    // adding the plugin timestamp to compiler options triggers recompilation
    // after editing the plugin. (Otherwise, a 'clean' is needed.)
    val dummy = "-P:crashPlugin:timestamp=" + jar.lastModified
    Seq(addPlugin, addPluginLogging, dummy)
  }

lazy val crash = project.in(file("crash"))
  .dependsOn(plugin)
  .settings(
    name := "crash",
    version := "0.0.1",
    scalaVersion := dottyVersion,
    scalacOptions ++= pluginOpts.value, // Comment to disable the plugin
  )
