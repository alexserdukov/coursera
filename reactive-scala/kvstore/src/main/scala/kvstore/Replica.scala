package kvstore

import akka.actor.{OneForOneStrategy, Props, ActorRef, Actor}
import kvstore.Arbiter._
import scala.Option
import scala.collection.immutable.Queue
import akka.actor.SupervisorStrategy.Restart
import scala.annotation.tailrec
import akka.pattern.{ask, pipe}
import akka.actor.Terminated
import scala.concurrent.duration._
import akka.actor.PoisonPill
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.util.Timeout

object Replica {

  sealed trait Operation {
    def key: String
    def id: Long
  }

  /**
   *
   * @param key unique identifier of db record
   * @param value arbitrary data
   * @param id
   */
  case class Insert(key: String, value: String, id: Long) extends Operation

  case class Remove(key: String, id: Long) extends Operation

  case class Get(key: String, id: Long) extends Operation

  sealed trait OperationReply

  case class OperationAck(id: Long) extends OperationReply

  case class OperationFailed(id: Long) extends OperationReply

  case class GetResult(key: String, valueOption: Option[String], id: Long) extends OperationReply

  def props(arbiter: ActorRef, persistenceProps: Props): Props = Props(new Replica(arbiter, persistenceProps))
}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor {

  import Replica._
  import Replicator._
  import Persistence._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  var kv = Map.empty[String, String]
  // a map from secondary replicas to replicators
  var secondaries = Map.empty[ActorRef, ActorRef]
  // the current set of replicators
  var replicators = Set.empty[ActorRef]

  val persistActor = context.actorOf(persistenceProps)

  arbiter ! Join

  def receive = {
    case JoinedPrimary => context.become(leader)
    case JoinedSecondary => context.become(replica)
  }

  /* TODO Behavior for  the leader role. */
  val leader: Receive = {
    case Replicas(actors) => {
      //TODO  define a name for replicator actor
      actors.foreach(actor => {
        val replicator = context.actorOf(Replicator.props(actor))
        secondaries + (actor -> replicator)
        replicators + context.actorOf(Replicator.props(actor))
      });

    }
    case Insert(key, value, id) => {
      kv = kv + (key -> value)
      replicators foreach {
        replicator => {
          replicator ! Replicate(key, Some(value), id)
        }
      }
      persistActor ! Persist(key, Some(value), id)
      println("Insert completed")
    }
    case Remove(key, id) => {
      kv = kv - key
      replicators foreach {
        replicator => {
          replicator ! Replicate(key, None, id)
        }
      }
      println("Remove done")
    }
    case Replicated(key, id) => {
      sender ! OperationAck(id)
    }
    case Persisted(key, id) => {

    }
    case Get(key, id) => {
      sender ! GetResult(key, Option(kv(key)), id)
    }
    case _ => println("Unknown message received ")
  }

  /* TODO Behavior for the replica role. */
  val replica: Receive = {
    case Snapshot(key, valueOption, seq) => {
      valueOption match {
        case Some(value) => kv + (key -> value)
        case None => kv - key
      }
    }
    case Get(key, id) => {
      sender ! GetResult(key, Option(kv(key)), id)
    }
    case _ =>
  }

}

