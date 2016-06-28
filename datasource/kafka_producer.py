import random
import sys
import six
import time
import json 
from datetime import datetime
from kafka import KafkaProducer
from kafka.client import SimpleClient
from kafka.producer import KeyedProducer
from item import getTimeStamp
from item import getItemScanned

NUM_USERS=3000
class Producer(object):

    def __init__(self):
        #self.client = SimpleClient(addr)
	#self.producer = KeyedProducer(self.client)
	self.producer = KafkaProducer(bootstrap_servers=["50.112.40.243","52.25.13.29","50.112.22.187","52.24.80.162"],value_serializer=lambda v: json.dumps(v).encode('utf-8'),acks=0,linger_ms=500)

    def jsonITEM(self,itemList):
        strout='{'
        strout=strout+'"location":'
        strout=strout+'"'+itemList[0]+'"'+','
        strout=strout+'"item":'
        strout=strout+'"'+str(itemList[1])+'"'+',' 
        strout=strout+'"time":'
        strout=strout+str(itemList[2])+',' 
        strout=strout+'"Producer":'
        strout=strout+str(itemList[3]) 
        strout=strout+'}'
        return strout

    def produce_msgs(self):
        msg_cnt = 0
        
        while True:
            lItem=getItemScanned()
	    message_info={"location":lItem[0],"item":lItem[1],"time":lItem[2],"storeid":random.randint(0,NUM_USERS-1)}
            self.producer.send('price', message_info)
	    print(message_info)
            time.sleep(.05)
            msg_cnt += 1
    
if __name__ == "__main__":
    args = sys.argv
    prod=Producer();
    prod.produce_msgs() 
