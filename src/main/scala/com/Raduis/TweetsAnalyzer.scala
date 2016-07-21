package com.Raduis

/**
  * Created by Pepukayi Chitondo on 14/7/2016.
  */

import java.io.{File, PrintWriter}

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.StdIn._

import org.apache.spark.sql._

//Text analytics example starts here
import scala.collection.mutable
import org.apache.spark.mllib.clustering.{LDA, DistributedLDAModel,LocalLDAModel}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD

object TweetsAnalyzer
{

  def main(args: Array[String]): Unit = {
    //SparkTest()
    displayResults


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

    val textFile = sc.textFile(user+"Tweets.txt")      //creation of a simple RDD
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1)).cache()         //storing results in cache

    counts.reduceByKey(_+_).collect.foreach(println)
    //counts.foreach(println)

    val word = readLine("Please enter a string value you would like to analyse its occurances on the "+user+" Twitter Page: ".toUpperCase)
    val linesWith = textFile.filter(line => line.contains(word)).count()
    printf("There are %d lines where Lewis Hamiloton was mentioned in the file", linesWith)

    val toFile = "There are "+linesWith+" lines where "+word+" was mentioned on the "+user+" Twitter Page."

    textFile.saveAsTextFile("SparkAnalytics2.txt")
    counts.saveAsTextFile("Spark.txt")


    val writer = new PrintWriter(new File("SparkAnalytics.txt"))
    writer.append(toFile)
    writer.close()
  }

  def displayResults: Unit = {
    val conf = new SparkConf() //Defining Spark Configuration object
      .setMaster("local[*]") //Setting Spark Master as Local
      .setAppName("Test Spark") //Spark Application name, optional
      .set("spark.executor.memory", "2g")

    val sc = new SparkContext(conf) //This is where things actuall start

    // Load documents from text files, 1 document per file
    val corpus: RDD[String] = sc.wholeTextFiles("TwitterExtractions/*").map(_._2)

    // Split each document into a sequence of terms (words)
    val tokenized: RDD[Seq[String]] =
    corpus.map(_.toLowerCase.split("\\s")).map(_.filter(_.length > 3).filter(_.forall(java.lang.Character.isLetter)))

    // Choose the vocabulary.
    //   termCounts: Sorted list of (term, termCount) pairs
    println("TOPIC:")

    val termCounts: Array[(String, Long)] = tokenized.flatMap(_.map(_-> 1L)).reduceByKey(_+_).collect().sortBy(-_._2)
    println("TOPIC2:")

    //val termCounts: Array[(String, Long)] =
    //tokenized.flatMap(_.map(_ -> 1L)).reduceByKey(_ + _).collect().sortBy(-_._2)
    println("TOPIC2:")
    //   vocabArray: Chosen vocab (removing common terms)
    val numStopwords = 20
    val vocabArray: Array[String] =
      termCounts.takeRight(termCounts.size - numStopwords).map(_._1)
    //   vocab: Map term -> term index
    val vocab: Map[String, Int] = vocabArray.zipWithIndex.toMap

    // Convert documents into term count vectors
    val documents: RDD[(Long, Vector)] =
    tokenized.zipWithIndex.map { case (tokens, id) =>
      val counts = new mutable.HashMap[Int, Double]()
      tokens.foreach { term =>
        if (vocab.contains(term)) {
          val idx = vocab(term)
          counts(idx) = counts.getOrElse(idx, 0.0) + 1.0
        }
      }
      (id, Vectors.sparse(vocab.size, counts.toSeq))
    }

    // Set LDA parameters
    val numTopics = 10
    val lda = new LDA().setK(numTopics).setMaxIterations(10)

    val ldaModel = lda.run(documents)
    val distLDAModel = ldaModel.asInstanceOf[DistributedLDAModel]
    val avgLogLikelihood = distLDAModel.logLikelihood / documents.count()

    // Print topics, showing top-weighted 10 terms for each topic.
    val topicIndices = distLDAModel.describeTopics(maxTermsPerTopic = 10)
    topicIndices.foreach { case (terms, termWeights) =>
      println("TOPIC:")
      terms.zip(termWeights).foreach { case (term, weight) =>
        println(s"${vocabArray(term.toInt)}\t$weight")
      }
      println()


    }
  }

}
