package chat.serverUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case class GroupMessage(in: BufferedReader, out: PrintWriter, groups: mutable.Map[String, List[String]], connectedClients: mutable.Map[String, Socket], username: String) {
  def apply(): Unit = {
    out.println(groups.filter((ele) => ele._2.count(_ == username) == 1).map(_._1).mkString(","))
    val groupName = in.readLine()

    var message = ""
    val users: List[String] = groups(groupName).filter(_ != username)
    while ({ message = in.readLine(); message != null && message != "exit" }) {
      users.foreach((user) => new PrintWriter(connectedClients(user).getOutputStream, true).println(s"Group($groupName):- $username :- $message"))
      println(s"Group($groupName):- $username :- $message")
    }
  }
}