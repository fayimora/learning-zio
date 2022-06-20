package com.fayimora.learningzio.github

import zio.*

object GithubApiMain extends ZIOAppDefault:
  def run =
    val configLayer: ULayer[MyConfig] = ZLayer.succeed(MyConfig("https://api.github.com"))

    val program: ZIO[GithubApi, Throwable, GithubUser] =
      for {
        u <- GithubApi.getUser("fayimora")
        _ <- ZIO.succeed(pprint.log(u))
      } yield u

    program.provide(configLayer, GithubApiLive.layer)

  // def oldRun =
  //   val url = "https://api.github.com/users/fayimora"
  //   for
  //     res <- ZIO.attempt(requests.get(url))
  //     //      _ <- printLine(res.text())
  //     user <- ZIO.fromEither(res.text().fromJson[GithubUser])
  //     //       _ <- printLine(user)
  //     repoResponse <- ZIO.attempt(requests.get(user.reposUrl))
  //     repos <- ZIO.fromEither(repoResponse.text().fromJson[List[GithubRepo]])
  //     _ <- printLine(repos.head)
  //   yield ()
