package com.fayimora.learningzio.postmarkclient

import zio.*
import zio.json.*
import java.time.OffsetDateTime
import java.util.UUID
import com.fayimora.learningzio.PostmarkConfig
import PostmarkEmail.*
import PostmarkEmailResponse.*
import java.io.OutputStream

trait PostmarkClient:
  def sendEmail(email: PostmarkEmail): ZIO[PostmarkClient, Throwable, PostmarkEmailResponse]

object PostmarkClient:
  def sendEmail(email: PostmarkEmail): ZIO[PostmarkClient, Throwable, PostmarkEmailResponse] =
    ZIO.serviceWithZIO(_.sendEmail(email))

final case class PostmarkClientLive(config: PostmarkConfig) extends PostmarkClient:
  def sendEmail(email: PostmarkEmail): Task[PostmarkEmailResponse] =
    for
      _ <- Console.printLine("Email sent")
      emailBody = Map(
        "from"          -> config.from,
        "to"            -> "",
        "templateAlias" -> email.templateAlias,
        "templateModel" -> email.templateModel,
      )
      headers = Map(
        "X-Postmark-Server-Token" -> config.serverToken,
        "Content-Type"            -> "application/json",
        "Accept"                  -> "application/json",
      )
      res <- ZIO.attempt(requests.post(config.url, data = emailBody.toJson, headers = headers))
      emailResponse = res.text().fromJson[PostmarkEmailResponse]
      _ <- Console.printLine(pprint.log(emailResponse))
    yield PostmarkEmailResponse("", OffsetDateTime.now(), UUID.randomUUID(), 0, "")

object PostmarkClientLive:
  val layer: ZLayer[PostmarkConfig, Throwable, PostmarkClient] =
    ZLayer.fromFunction(PostmarkClientLive.apply)
