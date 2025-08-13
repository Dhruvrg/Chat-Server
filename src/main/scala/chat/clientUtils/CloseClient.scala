package chat.clientUtils

import java.io.PrintWriter

case object CloseClient {

  def execute(out: PrintWriter): Unit = {
    out.println("f") 
    println("exit")
  }
  
  def unapply(str: String): Option[String] =
    if (str == "f") Some(str) else None
}

