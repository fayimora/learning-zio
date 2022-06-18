package com.fayimora.learningzio.simplecrawler

import org.jsoup.Jsoup
import zio.*

import java.io.IOException
import scala.collection.mutable
import scala.jdk.CollectionConverters.*

trait WeblinkParser:
  def parse(link: WebLink, allowedSubdomains: Set[WebLink]): Iterable[WebLink]

object JSoupWeblinkParser extends WeblinkParser:
  def parse(link: WebLink, allowedSubdomains: Set[WebLink]): Iterable[WebLink] =
    extractPageLinks(link)
      .map(_.absUrl("href"))
      .filter(url => allowedSubdomains.exists(subdomain => url.startsWith(subdomain.url)))
      .toSet
      .map(WebLink.apply)

  private def extractPageLinks(link: WebLink) =
    Jsoup
      .connect(link.url)
      .get()
      .select("a[href]")
      .asScala
