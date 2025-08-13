package chat.serverUtils

case object CloseClient {

  def unapply(str: String): Option[String] =
    if (str == "f") Some(str) else None
}
