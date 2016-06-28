#TopDemandNow
TopDemandNow is streaming analytic system to analyze behaviour of purchase at chain stores in realtime.

##Motivation
The fact that chain stores or convenience stores has concentrated density at particular location. The idea is providing service to allow them to upload their data realtime and perform analysis depending on business need. 
<p align="center">
  <img src="/images/map.png" width="450"/>
</p>

As can be seen in this picture one of the analysis is knowing what is the most purchased item at given location and at given time. One of the application is in the field of digital advertising, by knowing that at this time there is surge demand in milk then that is a good time to advertise milk as depicted in picture.

## Visual and Demonstration
The bar chart indicate different location and the color indicate different items. The cumulative distribution shows how single item at single location process through its final count. Since the data is updated real time, the chart will change over time depending on situtaion. The demonstration can be accessed from this web http://thebookface.top
<p align="center">
  <img src="/images/barchart.png" width="450"/>
</p>
<p align="center">
  <img src="/images/cumdis.png" width="450"/>
</p>
##Data Pipeline
Data from producer is ingested using Kafka. Spark streaming with scala is used to process data in realtime. Cassandra is used as database choices. Finally flask and d3.js is used as front-end to display data
<p align="center">
  <img src="/images/pipeline.png" width="450"/>
</p>

#Spark
Spark is framework to calculate data in distributed manner. In order to programmatically modify or process stream, functional programming is needed as it is suitable for parallel computation. I used Scala to modify RDD in spark. The key point on functional programming is mapping or applying function to data to modify them. Below are snippet of code to calculate cumulative distribution.   

```scala
// functional programming for calculating accumulative distribution
// many variable expression is declared so that easier to be understood
var df2=df.select(df("location"),df("item"),df("time")).sort("location","item","time")
val df3=df2.map{case Row(x:String,y:String,z:Long)=>((x,y),(z,z,z))}
val df4=df3.reduceByKey((x,y)=>(math.min(x._1,miny._1),math.max(x._2,y._2),x._3))
val df6=df4.map{case ((a,b),(c,d,e))=case>((a,b),(d,c))}
val df7=df2.map{case Row(x:String,y:String,z:Long)=>Long((x,y),z)}
val jdf=df7.join(df6)
val jdf2=jdf.sortBy(x=>(x._1,x._2._31))
val jdf3=jdf2
.map(x=>(x._1,(x._2._1,x._2._2._1,x._2._2._2,(x._2._1_1-x._2._2._2)*divi/(x._2._2._1-x._2._2._2))))
val jdf4=jdf3.map(x=>(x._1,List(x._2._4)))
val jdf5=jdf4.reduceByKey(_++_)
val jdf6=jdf5.mapping(x=>(x._1,x._2.groupBy(identity).toList.sortBy(_._1)))
val jdf7=jdf6.mappingap(x=>(x._1,x._2.map(k=>k._2).map(p=>p.size))) 
// jdf8 is accumulative distribution groupped by key (location,item)
val jdf8=jdf7.map(x=>(x._1,x._2.foldLeft(List[Int](0))((x,y)=>x:+(y+x.last))))
```

The simplified process is depicted here where each process contain mapping and eventually reducing data by key and no loop is involved.
<p align="center">
  <img src="/images/funcprog2.png" width="450"/>
</p>

