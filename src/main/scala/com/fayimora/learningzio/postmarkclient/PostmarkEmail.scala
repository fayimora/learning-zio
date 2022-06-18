package com.fayimora.learningzio.postmarkclient

import java.time.OffsetDateTime
import java.util.UUID


final case class PostmarkEmail(from: String,
                              to: String,
                              templateAlias: Option[String],
                              templateModel: Map[String, String])

final case class PostmarkEmailResponse(to: String,
                                      submittedAt: OffsetDateTime,
                                      messageID: UUID,
                                      errorCode: Int,
                                      message: String)