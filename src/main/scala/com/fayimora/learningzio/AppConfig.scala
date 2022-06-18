package com.fayimora.learningzio

import zio.config.*
import typesafe.*
import ConfigSource.*
import ConfigDescriptor.*
import com.typesafe.config.ConfigFactory
import typesafe.*
import zio.*
import zio.config.magnolia.*

final case class AppConfig(simpleCrawler: CrawlerConfig)
final case class CrawlerConfig (mainUrl: String, allowedSubDomains: List[String], workers: Int)

object AppConfig:
  private implicit val configDescriptor: ConfigDescriptor[AppConfig] = descriptor[AppConfig]

  def config: IO[ReadError[K], AppConfig] = read(
    configDescriptor.from(ConfigSource.fromTypesafeConfig(
      ZIO.attempt(ConfigFactory.defaultApplication())
    )))

  val layer = ZLayer.fromZIO(config)