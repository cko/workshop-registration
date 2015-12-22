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
      def * = (id, name, email) <> ((Registration.apply _).tupled, Registration.unapply)
    }

    private val registrations = TableQuery[RegistrationTable]

    def create(name: String, email: String): Future[Registration] = db.run {
      (registrations.map(p => (p.name, p.email))
        returning registrations.map(_.id)
        into ((nameAge, id) => Registration(id, nameAge._1, nameAge._2))
        ) += (name, email)
    }

  def list(): Future[Seq[Registration]] = db.run {
    registrations.result
  }
}