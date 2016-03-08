package service

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import javax.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import org.apache.commons.lang3.time.DateUtils
import play.api.Play
import scala.concurrent.Future
import com.mohiva.play.silhouette.api.services.IdentityService
import dal.{RegistrationRepository, UserRepository}
import models.User
import scala.concurrent.ExecutionContext


class UserService @Inject() (val userRepo: UserRepository, val registrationRepo: RegistrationRepository)(implicit ec: ExecutionContext) extends IdentityService[User]{

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userRepo.find(loginInfo)

  def save(user: User) = userRepo.save(user)

  def isRegistrationStarted:Boolean = {
    val format = new SimpleDateFormat("yyyy-MM-dd")
    val registrationStart = Play.current.configuration.getString("registration.start")
    val start = format.parse(registrationStart.get)
    val now = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)
    start.before(now)
  }

  def isNotBookedOut:Boolean = {
    val maxNumber = Play.current.configuration.getInt("registration.maxnumber").get
    val allRegistrations = registrationRepo.list().value
    var currentNumber = 0
    if (!allRegistrations.isEmpty){
      currentNumber = allRegistrations.get.get.size
    }
    currentNumber <= maxNumber
  }

  def isNotOver:Boolean = {
    val format = new SimpleDateFormat("yyyy-MM-dd")
    val registrationEnd = Play.current.configuration.getString("registration.end")
    val end = format.parse(registrationEnd.get)
    val now = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)
    end.after(now)
  }
  
  def maxNumberParticipants:Future[Integer] = {
    val confMaxNumber = Play.current.configuration.getInt("registration.maxnumber").get
    registrationRepo.count().map { count => confMaxNumber - count }
  }
}
