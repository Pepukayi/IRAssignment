package com.Raduis

/**
 * A simple scala application that displays the top 10 Hashtags on twiiter page.
 */

import java.io._

import com.danielasfregola.twitter4s.TwitterClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.entities.{HashTag, Tweet}

import scala.io.StdIn._
import scala.concurrent.ExecutionContext.Implicits.global

object MyRouteMain  {

  //insert your OAuth tokens and keys below
  val Accesstoken = ""
  val AccessSecret = ""
  val ConsumerKey = ""
  val ConsumerSecret = ""


  def main(args: Array[String]) {

    println("Okay")

    val consumerToken = ConsumerToken(key = ConsumerKey, secret = ConsumerSecret)
    val accessToken = AccessToken(key = Accesstoken, secret = AccessSecret)
    val client = new TwitterClient(consumerToken, accessToken)

    val user = readLine("Please enter the user name or ID of the twitter page you would like to analyse Hashtags tweets from: ".toUpperCase)

    val tweets = client.getUserTimelineForUser(screen_name = user, count = 200).map { tweets =>
      val topHashtags: Seq[((String, Int), Int)] = getTopHashtags(tweets).zipWithIndex
      val rankings = topHashtags.map { case ((entity, frequency), idx) => s"[${idx + 1}] $entity (found $frequency times)" }
      println(s"${user.toUpperCase}'S TOP HASHTAGS:")
      println(rankings.mkString("\n"))

      val writer = new PrintWriter(new File(user + "s_top_hash_tags.txt"))
      writer.write(rankings.mkString("\n"))
      writer.close()

      val writ = new PrintWriter(new File(user + "Tweets.txt"))
      writ.write(tweets.mkString("\n"))
      writ.close()
    }
  }

  def getTopHashtags(tweets: Seq[Tweet], n: Int = 10): Seq[(String, Int)] =
  {
    val hashtags: Seq[Seq[HashTag]] = tweets.map { tweet =>
      tweet.entities.map(_.hashtags).getOrElse(Seq.empty)
    }
    val hashtagTexts: Seq[String] = hashtags.flatten.map(_.text.toLowerCase)
    val hashtagFrequencies: Map[String, Int] = hashtagTexts.groupBy(identity).mapValues(_.size)
    hashtagFrequencies.toSeq.sortBy { case (entity, frequency) => -frequency }.take(n)
  }
}

