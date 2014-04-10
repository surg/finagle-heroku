package com.test.finagle

import com.twitter.util.{Future, Await}
import com.twitter.finagle.builder.ClientBuilder
import java.net.InetSocketAddress
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.conversions.time._
import org.jboss.netty.channel.socket.http.HttpTunnelingClientSocketChannelFactory
import com.twitter.finagle.netty3.Netty3Transporter
import org.jboss.netty.channel.socket.{SocketChannel, ClientSocketChannelFactory}
import org.jboss.netty.channel.ChannelPipeline

object CalculatorClient {
  def main(args: Array[String]) {
    val host = "finagle-test.herokuapp.com"
    val port = 80

    val factory = new HttpTunnelingClientSocketChannelFactory(Netty3Transporter.channelFactory.asInstanceOf[ClientSocketChannelFactory]) {
      override def newChannel(pipeline: ChannelPipeline): SocketChannel = {
        val channel = super.newChannel(pipeline)
        channel.getConfig.setOption("serverName", host)
        channel
      }
    }

    val service =
      ClientBuilder()
        .hosts(new InetSocketAddress(host, port))
        .codec(ThriftClientFramedCodec())
        .hostConnectionLimit(100)
        .tcpConnectTimeout(50.seconds)
        .requestTimeout(30.seconds)
        .channelFactory(factory)
        .daemon(true)
        .build()

    val client = new CalculatorService.FinagledClient(service, new TBinaryProtocol.Factory())

    val f: Future[Int] = client.add(10, 32)
    println(Await.result(f))

    println("Hooray!")
  }

}
