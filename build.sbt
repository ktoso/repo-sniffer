name := """akka-projects-github"""

version := "1.0"

scalaVersion := "2.11.1"

resolvers += "google-api-services" at "https://google-api-client-libraries.appspot.com/mavenrepo"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.6" % "test"

// Uncomment to use Akka
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.4"

libraryDependencies += "com.google.apis" % "google-api-services-bigquery" % "v2-rev151-1.18.0-rc"

libraryDependencies += "com.google.oauth-client" % "google-oauth-client" % "1.18.0-rc"

libraryDependencies += "com.google.http-client" % "google-http-client-jackson2" % "1.18.0-rc"

