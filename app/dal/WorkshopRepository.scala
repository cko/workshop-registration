package dal

import java.time.Instant

import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import javax.inject.Inject
import javax.inject.Singleton
import models.Workshop
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf

@Singleton
class WorkshopRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class WorkshopTable(tag: Tag) extends Table[Workshop](tag, "workshops") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def regstart = column[String]("regstart")
    def regend = column[String]("regend")
    def regmax = column[Int]("regmax")
    def when = column[String]("when")
    def title = column[String]("title")
    def description = column[String]("description")
    def * = (id.?, regstart, regend, regmax, when, title, description) <> (Workshop.tupled, Workshop.unapply)
  }

  private val workshops = TableQuery[WorkshopTable]

  def insert(workshop: Workshop): Future[Int] = db.run {
    workshops += workshop
  }

  def list(): Future[Seq[Workshop]] = db.run {
    workshops.result
  }

  def active(): Future[Option[Workshop]] = db.run {
    val now = Instant.now().toString()
    workshops.filter(w => w.regstart < now && w.regend > now).result.headOption
  }

  def count(): Future[Int] = {
    list().map { registration => registration.size }
  }

}