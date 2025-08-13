package chat.clientUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case object BroadCast {
  
  def execute(out: PrintWriter, userInput: BufferedReader): Unit = {
    out.println("b")
    var message = ""
    while ({ message = userInput.readLine(); message != null && message != "exit" })
      out.println(message)

    out.println("exit")
  }

  def unapply(str: String): Option[String] =
    if (str == "b") Some(str) else None
}
