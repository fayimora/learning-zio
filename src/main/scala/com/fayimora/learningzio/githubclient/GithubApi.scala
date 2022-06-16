package com.fayimora.learningzio.github

import zio.*
import zio.Console.printLine
import zio.json.*

import GithubUser.*
case class GithubApiError(message: String) extends Throwable
case class MyConfig(baseUrl: String)

trait GithubApi:
  def getUser(username: String): Task[GithubUser]

object GithubApi:
  def getUser(username: String): ZIO[GithubApi, Throwable, GithubUser] =
    ZIO.serviceWithZIO(_.getUser(username))

case class GithubApiLive(config: MyConfig) extends GithubApi:
  def getUser(username: String): Task[GithubUser] =
    ZIO
      .attempt(requests.get(s"${config.baseUrl}/users/$username"))
      .flatMap(res => ZIO.fromEither(res.text().fromJson[GithubUser]))
      .orElseFail(GithubApiError("Something went wrong"))

object GithubApiLive:
  val layer: ZLayer[MyConfig, Throwable, GithubApi] =
    ZLayer.fromFunction(GithubApiLive.apply)
