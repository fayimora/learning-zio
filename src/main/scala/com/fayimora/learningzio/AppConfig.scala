package com.fayimora.learningzio

import zio.config.*
import typesafe.*
import ConfigSource.*
import ConfigDescriptor.*
import com.typesafe.config.ConfigFactory
import typesafe.*
import zio.*
import zio.config.magnolia.*
import java.util.UUID
import com.typesafe.config.ConfigParseOptions
import com.typesafe.config.ConfigSyntax

final case class AppConfig(simpleCrawler: CrawlerConfig, postmark: PostmarkConfig)
final case class CrawlerConfig(
    mainUrl: String,
    allowedSubDomains: List[String],
    workers: Int,
  )
final case class PostmarkConfig(
    serverToken: String,
    from: String,
    template: String,
    url: String,
  )

object AppConfig:
  implicit private val configDescriptor: ConfigDescriptor[AppConfig] = descriptor[AppConfig]

  def config: IO[ReadError[K], AppConfig] =
    val confOptions = ConfigParseOptions.defaults.setSyntax(ConfigSyntax.CONF)
    read(
      configDescriptor.from(
        ConfigSource.fromTypesafeConfig(
          ZIO.attempt(ConfigFactory.load().resolve())
        )
      )
    )

  val layer = ZLayer.fromZIO(config)

  val postmarkConfigLayer = ZLayer.fromZIO(config.map(_.postmark))
