package com.fayimora.learningzio

import sttp.client3.*
import sttp.client3.httpclient.zio.*

import zio.*, json.*
import sttp.capabilities.zio.ZioStreams
import sttp.capabilities.WebSockets

type SttpClient = SttpBackend[Task, ZioStreams & WebSockets]

case class HttpBin(
    data: String,
    origin: String,
    url: String,
    headers: Map[String, String],
  )

object HttpBin:
  implicit val decoder: JsonDecoder[HttpBin] = DeriveJsonDecoder.gen[HttpBin]

object SttpPlayground extends ZIOAppDefault:
  def run =
    // the service/environment way
    val request = basicRequest
      .body("Hello, world")
      .post(uri"https://httpbin.org/post?hello=world")

    send(request)
      .flatMap(res =>
        res.body match {
          case Left(err) => Console.printError(err)
          case Right(msg) =>
            val cc = msg.fromJson[HttpBin]
            Console.printLine(msg) *> Console.printLine(pprint.log(cc))
        }
      )
      .provide(HttpClientZioBackend.layer())

    // for-comprehension over the backend
    // for
    //   backend <- HttpClientZioBackend()
    //   _       <- Console.printLine("YOLO!")
    //   res <- basicRequest
    //     .body("Hello, world")
    //     .post(uri"https://httpbin.org/postg?hello=world")
    //     .send(backend)
    //   _ = res.body match
    //     case Left(err)  => pprint.log(s"oops: $err")
    //     case Right(msg) => pprint.log(s"msg is: $msg")
    // yield ()

    // flatMap over the backend
    // HttpClientZioBackend()
    //   .flatMap(backend =>
    //     basicRequest
    //       .body("Hello, world")
    //       .post(uri"https://httpbin.org/post?hello=world")
    //       .send(backend)
    //       .flatMap(res =>
    //         res.body match {
    //           case Left(err) => Console.printError(err)
    //           case Right(msg) =>
    //             val cc = msg.fromJson[HttpBin]
    //             Console.printLine(msg) *> Console.printLine(pprint.log(cc))
    //         }
    //       )
    //   )

    // Simple stuff with sync backend
    // val backend = HttpClientSyncBackend()
    // val response = basicRequest
    //   .body("Hello, world!")
    //   .post(uri"https://httpbin.org/post?hello=world")
    //   .send(backend)

    // pprint.log(response.body)
    // ZIO.unit
