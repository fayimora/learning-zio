package com.fayimora.learningzio.github

import zio.json.jsonField
import zio.json.JsonDecoder
import zio.json.DeriveJsonDecoder

final case class GithubRepo(
    id: Int,
    name: String,
    owner: GithubUser,
    @jsonField("full_name") fullName: String,
    @jsonField("private") isPrivate: Boolean,
  )

object GithubRepo:
  implicit val repoDecoder: JsonDecoder[GithubRepo] = DeriveJsonDecoder.gen[GithubRepo]
