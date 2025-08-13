package chat.clientUtils

import java.io.PrintWriter

case object ListUsers {

  def execute(out: PrintWriter, users: List[String]): Unit = {
    out.println("e")
    println(users)
  }
  
  def unapply(str: String): Option[String] =
    if (str == "e") Some(str) else None
}
