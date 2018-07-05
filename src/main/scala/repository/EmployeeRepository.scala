package repository

import com.amazonaws.services.dynamodbv2.model.DeleteItemResult
import com.gu.scanamo.error.DynamoReadError
import models.Employee
import scala.concurrent.Future

trait EmployeeRepository
{
    def put(employee : Employee) : Future[Option[Either[DynamoReadError, Employee]]]

    def get(id : Long) : Future[Option[Either[DynamoReadError, Employee]]]

    def delete(id : Long) : Future[DeleteItemResult]

    def getAll : Future[List[Either[DynamoReadError, Employee]]]
}
