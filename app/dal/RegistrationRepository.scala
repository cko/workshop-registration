package dal

import javax.inject.{ Inject, Singleton }

import models.{Registration}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{Future, ExecutionContext}


@Singleton
class RegistrationRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class RegistrationTable(tag: Tag) extends Table[Registration](tag, "registrations") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")
    def workshopId = column[Long]("workshop_id")
    def * = (id, name, email, workshopId) <> ((Registration.apply _).tupled, Registration.unapply)
  }

  private val registrations = TableQuery[RegistrationTable]

  def create(name: String, email: String, workshopId: Long): Future[Registration] = db.run {
    (registrations.map(p => (p.name, p.email, p.workshopId))
      returning registrations.map(_.id)
      into ((reg, id) => Registration(id, reg._1, reg._2, reg._3))
      ) += (name, email, workshopId)
  }

  def list(workshopId: Long): Future[Seq[Registration]] = db.run {
    registrations.filter(r => r.workshopId === workshopId).result
  }

  def count(workshopId: Long): Future[Int] = {
    list(workshopId).map { registration => registration.size }
  } 

}