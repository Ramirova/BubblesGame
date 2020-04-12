// Enable Scala.js + Workbench plugin
enablePlugins(ScalaJSPlugin)
scalaJSUseMainModuleInitializer := true
enablePlugins(WorkbenchPlugin)

// Project name + basic definitions
name := "BubblesGame"
version := "1.0.1"
scalaVersion := "2.12.2"

// Dependencies to Scala.js dom
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2"

// Dependencies to Doodle
resolvers += Resolver.bintrayRepo("underscoreio", "training")
libraryDependencies += "underscoreio" %%% "doodle" % "0.8.2"

// Dependencies to Scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.4" % "test"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

// Bintray config
bintrayRepository := "scalanimate"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
bintrayPackageLabels := Seq("scalanimate", "scala", "animation")

// Test envirommnent configuration
jsEnv in Test := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
