package com.fayimora.learningzio.postmarkclient

import com.fayimora.learningzio.AppConfig
import zio.*, Console.*

object PostmarkMain extends ZIOAppDefault:
  def run =
    for
      appConf <- AppConfig.config
      _ <- printLine(pprint.log(s"Starting $appConf"))
    yield ()
