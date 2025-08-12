package chat.serverUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case class DirectMessage(in: BufferedReader, connectedClients: mutable.Map[String, Socket], username: String) {
  def apply(): Unit = {
    val receiver = in.readLine()
    var message = ""
    while ({ message = in.readLine(); message != null && message != "exit" }) {
      println(s"$username sends $message to $receiver")
      new PrintWriter(connectedClients(receiver).getOutputStream, true).println(s"$username: $message") // Echo the message back to the client
    }
  }
}