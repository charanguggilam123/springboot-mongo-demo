# springboot-mongo-demo
This Service is a demo to connect to mongo db with username and password and perform crud operations and some inbuilt operations like sort,project,group,match,unwind operations on mongodb


Prerequisites:

1. Mongo db installed> If not please folloe setup from 
2. basic understanding of springboot

After mongo setup is completed which can be tested with below commands

mongod --dbpath C:\Users\sguggilam\Documents\mongodb-win32-x86_64-windows-6.0.3\data --auth # to start mongo server --dbpath is optional

use admin

db.createUser({user: 'GSC',pwd: '12345678',roles: [dbAdmin"]})	# user details used to conenct to mongo server

use gscdemo # database that is used in the current project
			 
db.<collection_name>.find()	=> get all

db.<collection_name>.findOne({param: value})	=> get by query

db.<collection_name>.findOne({param: {$gte: value}}).limit(10)	=> get by filtering revords based on range

db.demo.findOne({id: {$gte: 1}},{name: 1})	=> filtering records based on query criteria and retrieving limited fields(called projection expression)

db.<collection_name>.insertOne({param: value,param2:{param3: val3}})	=> Insert

db.demo.updateOne({id: 1},{$set: {name: 'Sai Charan'}})	=> updating based on criteria. First object is update filter and second obj is filter action

db.demo.deleteOne({id: 1})

please refer to postman collection attached in resouces folder for quick setup of testing

