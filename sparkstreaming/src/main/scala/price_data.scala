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

    val brokers = "50.112.40.243:9092,52.25.13.29:9092,50.112.22.187:9092,52.24.80.162:9092" 
    val topics = "price"
    val topicsSet = topics.split(",").toSet

    val confSparkCassandra  = new SparkConf().setAppName("price_data").set("spark.cassandra.connection.host", "52.39.96.29")
val scCas = new SparkContext(confSparkCassandra)
    val ssc = new StreamingContext(scCas, Seconds(5))
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    // Create direct kafka stream with brokers and topics
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
    val timeSpan:Long=10000
    val divi:Long=10
    val intv:Long=timeSpan/divi
    messages.foreachRDD { rdd =>

        val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)

	import sqlContext.implicits._
        val lines = rdd.map(_._2)
	val df = sqlContext.jsonRDD(lines)


	// functional programming for calculating accumulative distribution
	// many variable expression is declared so that easier to be understood
	var df2=df.select(df("location"),df("item"),df("time")).sort("location","item","time")
  	val df3=df2.map{case Row(x:String,y:String,z:Long)=>((x,y),(z,z,z))}	
	val df4=df3.reduceByKey((x,y)=>(math.min(x._1,y._1),math.max(x._2,y._2),x._3))
	val df6=df4.map{case ((a,b),(c,d,e))=>((a,b),(d,c))}
  	val df7=df2.map{case Row(x:String,y:String,z:Long)=>((x,y),z)}	
	val jdf=df7.join(df6)
	val jdf2=jdf.sortBy(x=>(x._1,x._2._1))
	val jdf3=jdf2
	.map(x=>(x._1,(x._2._1,x._2._2._1,x._2._2._2,(x._2._1-x._2._2._2)*divi/(x._2._2._1-x._2._2._2))))
	val jdf4=jdf3.map(x=>(x._1,List(x._2._4)))
	val jdf5=jdf4.reduceByKey(_++_)
	val jdf6=jdf5.map(x=>(x._1,x._2.groupBy(identity).toList.sortBy(_._1)))
	val jdf7=jdf6.map(x=>(x._1,x._2.map(k=>k._2).map(p=>p.size))) 
	// jdf8 is accumulative distribution groupped by key (location,item)
	val jdf8=jdf7.map(x=>(x._1,x._2.foldLeft(List[Int](0))((x,y)=>x:+(y+x.last))))

	//temporary solution only
	val jdf9=jdf8.map(x=>(x._1._1,x._1._2,x._2(0),x._2(1),x._2(2),x._2(3),x._2(4),x._2(5),x._2(6),x._2(7),x._2(8),x._2(9),x._2(10)))
	jdf9.saveToCassandra("play","disti")
	
	//dumping data is not performed on demo to save space in database
	//df2.map{case Row(location: String,item: String,time:Long)=>dumbHere(location,item,time)}.saveToCassandra("play","dumbdata")

	// counting data
	val dfsort=df.groupBy("location","item").count().sort("location","count")
	val dfsortcount=dfsort.select(dfsort("location"),dfsort("item"),dfsort("count").cast("int"))
	dfsortcount.map{case Row(location: String,item: String,count: Int)=>countRT(location,item,count)}.saveToCassandra("play","sdata")
	//println("--------------------------------------------")
    }

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}


//temporary solution
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
