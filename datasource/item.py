import time
import random
#class storeLib(object):
#itemListN=['Bread','Bread','Bread','Toiletries','Toiletries','Drug','Chocolate','Beer','LOTTERY','Tobbaco','Soda']
#itemListS=['Bread','Bread','Toiletries','Toiletries','Toiletries','Drug','Chocolate','Beer','LOTTERY','Tobbaco','Soda']
#itemListW=['Bread','Toiletries','Drug','Drug','Drug','Chocolate','Chocolate','Beer','LOTTERY','Tobbaco','Tobbaco','Soda']
#itemListE=['Bread','Toiletries','Drug','Chocolate','Beer','Beer','LOTTERY','LOTTERY','LOTTERY','LOTTERY','Tobbaco','Tobbaco','Tobbaco','Soda']
itemListN=['Bread','Bread','Bread','Drug','Chocolate','Beer','Tobbaco','Soda']
itemListS=['Bread','Bread','Drug','Chocolate','Beer','Tobbaco','Soda']
itemListW=['Bread','Drug','Drug','Drug','Chocolate','Chocolate','Beer','Tobbaco','Tobbaco','Soda']
itemListE=['Bread','Drug','Chocolate','Beer','Beer','Tobbaco','Tobbaco','Tobbaco','Soda']

location=['North','South','West','East'] 
def getTimeStamp():
    millis = int(round(time.time() * 1000))
    return millis
def getItemScanned():
    locat=random.choice(location); 
    if (locat=="North"):
        out=[locat,random.choice(itemListN),getTimeStamp()]
    elif (locat=="South"):
        out=[locat,random.choice(itemListS),getTimeStamp()]
    elif (locat=="West"):
        out=[locat,random.choice(itemListW),getTimeStamp()]
    elif (locat=="East"):
        out=[locat,random.choice(itemListE),getTimeStamp()]
    return out
