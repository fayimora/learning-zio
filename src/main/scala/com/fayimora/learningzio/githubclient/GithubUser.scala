package com.fayimora.learningzio.github

import zio.json.jsonField
import zio.json.JsonDecoder
import zio.json.DeriveJsonDecoder

//@jsonMemberNames(SnakeCase)
case class GithubUser(
    login: String,
    id: Int,
    url: String,
    @jsonField("avatar_url") avatarUrl: String,
    @jsonField("repos_url") reposUrl: String,
  )

object GithubUser:
  implicit val decoder: JsonDecoder[GithubUser] = DeriveJsonDecoder.gen[GithubUser]
