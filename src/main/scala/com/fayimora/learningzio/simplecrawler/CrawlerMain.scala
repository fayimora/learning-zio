package com.fayimora.simplecrawler

import com.typesafe.config.ConfigFactory
import zio.*
import zio.Console.{printLine, printLineError}
import zio.Duration.*
import zio.config.*
import zio.config.magnolia.descriptor
import zio.config.typesafe.*, ConfigDescriptor.*

import java.util.concurrent.TimeUnit
import scala.collection.mutable.HashSet as MutableHashSet

object CrawlerMain extends ZIOAppDefault:

  def run =
    for
      // Read crawler config values
      conf <- CrawlerConfig.config
      initialWeblink = WebLink(conf.mainUrl)
      allowedSubDomains = conf.allowedSubDomains.map(WebLink.apply).toSet

      // Setup crawler data-structures and benchmarking timer
      startTime <- Clock.currentTime(TimeUnit.SECONDS)
      queue <- Queue.unbounded[WebLink]
      _ <- queue.offer(initialWeblink)
      cache <- Ref.make[MutableHashSet[WebLink]](MutableHashSet.empty[WebLink])

      // Trigger Crawl
      _ <- printLine(s"Starting crawl from $initialWeblink")
      numOfProcessedItems <- CrawlerImpl(queue, cache).run(conf.workers, allowedSubDomains)

      // Print run & benchmarking info
      _ <- printLine(s"Crawl complete. $numOfProcessedItems items processed")
      endTime <- Clock.currentTime(TimeUnit.SECONDS)
      timeTakenSecs = endTime-startTime
      timeTakenMins = timeTakenSecs/60.0
      _ <- printLine(s"Time taken: $timeTakenSecs secs OR $timeTakenMins mins")
    yield ()
