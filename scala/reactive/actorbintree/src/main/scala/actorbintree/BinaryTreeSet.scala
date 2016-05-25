/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package actorbintree

import actorbintree.BinaryTreeNode.{CopyFinished, CopyTo}
import akka.actor.Status.Failure
import akka.actor._
import scala.collection.Map
import scala.collection.immutable.Queue

object BinaryTreeSet {

  trait Operation {
    def requester: ActorRef

    def id: Int

    def elem: Int
  }

  trait OperationReply {
    def id: Int
  }

  /** Request with identifier `id` to insert an element `elem` into the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Insert(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to check whether an element `elem` is present
    * in the tree. The actor at reference `requester` should be notified when
    * this operation is completed.
    */
  case class Contains(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to remove the element `elem` from the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Remove(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request to perform garbage collection */
  case object GC

  /** Holds the answer to the Contains request with identifier `id`.
    * `result` is true if and only if the element is present in the tree.
    */
  case class ContainsResult(id: Int, result: Boolean) extends OperationReply

  /** Message to signal successful completion of an insert or remove operation. */
  case class OperationFinished(id: Int) extends OperationReply

}


class BinaryTreeSet extends Actor {

  import BinaryTreeSet._

  def createRoot: ActorRef = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = true))

  var root = createRoot

  // optional
  var pendingQueue = Queue.empty[Operation]

  // optional
  def receive = normal

  // optional
  /** Accepts `Operation` and `GC` messages. */
  val normal: Receive = {
    case op: Operation => root ! op

    case GC => {
      val newRoot = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = true))
      root ! CopyTo(newRoot)
      context become garbageCollecting(newRoot)
    }
    case _ => context.parent ! Failure(new IllegalArgumentException("Unknown message"))
  }

  // optional
  /** Handles messages while garbage collection is performed.
    * `newRoot` is the root of the new binary tree where we want to copy
    * all non-removed elements into.
    */
  def garbageCollecting(newRoot: ActorRef): Receive = {
    case op: Operation => pendingQueue enqueue op

    case CopyFinished =>
      root ! PoisonPill
      root = newRoot
      pendingQueue.foreach(op => root ! op)
      pendingQueue = Queue.empty[Operation]
      context become normal
  }

}

object BinaryTreeNode {

  trait Position {
    override def toString: String
  }

  case object Left extends Position{
    override def toString: String = "Left"
  }

  case object Right extends Position{
    override def toString: String  = "Right"
  }

  case class CopyTo(treeNode: ActorRef)

  case object CopyFinished

  def props(elem: Int, initiallyRemoved: Boolean) = Props(classOf[BinaryTreeNode], elem, initiallyRemoved)
}

class BinaryTreeNode(val elem: Int, initiallyRemoved: Boolean) extends Actor with ActorLogging{

  import BinaryTreeNode._
  import BinaryTreeSet._

  var subtrees = Map[Position, ActorRef]()
  var removed = initiallyRemoved

  // optional
  def receive = normal

  // optional
  /** Handles `Operation` messages and `CopyTo` requests. */
  val normal: Receive = {

    case op: Operation if op.elem < elem && subtrees.isDefinedAt(Left) => subtrees(Left) ! op
    case op: Operation if op.elem > elem && subtrees.isDefinedAt(Right) => subtrees(Right) ! op

    case Insert(requester, id, value) =>
      insert(value)
      requester ! OperationFinished(id)
    case Remove(requester, id, value) =>
      remove(value)
      requester ! OperationFinished(id)
    case Contains(requester, id, value) =>
      println("Contains " + value + "? Element = " + elem)
      requester ! ContainsResult(id, elem == value && !removed)

    case CopyTo(newRoot) => {
      context become copying (newRoot, !removed)
      subtrees match {
        case map if map.isEmpty => self ! CopyFinished
        case map => {
          map.values.foreach(actorRef => actorRef ! CopyTo(newRoot))
        }
      }
    }
  }

  // optional
  /** `expected` is the set of ActorRefs whose replies we are waiting for,
    * `insertConfirmed` tracks whether the copy of this node to the new tree has been confirmed.
    */
  def copying(newRoot: ActorRef, insertConfirmed: Boolean): Receive = {
    case CopyFinished => {
      if (insertConfirmed) newRoot ! Insert(newRoot, 0, elem)
      context.parent ! CopyFinished
    }
  }

  private[BinaryTreeNode] def insert(elem: Int): Unit = {

    def addToSubtree(position: Position, value: Int) = {
      println("Inserted " + value + " to " + position)
      subtrees = subtrees + ((position, context.actorOf(BinaryTreeNode.props(value, initiallyRemoved = false))))
    }


    elem match {
      case value if elem == this.elem => removed = false
      case value if elem < this.elem => addToSubtree(Left, value)
      case value if elem > this.elem => addToSubtree(Right, value)
    }
  }

  private[BinaryTreeNode] def remove(elem: Int): Unit = {
    if (elem == this.elem) removed = true
  }

}
