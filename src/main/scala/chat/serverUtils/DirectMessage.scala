package chat.serverUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case object DirectMessage {
  def apply(in: BufferedReader, connectedClients: mutable.Map[String, Socket], username: String): Unit = {
    val receiver = in.readLine()
    var message = ""
    while ({ message = in.readLine(); message != null && message != "exit" }) {
      println(s"$username sends $message to $receiver")
      new PrintWriter(connectedClients(receiver).getOutputStream, true).println(s"$username: $message") // Echo the message back to the client
    }
  }

  def unapply(str: String): Option[String] =
    if (str == "a") Some(str) else None
}