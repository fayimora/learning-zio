package com.fayimora.learningzio.postmarkclient

import java.time.OffsetDateTime
import java.util.UUID
import zio.json.JsonDecoder
import zio.json.DeriveJsonDecoder

final case class PostmarkEmail(
    from: String,
    to: String,
    templateAlias: Option[String],
    templateModel: Map[String, String],
  )

final case class PostmarkEmailResponse(
    to: String,
    submittedAt: OffsetDateTime,
    messageID: UUID,
    errorCode: Int,
    message: String,
  )

object PostmarkEmail:
  implicit val decoder: JsonDecoder[PostmarkEmail] = DeriveJsonDecoder.gen[PostmarkEmail]

object PostmarkEmailResponse:
  implicit val decoder: JsonDecoder[PostmarkEmailResponse] =
    DeriveJsonDecoder.gen[PostmarkEmailResponse]
