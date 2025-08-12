package chat.serverUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case class BroadCast(in: BufferedReader, connectedClients: mutable.Map[String, Socket], username: String) {
  def apply(): Unit = {
    var message = ""
    while ({message = in.readLine(); message != null && message != "exit"}) {
      connectedClients.filter((s) => s._1 != username)
        .foreach((s) => new PrintWriter(s._2.getOutputStream, true).println(s"BroadCast($username): $message"))
      println(s"$username broadcast a message:- $message")
    }
  }
}
