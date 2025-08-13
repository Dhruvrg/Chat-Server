package chat.serverUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case object CreateGroup {
  def execute(in: BufferedReader, out: PrintWriter, groups: mutable.Map[String, List[String]], connectedClients: mutable.Map[String, Socket], username: String): Unit = {
    val groupName = in.readLine()
    val groupMembersString = in.readLine()

    println(s"$username create a group:- $groupName")
    out.println(s"Server: Group Created :- $groupName")
    val users: List[String] = groupMembersString.split(",").toList
    users.foreach((name) => new PrintWriter(connectedClients(name).getOutputStream, true).println(s"$username added you in group($groupName)"))
    groups += (groupName -> (username :: users))
  }

  def unapply(str: String): Option[String] =
    if (str == "d") Some(str) else None
}