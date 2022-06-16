package com.fayimora.simplecrawler

import zio.config.*
import typesafe.*
import ConfigSource.*
import ConfigDescriptor.*
import com.typesafe.config.ConfigFactory
import typesafe.*
import zio.*
import zio.config.magnolia.*

case class CrawlerConfig (mainUrl: String, allowedSubDomains: List[String], workers: Int)

object CrawlerConfig:
  private implicit val configDescriptor: ConfigDescriptor[CrawlerConfig] = descriptor[CrawlerConfig]

  def config: IO[ReadError[K], CrawlerConfig] = read(
    configDescriptor.from(ConfigSource.fromTypesafeConfig(
      ZIO.attempt(ConfigFactory.defaultApplication())
    )))

  val layer = ZLayer.fromZIO(config)
