package com.demo.streaming

import org.apache.spark.sql._
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import java.sql.Timestamp
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType

object BatchDemo {

  case class WebLog(host:String,
                    timestamp: Timestamp,
                    request: String,
                    http_reply:Int,
                    bytes: Long
                   )


  def main(args: Array[String]): Unit = {

    println(" Seguimos!! ")

    Logger.getLogger("akka").setLevel(Level.OFF)
    Logger.getLogger("org").setLevel(Level.OFF)


    val spark = SparkSession.builder
      .master("local[*]")
      .appName("SparkStreamingDemo")
      .getOrCreate;


    // This is the location of the unpackaged files. Update accordingly
    val logsDirectory = "/home/logan/Datasets/nasa_dataset_july_1995"
    val rawLogs = spark.read.json(logsDirectory)



    // we need to narrow the `Interger` type because
    // the JSON representation is interpreted as `BigInteger`
    val preparedLogs = rawLogs.withColumn("http_reply", col("http_reply").cast(IntegerType))

    import spark.implicits._
    val weblogs = preparedLogs.as[WebLog]

    weblogs.count()


  }
}

