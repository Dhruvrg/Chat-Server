package chat.serverUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case object BroadCast {
  def apply(in: BufferedReader, connectedClients: mutable.Map[String, Socket], username: String): Unit = {
    var message = ""
    while ({message = in.readLine(); message != null && message != "exit"}) {
      connectedClients.filter((s) => s._1 != username)
        .foreach((s) => new PrintWriter(s._2.getOutputStream, true).println(s"BroadCast($username): $message"))
      println(s"$username broadcast a message:- $message")
    }
  }

  def unapply(str: String): Option[String] =
    if (str == "b") Some(str) else None
}
