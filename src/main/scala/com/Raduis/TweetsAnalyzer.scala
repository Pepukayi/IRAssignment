package com.Raduis

/**
  * Created by Pepukayi Chitondo on 14/7/2016.
  */

import java.io.{File, PrintWriter}

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.StdIn._


object TweetsAnalyzer
{

  def main(args: Array[String]): Unit = {
    SparkTest()

  }

  def SparkTest(): Unit =
  {
    val conf = new SparkConf()      //Defining Spark Configuration object
      .setMaster("local[*]")        //Setting Spark Master as Local
      .setAppName("Test Spark")     //Spark Application name, optional
      .set("spark.executor.memory", "2g")

    val sc = new SparkContext(conf) //This is where things actuall start

    /*val lines = sc.parallelize(Seq("This is first line", "This is second line", "This is third line"))

    val count = lines.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_+_)
    count.foreach(println)
    */
    val user = readLine("Please enter the user name or ID of the twitter page you would like to analyse: ".toUpperCase)

    val textFile = sc.textFile(user+"Tweets.txt")
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1))
    counts.foreach(println)

    val word = readLine("Please enter a string value you would like to analyse its occurances on the "+user+" Twitter Page: ".toUpperCase)
    val linesWith = textFile.filter(line => line.contains(word)).count()
    printf("There are %d lines where Lewis Hamiloton was mentioned in the file", linesWith)

    val toFile = "There are "+linesWith+" lines where "+word+" was mentioned on the "+user+" Twitter Page."

    val writer = new PrintWriter(new File("SparkAnalytics.txt"))
    writer.append(toFile)
    writer.close()
  }

}
