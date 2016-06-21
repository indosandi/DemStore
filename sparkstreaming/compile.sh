sbt assembly
sbt package
spark-submit --class JSONDataStreaming --master spark://ip-172-31-1-71:7077 --jars target/scala-2.10/price_data-assembly-1.0.jar target/scala-2.10/price_data_2.10-1.0.jar
