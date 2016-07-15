Twitter Client To Analyse Top Hashtags of a Twitter Page
===================================


SetUp:

Register an application at http://apps.twitter.com

Replace the static variables in the companion object with consumer key,
consumer secret, access key and access secret generated when registering the app.

To build this project use:

Scala version 2.11.*
Java 8
Apache Spark 1.6.2
Load dependancies as desribed in the pom.xml

To run this project

There are two mains MyRouteMain and TweetsAnalyzer

For MyRouterMain:
You will be prompted to enter a VALID Twitter page id or name. You then press enter and the
top 10 Hashtags mentioned on that page will be outputed to the console as well as to a file
in your project directory with the name of the page in concern.

For TweetsAnalyzer:
This program is dependent on the results of the MyRouterMain which would have been stored in the file created.
1. Your will be prompted to enter a the Twitter page name you analysed TopHashtags for.
2. You will then be asked to enter a string value you want to search for on the Twitter page in concern.
Some results will be generated and saved in the SparkAnalytics.txt file.



