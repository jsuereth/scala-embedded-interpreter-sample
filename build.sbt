name := "scala-embedded-interpreter"

organization := "com.jsuereth"

version := "1.0"

scalaVersion := "2.9.2"

libraryDependencies <+= scalaVersion { v =>
  "org.scala-lang" % "scala-compiler" % v
}



scalacOptions in Compile += "-deprecation"
