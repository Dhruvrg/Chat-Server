package chat.serverUtils

case object ListUsers {
  
  def unapply(str: String): Option[String] =
    if (str == "e") Some(str) else None
}
