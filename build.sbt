name := "StructuredStreaming"

version := "0.1"

scalaVersion := "2.11.12"


val sparkVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
)

mainClass in assembly := some("com.demo.streaming.BatchDemo")
assemblyJarName := "StreamingDemo.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
