# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/#build-image)

Project Name : Account-Manager

AccountController - is the controller class to process the requestAPI consists of Post Methods, namely,
AccountService - is a helper class to perform business logic with validations.


/getAccountBalance - POST Method to provide the current account balance of an account.
Request body passed as JSON object with two fields as Account Holder name and Account number.
API reponds ResponseEntity object with HTTPStatus code and response body consists of params - 
1) Completed as boolean for successful & failure transactions.
2) errorMsg passes any error message from API.
3) data provides response with Account details.
4) violations param returns any validation error passed in request.

Input:
{
  "accountHolder": "CaptainAmerica",
  "accountNumber":123456
}


Output - for server Respone code 200.
{
  "completed": true,
  "errorMsg": null,
  "violations": null,
  "data": [
    {
      "accountNumber": 123456,
      "accountName": "CaptainAmerica",
      "bankName": "acme",
      "currenyCode": "HKD",
      "currentBalance": 9000
    }
  ]
}

/tranfer - API peforms account transfer from one account to another account. 

RequestBody:
{
  "accountHolder": "captainAmerica",
  "amountToTransfer": 500.00,
  "bankName": "acme",
  "fromAccountNumber": 123456,
  "toAccountNumber": 555555
}

ResponseBody:
{
  "completed": true,
  "errorMsg": null,
  "violations": null,
  "data": [
    {
      "accountNumber": 123456,
      "accountName": "CaptainAmerica",
      "bankName": "acme",
      "currenyCode": "HKD",
      "currentBalance": 8500
    }
  ]
}