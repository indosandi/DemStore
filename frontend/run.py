#!/usr/bin/env python

from app import app
#app.run(debug=True)
app.run(host='ec2-52-40-58-67.us-west-2.compute.amazonaws.com', debug = True )
