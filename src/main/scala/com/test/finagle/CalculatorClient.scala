package com.test.finagle

import com.twitter.util.{Future, Await}
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.Service
import java.net.InetSocketAddress
import com.twitter.finagle.thrift.{ThriftClientBufferedCodec, ThriftClientRequest}
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.conversions.time._
import com.twitter.finagle.http._
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.HttpMethod

object CalculatorClient {
  def main(args: Array[String]) {
    val host = "finagle-test.herokuapp.com"
    val port = 80
    val codec = ThriftClientBufferedCodec()

    val service: Service[ThriftClientRequest, Array[Byte]] =
      ClientBuilder()
        .hosts(new InetSocketAddress(host, port))
        .codec(codec)
        .hostConnectionLimit(100)
        .tcpConnectTimeout(50.seconds)
        .requestTimeout(30.seconds)
        .daemon(true)
        .build()
    val client = new CalculatorService.FinagledClient(service, new TBinaryProtocol.Factory())

    val f: Future[Int] = client.add(10, 19)
    println(Await.result(f))

    println("Hooray!")
  }

}
