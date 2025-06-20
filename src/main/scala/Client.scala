import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.matching.Regex

object Client {
  def main(args: Array[String]): Unit = {
      val client = new Socket("localhost", 11111)
      val in = new BufferedReader(new InputStreamReader(client.getInputStream))
      val out = new PrintWriter(client.getOutputStream, true)
      val userInput = new BufferedReader(new InputStreamReader(System.in))
      var users: List[String] = List()
      println("Connected to the server. Type your messages:")

    try {
      var username = ""
      println("Enter your username :")
      if ({ username = userInput.readLine(); username != null})
        out.println(username)

      // List of users

      var usersString = "";
      if ({ usersString = in.readLine(); usersString != null && usersString.size > 0})
        users = usersString.split(",").toList

      new Thread(new Runnable {
        override def run(): Unit = {
          var serverMessage = ""
          val connectedPattern: Regex = """Server: (.+) is connected""".r
          val disconnectedPattern: Regex = """Server: (.+) is disconnected""".r

          while ({serverMessage = in.readLine(); serverMessage != null}) {
            println(serverMessage)
            serverMessage match {
              case connectedPattern(name) => users = name :: users
              case disconnectedPattern(name) => users = users.filter(_ != name)
              case _ =>
            }
          }
        }
      }).start()
      println("Welcome to the server! Type 'exit' to disconnect.")

      def optionSelection: Unit = {
        var option = ""
        println("Choose an option")
        println("a :- Direct Message\nb :- BroadCast Message\nc :- Group Message\nd :- Creat Group\ne :- List User\nf :- Exit")
        if ({ option = userInput.readLine(); option != null}){
          option match {
            case "a" => directMessage
            case "b" => broadCast
            case "c" => out.println("c"); println("group message")
            case "d" => out.println("d"); createGroup
            case "e" => out.println("e"); println(users); optionSelection
            case "f" => out.println("f"); println("exit")
            case _ => out.println("_"); println("Choose something else"); optionSelection
          }
        }
      }
      optionSelection

      def createGroup = {
        out.println("d")
        println("Name your Group:-")
        var groupName = ""
        if ({groupName = userInput.readLine(); groupName != null})
          out.println(groupName)
          
        println(users)
        var groupMembersString = ""
        println("Choose who you want to add in this group, name must be separated by ,")
        if ({groupMembersString = userInput.readLine(); groupMembersString != null}) 
          out.println(groupMembersString)
        optionSelection
      }
      
      def broadCast = {
        out.println("b")
        var message = ""
        while ( {message = userInput.readLine(); message != null && message != "exit"})
          out.println(message)

        out.println("exit")
        optionSelection
      }

      def directMessage = {
        out.println("a")
        println("Choose who you want to DM?")
        println(users)

        var receiver = ""
        if ( {receiver = userInput.readLine(); receiver != null})
          out.println(receiver)
          
        var message = ""
        while ({ message = userInput.readLine(); message != null && message != "exit" })
          out.println(message)

        out.println("exit")
        optionSelection
      }
    }
    catch {
      case e: Exception => println("An error occurred: " + e.getMessage)
    } finally {
      in.close()
      out.close()
      client.close()
      userInput.close()
      println("Disconnected from the server.")
    }
  }
}