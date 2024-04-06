<h1>CRUD JAVA SPRING</h1>

new project crud without db

project for study language of the java.

<h2>Start Project</h2>
<i>Project in jdk 21;</i>

To work with JDK 21,
    - the Jacoco coverage plugin must be version 0.8.11, whereas the Maven build tool must be version 3.9.6. The versions listed below do not support JDK 21.
    
 - Added unit tests for component layers - Created integration tests for controller layers.
 - The Jacoco plugin was used for code coverage.
 - The business logic of service classes was updated, and appropriate http responses were added to the controller methods.
 - The application has tested with 100% code coverage.
 - All the changes are added to withtest branch.
   

run project;<br><br>
Methods Local: 
    <ul>
      <li>GET - http://localhost:8080/order/allorders</li>
      <li>GET - http://localhost:8080/order/1561</li>
      <li>POST - http://localhost:8080/order/addData</li>
      <li>PUT - http://localhost:8080/order/1561</li>
      <li>DELETE - http://localhost:8080/order/1561</li>
    </ul>
Body for POST:
  {
  	"id": 3,
  	"price": 5
  }

Body for PUT:
<li>PUT - http://localhost:8080/order/1561</li>
  {
    "id": 1561
  	"price": 56
  }

