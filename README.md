# FetchPointsAssignment
Fetch webservice assignment for user rewards management.

## Requirements:

1. Java JDK 8
2. Maven 
	Link to download: http://maven.apache.org/download.cgi
	Install instructions: http://maven.apache.org/install.html


## Project Installation:

### Installing all the dependencies required; delete previously compiled java .class and compile, test and packagethe project.
	1. cmd/bash/powershell
	2. cd FetchWS
	3. mvnw.cmd clean install or ./mvnw.cmd clean install (Powershell)

### Run the compiled project
	1. cmd/bash/powershell
	2. cd FetchWS
	3. mvnw spring-boot:run or ./mvnw spring-boot:run (powershell)


## API Documentation:

Web service for transactions related to user's reward points. Endpoints definded to get the transactions of users, add transaction for a user, deduct points from users and show user's points.  Accepts HTTP requests and returns HTTP responses.

## Assumptions:
When a transaction is being addeded, it's timestamp will be the current date & time and each addition will check if the transaction to be added is a valid one.

## Allowed Endpoints:

### 1. Get all transactions for all users:

#### Example:

#### Endpoint: 
	api/v1.0/transactions

#### Request: 
	curl http://localhost:8080/api/v1.0/transactions

#### Response:
	{"1":[{"transactionId":1,"payer":"DANNON","points":100.0,"timestamp":"2021-02-17T11:22:07.786"},{"transactionId":2,"payer":"UNILEVER","points":200.0,"timestamp":"2021-02-17T11:22:17.008"},{"transactionId":4,"payer":"MILLER COORS","points":10000.0,"timestamp":"2021-02-17T11:22:55.473"},{"transactionId":5,"payer":"DANNON","points":1000.0,"timestamp":"2021-02-17T11:23:07.353"}]}

#### Response Status:
	1. Successfull request : 200 OK 


### 2. Get transaction by user id:

#### Example:

#### Endpoint: 
	api/v1.0/addpoints/1 (adding points for user 1)

#### Request: 
	curl http://localhost:8080/api/v1.0/transactions/1 (User id)

#### Response: 
	[{"transactionId":1,"payer":"DANNON","points":100.0,"timestamp":"2021-02-17T11:22:07.786"},{"transactionId":2,"payer":"UNILEVER","points":200.0,"timestamp":"2021-02-17T11:22:17.008"},{"transactionId":4,"payer":"MILLER COORS","points":10000.0,"timestamp":"2021-02-17T11:22:55.473"},{"transactionId":5,"payer":"DANNON","points":1000.0,"timestamp":"2021-02-17T11:23:07.353"}]

#### Response Status:

	1. Successfull request: 200 OK

	2. User not found in the system: 	"status": 404,
   			 "error": "Not Found",
    			"message": "User id not present.",


	3. Add reward points to user's account

### 3. Add transaction/points to user's account.

#### Example:

#### Endpoint: 
	api/v1.0/addpoints/1 (adding points for user 1)

#### Request: 
	curl -H "Content-Type: application/json" -d "{\"payerName\":\"DANNON\",\"points\":300}" http://localhost:8080/api/v1.0/addpoints/1 (Windows, escape character for key & value of JSON data included)

#### Response: 
	{"transactionId":1,"payerName":"DANNON","points":300.0,"transactionDate":"2021-02-16T20:06:54.765"}.

#### Response Status:

	1. Successfull request: 200 OK

	2. Invalid Transaction: "status": 400,
		  "error": "Bad Request",
	    	  "message": "Invalid transaction."	

### 4. Deduct points from a user

#### Example:

#### Endpoint: 
	api/v1.0/deduct/1(userid)/5000(points to deduct) 

#### Request: 
	curl http://localhost:8080/api/v1.0/deduct/1/5000

#### Response: 
	["DANNON -100.0 Now","UNILEVER -200.0 Now","MILLER COORS -4700.0 Now"]

#### Response Status:

	1. Successfull request: 200 OK

	2. User not present in the system: "status": 404,
   			  "error": "Not Found",
    			  "message": "User id not present."

	3. Invalid transaction request: "status": 404,
   			  		"error": "Not Found",
    			  		"message": "Invalid transaction."

### 5. Show user's points per payer

#### Example:

#### Endpoint: 
	api/v1.0/show/1 (user id) 

#### Request: 
	curl http://localhost:8080/api/v1.0/show/1/5000

#### Response:
	["UNILEVER, 0.0 points","MILLER COORS, 5300.0 points","DANNON, 1000.0 points"]

#### Response Status:

	1. Successfull request: 200 OK

	2. User not present in the system: "status": 404,
   					  "error": "Not Found",
    					  "message": "User id not present."
 
