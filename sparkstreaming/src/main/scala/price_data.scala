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

    val confSparkCassandra  = new SparkConf().setAppName("price_data").set("spark.cassandra.connection.host", "52.39.96.29")
val scCas = new SparkContext(confSparkCassandra)
    val ssc = new StreamingContext(scCas, Seconds(10))
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    // Create direct kafka stream with brokers and topics
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    messages.foreachRDD { rdd =>

        val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)

	import sqlContext.implicits._
        val lines = rdd.map(_._2)
	val df = sqlContext.jsonRDD(lines)
	var df2=df.select(df("location"),df("item"),df("time"))
  	val df3=df2.map{case Row(x:String,y:String,z:Long)=>((x,y),(z,z))}	
	val df4=df3.reduceByKey((x,y)=>(math.min(x._1,y._1),math.max(x._2,y._2)))
	val df5=df4.map{case ((a,b),(c,d))=>(a,b,d,c)}
	df5.saveToCassandra("play","tempdata")
	//println(df4.toDF().first)
	df5.toDF().show()
	println(df4.asInstanceOf[AnyRef].getClass.getSimpleName)
	df2.map{case Row(location: String,item: String,time:Long)=>dumbHere(location,item,time)}.saveToCassandra("play","dumbdata")
	//df2.show(); 
	val dfsort=df.groupBy("location","item").count().sort("location","count")
	val dfsortcount=dfsort.select(dfsort("location"),dfsort("item"),dfsort("count").cast("int"))
	//dfsortcount.show(32)
	dfsortcount.map{case Row(location: String,item: String,count: Int)=>countRT(location,item,count)}.saveToCassandra("play","sdata")
	//println(lines.asInstanceOf[AnyRef].getClass.getSimpleName)
	println("--------------------------------------------")
    }

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}


case class minMax(location: String,item: String,min: Long, max:Long)
case class countRT(location: String,item: String,count: Int)
case class dumbHere(location: String,item: String,time:Long)

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
