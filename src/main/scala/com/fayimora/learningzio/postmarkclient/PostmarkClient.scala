package com.fayimora.learningzio.postmarkclient

import zio.*
import java.time.OffsetDateTime
import java.util.UUID
import com.fayimora.learningzio.PostmarkConfig


trait PostmarkClient:
  def sendEmail(email: PostmarkEmail): ZIO[PostmarkClient, Throwable, PostmarkEmailResponse]

object PostmarkClient:
  def sendEmail(email: PostmarkEmail): ZIO[PostmarkClient, Throwable, PostmarkEmailResponse] =
    ZIO.serviceWithZIO(_.sendEmail(email))

final case class PostmarkClientLive(config: PostmarkConfig) extends PostmarkClient:
  def sendEmail(email: PostmarkEmail): Task[PostmarkEmailResponse] =
    for
      _ <- Console.printLine("Email sent")
    yield PostmarkEmailResponse("", OffsetDateTime.now(), UUID.randomUUID(), 0, "")

object PostmarkClientLive:
    val layer: ZLayer[PostmarkConfig, Throwable, PostmarkClient] =
      ZLayer.fromFunction(PostmarkClientLive.apply)