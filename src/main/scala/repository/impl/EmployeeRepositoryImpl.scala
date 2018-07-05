package repository.impl

import com.amazonaws.services.dynamodbv2.model.DeleteItemResult
import com.gu.scanamo.ScanamoAlpakka
import com.gu.scanamo.error.DynamoReadError
import com.gu.scanamo.syntax._
import models.Employee
import repository.EmployeeRepository
import repository.impl.EmployeeRepositoryImpl._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Dynamo table called 'Employee' has only 'id' as it hash key or partition key
  **/
object EmployeeRepositoryImpl
{
    val TableName = "Employee"
}

class EmployeeRepositoryImpl(dbClient : DynamoDBClient) extends EmployeeRepository
{
    val client = dbClient.alpakkaClient

    override def put(employee : Employee) : Future[Option[Either[DynamoReadError, Employee]]] =
        ScanamoAlpakka.put(client)(TableName)(employee)

    override def get(id : Long) : Future[Option[Either[DynamoReadError, Employee]]] =
        ScanamoAlpakka.get[Employee](client)(TableName)('id -> id)

    override def delete(id : Long) : Future[DeleteItemResult] =
        ScanamoAlpakka.delete[Employee](client)(TableName)('id -> id)

    override def getAll : Future[List[Either[DynamoReadError, Employee]]] =
        ScanamoAlpakka.scan[Employee](client)(TableName)
}
