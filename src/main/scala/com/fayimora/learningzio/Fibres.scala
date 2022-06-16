package com.fayimora.learningzio

import zio.*, Console.*

object ZIOFibresPlayground extends ZIOAppDefault {
  override def run =
    val fayiShower = ZIO.succeed("Fayi is taking a shower")
    val joShower = ZIO.succeed("Jo is taking a shower")
    val joCooking = ZIO.succeed("Jo is scrambling the eggs")
    val havingBreakfast = ZIO.succeed("We are having breakfast")

    def printThread = s"[${Thread.currentThread()}]"

    val program = for {
      // start both showers concurrently
      fayiShowerFibre <- fayiShower.debug(printThread).fork
      joShowerFibre <- joShower.debug(printThread).fork
      showerFibre = fayiShowerFibre <*> joShowerFibre

      // wait for both showers to complete before moving on
      showersComplete <- showerFibre.join.debug(printThread).delay(3.seconds)

      // announce that showers are done, jo starts scrambling eggs
      _ <- ZIO.succeed(s"showers Complete").debug(printThread).fork *> joCooking.debug(printThread)


      // back to having breakfast on previous thread
      _ <- havingBreakfast.debug(printThread)
      _ <- printLine("All done")

    } yield ()


    program.exitCode
}