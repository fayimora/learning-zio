package com.fayimora.simplecrawler

import zio.*
import zio.test.*
import zio.test.Assertion.{isEmpty, equalTo}

import scala.collection.mutable.HashSet as MutableHashSet

object CrawlerImplSpec extends ZIOSpecDefault:
  val localWeblink = WebLink("http://localhost:8888/test-page.html")
  val allowedSubDomains = Set(WebLink("http://localhost"))

  override def spec: Spec[Any, Any] =
    suite("CrawlerImplSpec")(specs =
      test("processPage() returns the right list of urls") {
        for
          queue <- Queue.unbounded[WebLink]
          cache <- Ref.make(MutableHashSet.empty[WebLink])
          weblinks <- CrawlerImpl(queue, cache).processPage(localWeblink, allowedSubDomains)
        yield assertTrue(weblinks.size == 3)
      },

      test("processPage() returns the no elements if there are no valid subdomains") {
        for
          queue <- Queue.unbounded[WebLink]
          cache <- Ref.make(MutableHashSet.empty[WebLink])
          weblinks <- CrawlerImpl(queue, cache).processPage(localWeblink, Set.empty[WebLink])
        yield assertTrue(weblinks.isEmpty)
      },

      test("crawl() effect should complete successfully"){
        for
          queue <- Queue.unbounded[WebLink]
          cache <- Ref.make(MutableHashSet.empty[WebLink])
          _ <- CrawlerImpl(queue, cache).crawl(localWeblink, allowedSubDomains)
        yield assertCompletes
      },

      test("crawl() should return the right url") {
        for
          queue <- Queue.unbounded[WebLink]
          cache <- Ref.make(MutableHashSet.empty[WebLink])
          weblink <- CrawlerImpl(queue, cache).crawl(localWeblink, allowedSubDomains)
        yield assertTrue(weblink.url == localWeblink.url)
      },

      test("run() completes successfully"){
        for
          queue <- Queue.unbounded[WebLink]
          cache <- Ref.make(MutableHashSet.empty[WebLink])
          _ <- CrawlerImpl(queue, cache).run(1, allowedSubDomains).fork
          _ <- TestClock.adjust(10.seconds)
        yield assertCompletes
      }
    )