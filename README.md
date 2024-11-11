# Guide to run application from Docker
1. docker build -t receipt-processor .
2. docker run -p 8080:8080 receipt-processor
# Test application Api
1. http://localhost:8080/auth/login/123/123
   * With 123 is username and password for this demo
   * The application will check if username and password correct
   * Then application with generate and return token string which will only be valid in 1 hour
2. http://localhost:8080/receipts/process
   * Copy token from Step1 to Bearer Token and add body as JSON file 
   * I mimic way of using Bearer Token. Therefore, in application I will check if token start with Bearer 
   * Application will check token before process the request
   * This API will return id in UUID format, which will be needed for next step
3. http://localhost:8080/receipts/{id}/points
   * Copy id from step 2 to {id} in URL
   * Copy token from step 1 to Bearer Token
   * This API will return points based on rules of calculation.
