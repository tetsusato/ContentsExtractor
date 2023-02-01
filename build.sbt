val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "ContentsExtractor",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    libraryDependencies += "org.apache.poi" % "poi" % "5.2.3",
    libraryDependencies += "org.apache.poi" % "poi-ooxml" % "5.2.3",
    libraryDependencies += "org.apache.poi" % "poi-scratchpad" % "5.2.3",
    libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.27",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test,
    //libraryDependencies += "org.tinylog" % "tinylog" % "1.3.6",
    libraryDependencies += "org.tinylog" % "tinylog-api" % "2.5.0",
    libraryDependencies += "org.tinylog" % "tinylog-impl" % "2.5.0",
    libraryDependencies += "com.github.pathikrit" % "better-files_2.13" % "3.9.1",
  )
