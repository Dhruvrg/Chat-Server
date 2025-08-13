package chat

import chat.clientUtils.{BroadCast, CreateGroup, DirectMessage, GroupMessage}

import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import java.net.Socket
import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex
import scala.util.{Failure, Success}

object Client {

  def main(args: Array[String]): Unit = {
      val client = new Socket("localhost", 11111)
      val in = new BufferedReader(new InputStreamReader(client.getInputStream))
      val out = new PrintWriter(client.getOutputStream, true)
      val SystemIn = System.in
      val userInput = new BufferedReader(new InputStreamReader(SystemIn))
      var users: List[String] = List()
      var flag: Boolean = true

      def cleanup(): Unit = {
        try {
          in.close()
          out.close()
          client.close()
          userInput.close()
        } catch {
          case e: Exception => println(s"Error during cleanup: ${e.getMessage}")
        }
        System.exit(0)
      }

      def serverFail(): Unit = {
        try {
          flag = false
          in.close()
          out.close()
          client.close()
          userInput.close()
        } catch {
          case e: Exception => println(s"Error during cleanup: ${e.getMessage}")
        }
        System.exit(0)
      }

      println("Connected to the server. Type your messages:")

      try {
        var username = ""
        println("Enter your username :")
        if ({ username = userInput.readLine(); username != null})
          out.println(username)

        var usersString = "";
        if ({ usersString = in.readLine(); usersString != null && usersString.size > 0})
          users = usersString.split(",").toList

        implicit val ec: ExecutionContext = ExecutionContext.global

        Future {
          var serverMessage = ""
          val connectedPattern: Regex = """Server: (.+) is connected""".r
          val disconnectedPattern: Regex = """Server: (.+) is disconnected""".r

          while ( {
            serverMessage = in.readLine(); serverMessage != null
          }) {
            println(serverMessage)
            serverMessage match {
              case connectedPattern(name) => users = name :: users
              case disconnectedPattern(name) => users = users.filter(_ != name)
              case _ =>
            }
          }
        }.onComplete {
          case Success(_) => println("Server message reading stopped.")
          case Failure(ex) => serverFail()
        }

        println("Welcome to the server! Type 'exit' to disconnect.")

        def optionSelection: Unit = {
          var option = "f"
          println("Choose an option")
          println("a :- Direct Message\nb :- BroadCast Message\nc :- Group Message\nd :- Create Group\ne :- List User\nf :- Exit")
          while (flag){
            if (SystemIn.available() > 0) {
                option = userInput.readLine()
                flag = false
            }
          }
          flag = true
          option match {
            case "a" => DirectMessage(out, userInput, users)(); optionSelection
            case "b" => BroadCast(out, userInput)(); optionSelection
            case "c" => GroupMessage(out, userInput)(); optionSelection
            case "d" => CreateGroup(out, userInput, users)(); optionSelection
            case "e" => out.println("e"); println(users); optionSelection
            case "f" => out.println("f"); println("exit")
            case _ => out.println("_"); println("Choose something else"); optionSelection
          }
        }
        optionSelection

      }
    catch {
      case e: Exception => println("An error occurred:- " + e.getMessage)
    } finally {
        cleanup()
        println("Disconnected from the server.")
    }
  }
}
