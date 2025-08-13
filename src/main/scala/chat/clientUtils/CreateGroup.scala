package chat.clientUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case object CreateGroup {
  def apply(out: PrintWriter, userInput: BufferedReader, users: List[String]): Unit = {
    out.println("d")
    println("Name your Group:-")

    var groupName = ""
    if ({ groupName = userInput.readLine(); groupName != null })
      out.println(groupName)

    println(users)
    var groupMembersString = ""
    println("Choose who you want to add in this group, name must be separated by ,")
    if ({ groupMembersString = userInput.readLine(); groupMembersString != null })
      out.println(groupMembersString)
  }

  def unapply(str: String): Option[String] =
    if (str == "d") Some(str) else None
}