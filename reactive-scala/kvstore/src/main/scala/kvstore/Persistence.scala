package kvstore

import akka.actor.{Props, Actor}
import akka.event.Logging
import org.slf4j.{LoggerFactory, Logger}
import scala.util.Random
import java.util.concurrent.atomic.AtomicInteger

object Persistence {
  case class Persist(key: String, valueOption: Option[String], id: Long)
  case class Persisted(key: String, id: Long)
  case object Restarted

  class PersistenceException extends Exception("Persistence failure")

  def props(flaky: Boolean): Props = Props(classOf[Persistence], flaky)
}

class  Persistence(flaky: Boolean) extends Actor {

  val log = Logging(context.system, this)

  import Persistence._

  def receive = {
    case Persist(key, _, id) => {
      log.info(s"Try to persist key $key and id $id")
      if (!flaky || Random.nextBoolean()) {
        log.info(s"Key $key and id $id are persisted")
        sender ! Persisted(key, id)
      }
      else {
        log.error(s"Persistence actor is failed for $key and $id")
        throw new PersistenceException
      }

    }
  }

  @throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = sender ! Restarted
}
