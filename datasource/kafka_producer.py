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
	self.producer = KafkaProducer(bootstrap_servers=["52.39.57.55","52.40.218.111","52.24.38.215","52.33.31.202"],value_serializer=lambda v: json.dumps(v).encode('utf-8'),acks=0,linger_ms=500)

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
            #lItem.append(source_symbol)

            #message_info=self.jsonITEM(lItem) 
	    #message_info={"location":"NORTH","item":"bread","time":128309123,"storeid":0}
	    message_info={"location":lItem[0],"item":lItem[1],"time":lItem[2],"storeid":random.randint(0,NUM_USERS-1)}
            #print(message_info)
            self.producer.send('price_data_part4', message_info)
            #time.sleep(.05)
            msg_cnt += 1
    
if __name__ == "__main__":
    args = sys.argv
    #ip_addr = str(args[1])
    #partition_key = str(args[2])
    #prod = Producer(ip_addr)
    prod=Producer();
    prod.produce_msgs() 
