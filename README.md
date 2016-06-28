#TopDemandNow
TopDemandNow is streaming analytic system to analyze behaviour of purchase at chain stores in realtime.

##Motivation
The fact that chain stores or convinience stores has concentrated density at particular location. The idea is providing service to allow them to upload their data realtime and perform analysis dependending on bussines need. 

As can be seen in this picture one of the analysis is knowing what is the most purchased item at given location and at given time. One of the application is in the field of digital advertising, by knowing that at this time there is surge demand in milk then that is a good time to advertise milk as depicted in picture.

## Visual and Demonstration
The bar chart indicate different location and the color indicate different items. The cumulative distributio shows how single item at single location process through its final count. Since the data is updated real time, the chart will change over time depending on situtaion. The demonstration can be accessed from this web http://thebookface.top

##Data Pipeline
Data from producer is ingested using Kafka. Spark streaming with scala is used to process data in realtime. Cassandra is used as database choices. Finally flask and d3.js is used as front-end to display data

#Spark
Spark is framework to calculate data in distributed manner. In order to programmatically modify or process stream, functional programming is needed as it is suitable for parallel computation. I used Scala to modify RDD in spark. The key point on functional programming is mapping or applying function to data to modify them. Below are snippet of code to calculate cumulative distribution.   

The simplified process is depicted here where each process contain mapping and eventually reducing data by key and no loop is involved.
