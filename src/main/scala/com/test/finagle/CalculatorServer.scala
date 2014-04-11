package com.test.finagle

import com.twitter.util.Future
import util.Properties
import com.twitter.finagle.builder.ServerBuilder
import java.net.InetSocketAddress
import com.twitter.finagle.thrift.{ThriftServerFramedCodec, ThriftServerBufferedCodec}
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.conversions.time._
import com.test.finagle.CalculatorService.FutureIface
import java.util.logging.Logger


object CalculatorServer {

  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    val processor = new FutureIface {
      override def add(num1: Int, num2: Int): Future[Int] = {
        println(s"Processing request $num1 + $num2")
        Future {
          Thread.sleep(10000)
          num1 + num2
        }
      }
    }

    val service = new CalculatorService.FinagledService(processor, new TBinaryProtocol.Factory())

    ServerBuilder()
      .bindTo(new InetSocketAddress(port))
      .codec(ThriftServerFramedCodec())
      .name("ping")
      .writeCompletionTimeout(50 seconds)
      .readTimeout(50.seconds)
      .requestTimeout(30 seconds)
      .logChannelActivity(true)
      .logger(Logger.getLogger("CalculatorServer"))
      .build(service)
  }
}
