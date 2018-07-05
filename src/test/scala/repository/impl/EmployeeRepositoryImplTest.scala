package repository.impl

import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType.N
import models._

class EmployeeRepositoryImplTest extends TestTrait {

    private val employeeRepository = new EmployeeRepositoryImpl(alpakkaClient)

    override def beforeAll() : Unit = {
        super.beforeAll()
        LocalDynamoDB.createTable(client)("Employee")('id -> N)
    }

    it should "put the employee" in {
            val id = scala.util.Random.nextInt()
            val employee = Employee(id, "name", Code(List("code1")))
            val futureRes = employeeRepository.put(employee)
            futureRes.map(res => assert(res === None))
        }

    it should "put & get the employee" in {
            val id = scala.util.Random.nextInt()
            val employee = Employee(id, "name", Code(List("code2")))
            val futureRes = for
                {
                _ <- employeeRepository.put(employee)
                res <- employeeRepository.get(id)
            } yield res
            futureRes.map(res => assert(res === Some(Right(Employee(id, "name", Code(List("code2")))))))
        }

    it should "delete the employee" in {
            val id = scala.util.Random.nextInt()
            val employee = Employee(id, "name", Code(List("code3")))
            val futureRes = for
                {
                _ <- employeeRepository.put(employee)
                resBeforeDelete <- employeeRepository.get(id)
                _ <- employeeRepository.delete(id)
                resAfterDelete <- employeeRepository.get(id)
            } yield (resBeforeDelete, resAfterDelete)

            futureRes.map
            {
                case (resBeforeDelete, resAfterDelete) => assert(resBeforeDelete === Some(Right(Employee(id, "name", Code(List("code3")))))
                    && resAfterDelete === None)
            }
        }

    it should "get all the employees" in {
            val futureRes = employeeRepository.getAll
            futureRes.map(list => assert(list.nonEmpty))
        }

    override def afterAll() : Unit = {
        super.afterAll()
    }
}
