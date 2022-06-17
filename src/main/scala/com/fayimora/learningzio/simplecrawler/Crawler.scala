package com.fayimora.simplecrawler

import org.jsoup.Jsoup
import zio.*
import zio.Console.{printLine, printLineError}
import zio.Duration.*
import zio.stream.*

import java.io.IOException
import scala.collection.mutable.HashSet as MutableHashSet

trait Crawler:
  def run(numOfWorkers: Int, allowedSubdomains: Set[WebLink]): ZIO[Any, IOException, Long]


case class CrawlerImpl(queue: Queue[WebLink], cache: Ref[MutableHashSet[WebLink]]) extends Crawler:
  def run(numOfWorkers: Int, allowedSubDomains: Set[WebLink]): ZIO[Any, IOException, Long] =
    ZStream
      .fromQueueWithShutdown(queue)
      .tap(wl => printLine(pprint.log(wl)))
      .mapZIOPar(numOfWorkers)(wl => crawl(wl, allowedSubDomains))
      .timeout(5.seconds)
      .run(ZSink.count)

  def crawl(link: WebLink, allowedSubDomains: Set[WebLink]) =
    processPage(link, allowedSubDomains)
      .flatMap(links => ZIO.foreach(links)(setProcessedStatus).as(link))

  def processPage(link: WebLink, allowedSubDomains: Set[WebLink]) =
    ZIO.attempt(JSoupWeblinkParser.parse(link, allowedSubDomains))
      .catchAll(t => printLineError(t).as(MutableHashSet.empty[WebLink]))

  def setProcessedStatus(link: WebLink) =
    cache.get.flatMap(hashSet =>
      (cache.update(_.addOne(link)) *> queue.offer(link))
        .unless(hashSet.contains(link)))
