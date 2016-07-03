package service

import java.time.Instant

import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService

import dal.RegistrationRepository
import dal.UserRepository
import dal.WorkshopRepository
import javax.inject.Inject
import javax.inject.Singleton
import models.User

class UserService @Inject() (
    val userRepo: UserRepository,
    val registrationRepo: RegistrationRepository,
    val workshopRepo: WorkshopRepository)(implicit ec: ExecutionContext) extends IdentityService[User] {

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userRepo.find(loginInfo)

  def save(user: User) = userRepo.save(user)

  def isRegistrationStarted:Future[Boolean] = {
    workshopRepo.active().map {
      case Some(workshop) => {
        val start = Instant.parse(workshop.regstart)
        Instant.now().isAfter(start)
      }
      case None => false
    }
  }

  def isNotBookedOut: Future[Boolean] = {
    workshopRepo.active().flatMap {
      case Some(workshop) => {
        registrationRepo.count(workshop.id.get).map { count => count <= workshop.regmax }
      }
      case None => Future(false)
    }
  }

  def isNotOver:Future[Boolean] = {
    workshopRepo.active().map {
      case Some(workshop) => {
        val end = Instant.parse(workshop.regend)
        Instant.now().isBefore(end)
      }
      case None => false
    }
  }

  def getFreePlaces:Future[Int] = {
    workshopRepo.active().flatMap {
      case Some(workshop) => {
        registrationRepo.count(workshop.id.get).map { count => 
          workshop.regmax - count }
      }
      case None => Future(0)
    }
  }
}
