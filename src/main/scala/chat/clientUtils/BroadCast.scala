package chat.clientUtils

import java.io.{BufferedReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

case class BroadCast(out: PrintWriter, userInput: BufferedReader) {
  def apply(): Unit = {
    out.println("b")
    var message = ""
    while ({ message = userInput.readLine(); message != null && message != "exit" })
      out.println(message)

    out.println("exit")
  }
}
