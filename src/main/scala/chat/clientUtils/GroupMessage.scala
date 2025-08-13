package chat.clientUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case object GroupMessage {
  
  def execute(out: PrintWriter, userInput: BufferedReader): Unit = {
    out.println("c")
    println("Choose group:-")
    var groupName = ""
    if ({ groupName = userInput.readLine(); groupName != null })
      out.println(groupName)

    println("message:-")
    var message = ""
    while ({ message = userInput.readLine(); message != null && message != "exit" })
      out.println(message)

    out.println("exit")
  }

  def unapply(str: String): Option[String] =
    if (str == "c") Some(str) else None
}