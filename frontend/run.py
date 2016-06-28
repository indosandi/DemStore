#!/usr/bin/env python

from app import app
#app.run(debug=True)
#app.run(host='ec2-52-41-190-81.us-west-2.compute.amazonaws.com', debug = True,port=80 )
app.run(host='0.0.0.0', debug = True,port=80)
