from app import app
from flask import jsonify
from cassandra.cluster import Cluster
from flask import render_template
import random

@app.route('/')
@app.route('/index')
def index():
  return render_template("d3dex2.html")
# setting up connections to cassandra
cluster = Cluster(['52.39.96.29'])
#change the bolded text to your seed node public dns (no < or > symbols but keep quotations. Be careful to copy quotations as it might copy it as a special character and throw an error. Just delete the quotations and type them in and it should be fine.
session = cluster.connect('play')

@app.route('/api/update')
def get_email():
       stmt = "SELECT * FROM sdata"
       response = session.execute(stmt, parameters=[])
       response_list = []
       for val in response:
            response_list.append(val)
       jsonresponse = [{"location": x.location, "item": x.item, "count": x.count} for x in response_list]
       return jsonify(jsonresponse)
@app.route('/api/disti')
def get_disti():
       stmt = "SELECT * FROM disti"
       response = session.execute(stmt, parameters=[])
       response_list = []
       for val in response:
            response_list.append(val)
       #temporary solution
       jsonresponse = [{"location": x.location, "item": x.item, "dis10":[ x.eight,  x.five,  x.four,  x.nine, x.one, x.seven, x.six, x.ten, x.three, x.two, x.zero]} for x in response_list]
       return jsonify(jsonresponse)
@app.route('/ajaxtest')
def get_data():
       a=random.randint(0,100)
       b=random.randint(0,100)
       c=random.randint(0,100)
       d=random.randint(0,100)
       jsonr=[{"letter":"A","frequency":a},{"letter":"B","frequency":b},{"letter":"C","frequency":c},{"letter":"D","frequency":d},{"letter":"F","frequency":d}]
       return jsonify(jsonr);


