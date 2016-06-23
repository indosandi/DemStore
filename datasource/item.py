from random import random
from bisect import bisect
import time
import random

itemListN=['Bread','Bread','Drug','Chocolate','Beer','Tobbaco','Soda','Soda']
itemListN2=['Bread','Drug','Drug','Chocolate','Beer','Tobbaco','Soda']
itemListN3=['Bread','Drug','Chocolate','Beer','Beer','Tobbaco','Soda','Soda']
itemListS=['Bread','Bread','Drug','Drug','Chocolate','Beer','Tobbaco','Soda']
itemListS2=['Bread','Bread','Drug','Drug','Drug','Chocolate','Beer','Beer','Tobbaco','Soda']
itemListS3=['Bread','Bread','Drug','Drug','Chocolate','Chocolate','Beer','Tobbaco','Soda']
itemListW=['Bread','Drug','Drug','Drug','Chocolate','Chocolate','Beer','Tobbaco','Tobbaco','Soda']
itemListW2=['Bread','Drug','Drug','Chocolate','Chocolate','Chocolate','Beer','Tobbaco','Soda','Soda']
itemListW3=['Bread','Drug','Drug','Drug','Chocolate','Chocolate','Beer','Tobbaco','Tobbaco','Tobbaco','Soda','Soda']
itemListE=['Bread','Bread','Bread','Drug','Chocolate','Beer','Beer','Tobbaco','Tobbaco','Tobbaco','Soda']
itemListE2=['Bread','Bread','Drug','Drug','Chocolate','Beer','Beer','Beer','Tobbaco','Tobbaco','Soda']
itemListE3=['Bread','Bread','Drug','Chocolate','Chocolate','Beer','Beer','Beer','Tobbaco','Tobbaco','Tobbaco','Soda']

item0=[itemListN,itemListN2,itemListN3]
item1=[itemListS,itemListS2,itemListS3]
item2=[itemListW,itemListW2,itemListW3]
item3=[itemListE,itemListE2,itemListE3]
itemn=itemListN
items=itemListS
itemw=itemListW
iteme=itemListE
location=['San Francisco','Palo Alto','San Jose','Oakland'] 
currentTime=0
interval=5000
def getTimeStamp():
    millis = int(round(time.time() * 1000))
    return millis
def getItemScanned():
    detTime()
    locat=random.choice(location); 
    if (locat==location[0]):
	tempc=random.choice(item0)
        out=[locat,random.choice(itemn),getTimeStamp()]
    elif (locat==location[1]):
	tempc=random.choice(item1)
        out=[locat,random.choice(items),getTimeStamp()]
    elif (locat==location[2]):
	tempc=random.choice(item2)
        out=[locat,random.choice(itemw),getTimeStamp()]
    elif (locat==location[3]):
	tempc=random.choice(item3)
        out=[locat,random.choice(iteme),getTimeStamp()]
    return out
def detTime():
    #temporary solution
    global currentTime
    global itemn
    global items
    global itemw
    global iteme
    now=getTimeStamp()
    if(now-currentTime>interval):
	itemn=random.choice(item0)
	items=random.choice(item1)
	itemw=random.choice(item2)
	iteme=random.choice(item3)
	currentTime=now

