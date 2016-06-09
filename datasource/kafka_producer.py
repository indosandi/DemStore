import random
import sys
import six
import time
import json 
from datetime import datetime
from kafka.client import SimpleClient
from kafka.producer import KeyedProducer
from item import getTimeStamp
from item import getItemScanned

class Producer(object):

    def __init__(self, addr):
        print "ok"
        self.client = SimpleClient(addr)
        self.producer = KeyedProducer(self.client)
    def jsonITEM(self,itemList):
        strout='{'
        strout=strout+'"location":'
        strout=strout+'"'+itemList[0]+'"'+','
        strout=strout+'"item":'
        strout=strout+'"'+str(itemList[1])+'"'+',' 
        strout=strout+'"TimeStamp":'
        strout=strout+str(itemList[2])+',' 
        strout=strout+'"Producer":'
        strout=strout+str(itemList[3]) 
        strout=strout+'}'
        return strout

    def produce_msgs(self, source_symbol):
        price_field = random.randint(800,1400)
        msg_cnt = 0
        
        while True:
            lItem=getItemScanned()
            lItem.append(source_symbol)
            print(self.jsonITEM(lItem))

            message_info=self.jsonITEM(lItem) 
            self.producer.send_messages('price_data_part4', source_symbol, message_info)
            time.sleep(.05)
            msg_cnt += 1
    
if __name__ == "__main__":
    args = sys.argv
    ip_addr = str(args[1])
    partition_key = str(args[2])
    prod = Producer(ip_addr)
    prod.produce_msgs(partition_key) 
