name := "StructuredStreaming"

version := "0.1"

scalaVersion := "2.11.12"


val sparkVersion = "2.4.0"

libraryDependencies ++= {
  val sparkV = "2.3.4"
  val cassandraV = "2.0.5"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkV,
    "org.apache.spark" %% "spark-streaming" % sparkV,
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkV,
    "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkV,
    "org.apache.spark" %% "spark-sql" % sparkV,
    "org.apache.spark" %% "spark-hive" % sparkV,
    "com.datastax.spark" %% "spark-cassandra-connector" % cassandraV,
    "org.scalatest" %% "scalatest" % "3.0.3" % "test",
    "com.github.nscala-time" %% "nscala-time" % "2.12.0",
    "com.typesafe" % "config" % "1.3.1",
    "com.holdenkarau" %% "spark-testing-base" % "2.2.0_0.7.2",
    "com.datastax.cassandra" % "cassandra-driver-core" % "3.3.0"
  )
}



mainClass in assembly := some("com.demo.streaming.KafkaToSpark")
assemblyJarName := "StructuredStreamingDemo.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
