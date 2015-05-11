import sbt._
import Keys._
import org.scalatra.sbt._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object ReaderBuild extends Build {
  val Organization = "info.aiquestion"
  val Name = "Reader"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.1"
  val ScalatraVersion = "2.3.0"

  lazy val project = Project(
    "reader",
    file("."),
    settings = ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.1.5.v20140505" % "container",
        "org.eclipse.jetty" % "jetty-plus" % "9.1.5.v20140505" % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0",
        //for email
        "me.lessis" %% "courier" % "0.1.3",
        // for http client
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.1" withSources() withJavadoc(),
        // for merge pdfs
        "org.apache.pdfbox" % "pdfbox" % "1.8.2",
        // json support
        "org.scalatra" %% "scalatra-json" % "2.3.0",
        "org.json4s" %% "json4s-jackson" % "3.2.10" withSources() withJavadoc(),
        // for actor
        "org.scala-lang" % "scala-actors" % ScalaVersion,
        //for mongodb
        "org.mongodb" %% "casbah" % "2.7.2",
        "org.json4s" %% "json4s-mongo" % "3.2.10"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) { base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty, /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)), /* add extra bindings here */
            Some("templates")))
      }))
}
