package com.test.finagle

import java.net.Socket
import java.io.{InputStreamReader, BufferedReader}
import com.twitter.scrooge.ThriftStruct
import org.apache.thrift.protocol.TBinaryProtocol
import java.util

object PlainTcp {
   def main(args: Array[String]) {
     val bytes = Array[Byte](-128,1,0,1,0,0,0,3,97,100,100,0,0,0,0,8,0,1,0,0,0,10,8,0,2,0,0,0,19,0)
     val socket = new Socket("finagle-test.herokuapp.com", 80)
     val out = socket.getOutputStream
     try {

       val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
       try {
         out.write(bytes)
         out.flush()
         var response: Array[Byte] = Array[Byte]()
         var i = in.read()
         while(i != -1) {
           response = response :+ i.asInstanceOf[Byte]
           i = in.read()
         }
         println(response.mkString(","))
         println(decodeResponse(response))
       } finally {
         in.close()
       }
     } finally {
       out.close()
     }

   }

   protected def decodeResponse[T <: ThriftStruct](resBytes: Array[Byte]) = {
     new String(resBytes)
   }
 }
