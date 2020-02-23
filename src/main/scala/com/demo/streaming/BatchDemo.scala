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
    val preparedLogs = rawLogs
      .withColumn("http_reply", col("http_reply")
        .cast(IntegerType)
      )
      .filter("http_reply is not null")

    import spark.implicits._
    val weblogs = preparedLogs.as[WebLog]

    weblogs.sort($"http_reply".asc).show(5,true)

    val topDailyURLs =  weblogs
      .withColumn("DayOfMonth",dayofmonth($"timestamp"))
      .select($"DayOfMonth",$"request")
      .groupBy($"DayOfMonth",$"request")
      .agg(count($"request").alias("Count"))
      .orderBy($"Count".asc)

    //TOP 10
    topDailyURLs.show(10,false)


    //  Extraer GET y dejar .html y .htm
    val urlExtractor = """^GET (.+) HTTP/\d.\d""".r
    val allowedExtensions = Set(".html",".htm", "")

    val contentPageLogs = weblogs.filter {log =>
      log.request match {
        case urlExtractor(url) =>
          val ext = url.takeRight(5).dropWhile(c => c != '.')
          allowedExtensions.contains(ext)
        case _ => false
      }
    }

    val topContentPages =  contentPageLogs
      .withColumn("dayOfMonth", dayofmonth($"timestamp"))
      .select($"request", $"dayOfMonth")
      .groupBy($"dayOfMonth", $"request")
      .agg(count($"request").alias("count"))
      .orderBy(desc("count"))

    println("-----------------   ahora si  ------------------")
    topContentPages.show(10,false)


    println("iniciar server...")




  }
}

