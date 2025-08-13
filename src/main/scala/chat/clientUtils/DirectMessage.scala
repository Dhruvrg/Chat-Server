package chat.clientUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case object DirectMessage {
  def apply(out: PrintWriter, userInput: BufferedReader, users: List[String]): Unit = {
    out.println("a")
    println("Choose who you want to DM?")
    println(users)

    var receiver = ""
    if ({ receiver = userInput.readLine(); receiver != null })
      out.println(receiver)

    var message = ""
    while ({ message = userInput.readLine(); message != null && message != "exit" })
      out.println(message)

    out.println("exit")
  }

  def unapply(str: String): Option[String] =
    if (str == "a") Some(str) else None
}