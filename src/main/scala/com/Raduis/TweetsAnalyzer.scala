package com.Raduis

/**
  * Created by Pepukayi Chitondo on 14/7/2016.
  */
/*import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await

class TweetsAnalyzer(consumerToken: ConsumerToken, accessToken: AccessToken, user: String)
{
  val client = new TwitterClient(consumerToken, accessToken)

  def SparkTest(): Unit =
  {
    val tweets = client.getUserTimelineForUser(screen_name = user, count = 200)

    textFile = sc.textFile("README.md")
    textFile: spark.RDD[String] = spark.MappedRDD@2ee9b6e3

    linesWithLewisHamilton = textFile.filter(line => line.contains("Lewis Hamilton"))
    linesWithLewisHamilton: spark.RDD[String] = spark.FilteredRDD@7dd4af09
    textFile.filter(line => line.contains("Lewis Hamilton")).count()

  }

}*
