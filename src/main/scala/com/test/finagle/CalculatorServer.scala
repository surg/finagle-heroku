package com.test.finagle

import com.twitter.util.Future
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import org.apache.thrift.protocol.TBinaryProtocol
import org.jboss.netty.channel.local.{DefaultLocalServerChannelFactory, LocalAddress}
import javax.servlet.{ServletContextEvent, ServletContextListener}
import com.test.finagle.CalculatorService.FutureIface


class CalculatorServer extends ServletContextListener {

  override def contextDestroyed(p1: ServletContextEvent) {}

  override def contextInitialized(p1: ServletContextEvent) {
    val processor = new FutureIface {
      override def add(num1: Int, num2: Int): Future[Int] = {
        println(s"Processing request $num1 + $num2")
        Future {
          num1 + num2
        }
      }
    }

    val service = new CalculatorService.FinagledService(processor, new TBinaryProtocol.Factory())
    ServerBuilder()
      .channelFactory(new DefaultLocalServerChannelFactory())
      .bindTo(new LocalAddress("myServer"))
      .codec(ThriftServerFramedCodec())
      .name("tcpServer")
      .build(service)
  }
}
