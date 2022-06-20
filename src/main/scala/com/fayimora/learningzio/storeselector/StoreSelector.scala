package com.fayimora.learningzio.storeselector

import zio.*

object StoreSelector:
  def apply[T](tasks: List[Task[T]], delay: Duration): ZIO[Any, Throwable, T] =
    tasks match
      case Nil                    => ZIO.fail(new IllegalArgumentException("No stores available"))
      case task :: Nil            => task
      case task :: remainingTasks =>
        // use single element queue to know if there was a failure
        Queue.bounded[Unit](1).flatMap { taskFailedQueue =>
          val taskWithSignalFailed = task.onError(_ => taskFailedQueue.offer(()))
          // we either hold for ack or skip that if the request failed
          val sleepOrFailed = ZIO.sleep(delay).race(taskFailedQueue.take)
          taskWithSignalFailed.race(sleepOrFailed *> apply(remainingTasks, delay))
        }
