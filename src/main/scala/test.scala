import scala.util.matching.Regex

object test {
  def main(args: Array[String]): Unit = {new Thread(new Runnable {
    override def run(): Unit = {
      var serverMessage = "Server: Ujjwal is connected"
      val pattern: Regex = """Server: (.+) is connected""".r
      println(pattern)
      serverMessage match {
        case pattern(_) => println("Matched pattern!")
        case _ => println(serverMessage)
      }
    }
  }).start()
  println("Welcome to the server! Type 'exit' to disconnect.")}
}

