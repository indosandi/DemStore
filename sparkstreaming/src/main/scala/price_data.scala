import kafka.serializer.StringDecoder

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.log4j.{Level, Logger}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import com.datastax.spark.connector._
import com.datastax.spark.connector.streaming._
import com.datastax.driver.core.utils._

object JSONDataStreaming {
  def main(args: Array[String]) {

    val brokers = "52.33.31.202:9092"
    val topics = "price_data_part4"
    val topicsSet = topics.split(",").toSet

    // Create context with 2 second batch interval
    //val sparkConf = new SparkConf().setAppName("price_data")
    val confSparkCassandra  = new SparkConf().setAppName("price_data").set("spark.cassandra.connection.host", "52.39.96.29")
val scCas = new SparkContext(confSparkCassandra)
    val ssc = new StreamingContext(scCas, Seconds(9))
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    // Create direct kafka stream with brokers and topics
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    // Get the lines and show results
    messages.foreachRDD { rdd =>

        val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
        import sqlContext.implicits._

	//println(df.asInstanceOf[AnyRef].getClass.getSimpleName)
        val lines = rdd.map(_._2)
	val df = sqlContext.jsonRDD(lines)
	val dfsort=df.groupBy("location","item").count().sort("location","count")//.show(32)
	val dfsortcount=dfsort.select(dfsort("location"),dfsort("item"),dfsort("count").cast("int"))//.show(32)
	dfsortcount.show(32)
	//dfsortcount.printSchema()
	//dfsort.map{case Row(location: String, item: String)=>countRT(location,item)}.saveToCassandra("play","sloc2")
	dfsortcount.map{case Row(location: String,item: String,count: Int)=>countRT(location,item,count)}.saveToCassandra("play","sdata")
	//df.registerTempTable("msgs")
	//val test1= sqlContext.sql("SELECT location FROM msgs")
	//test1.show()
	//val test2=test1.map{case Row(location: String)=>loc(location)}.saveToCassandra("play","sloc")
	//println(test2.asInstanceOf[AnyRef].getClass.getSimpleName)
	//val dfcc = sqlContext.read.format("org.apache.spark.sql.cassandra").options(Map( "table" -> "sdata", "keyspace" -> "play" )).load()
	//dfcc.show()
	//dfsort.show(32)
//	dfsort.write.format("org.apache.spark.sql.cassandra").options(Map("table"->"sdata","keyspace"->"play")).save()
//dfsort.saveToCassandra("play","sdata")
	//println(lines.asInstanceOf[AnyRef].getClass.getSimpleName)
	//println(df.asInstanceOf[AnyRef].getClass.getSimpleName)
	/**val northDF=df.filter(df("location")==="North")
	val southDF=df.filter(df("location")==="South")
	val eastDF=df.filter(df("location")==="East")
	val westDF=df.filter(df("location")==="West")
	**/
	//df.show()
	/**
	println("NORTH BAY")
	northDF.groupBy("item").count().sort("count").show()
	println("SOUTH BAY")
	southDF.groupBy("item").count().sort("count").show()
	println("EAST BAY")
	eastDF.groupBy("item").count().sort("count").show()
	println("WEST BAY")
	westDF.groupBy("item").count().sort("count").show()
	**/	
//	df.show()
        /**val ticksDF = lines.map( x => {
                                  val tokens = x.split(";")
                                  Tick(tokens(0), tokens(2).toDouble, tokens(3).toInt)}).toDF()
        val ticks_per_source_DF = ticksDF.groupBy("source")
                                .agg("price" -> "avg", "volume" -> "sum")
                                .orderBy("source")

	**/
	//ticks_per_source_DF.show()
        /**val oneDF=ticksDF.filter(ticksDF("source")===1)
        val twoDF=ticksDF.filter(ticksDF("source")===2)
        val threeDF=ticksDF.filter(ticksDF("source")===3)
        val fourDF=ticksDF.filter(ticksDF("source")===4)

        oneDF.groupBy("source").count().sort("count").show()
        twoDF.groupBy("source").count().sort("count").show()
        threeDF.groupBy("source").count().sort("count").show()
        fourDF.groupBy("source").count().sort("count").show()
	ticksDF.groupBy("source").count().sort("count").show(2)
	**/
	//println(ticks_per_source_DF.asInstanceOf[AnyRef].getClass.getSimpleName)
	println("--------------------------------------------")
    }

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}


case class countRT(location: String,item: String,count: Int)

/** Lazily instantiated singleton instance of SQLContext */
object SQLContextSingleton {

  @transient  private var instance: SQLContext = _

  def getInstance(sparkContext: SparkContext): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
    }
    instance
  }
}
