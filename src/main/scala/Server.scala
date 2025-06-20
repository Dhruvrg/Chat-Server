import java.net.{InetAddress, ServerSocket, Socket}
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Server {
  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.global

    try {
      val server = new ServerSocket(11111) // This will bind the port 11111
      println("Server is running...")
      val connectedClients: mutable.Map[String, Socket] = mutable.Map()
      val groups: mutable.Map[String, List[String]] = mutable.Map()

      while (true) {
        val clientSocket = server.accept() // Accept client connection
        Future {
          handleClient(clientSocket)
        }
      }

        def handleClient(clientSocket: Socket): Unit = {
          val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
          val out = new PrintWriter(clientSocket.getOutputStream, true)
          var username = ""

          try {
            if ({username = in.readLine(); username != null}) {
              connectedClients.map((s) => new PrintWriter(s._2.getOutputStream, true).println(s"Server: ${username} is connected"))
              out.println(connectedClients.map((ele) => ele._1).mkString(","))
              connectedClients += (username -> clientSocket)
            }
            println(s"${username} is connected")

            def optionSelection: Unit = {
              var option = ""
              if ({ option = in.readLine(); option != null}){
                option match {
                  case "a" => directMessage
                  case "b" => broadCast
                  case "c" => println("group message")
                  case "d" => createGroup
                  case "e" => optionSelection
                  case "f" =>
                  case _ => println("Choose something else"); optionSelection
                }
              }
            }
            optionSelection

            def createGroup = {
              var groupName = ""
              if ({ groupName = in.readLine(); groupName != null}){}
              var groupMembersString = in.readLine()
              if ({ groupMembersString = in.readLine(); groupMembersString != null}){}

//              println(s"$username create a group:- $groupName")
//              out.println(s"Server: Group Created :- $groupName")
//              val users: List[String] = groupMembersString.split(",").toList
//              users.map((name) => new PrintWriter(connectedClients(name).getOutputStream, true).println(s"$username added you in group($groupName)"))
//              groups += (groupName -> (username :: users))

              optionSelection
            }

            def broadCast = {
              var message = ""
              while ({ message = in.readLine(); message != null && message != "exit" }) {
                connectedClients.filter((s) => s._1 != username).map((s) => new PrintWriter(s._2.getOutputStream, true).println(s"BroadCasr($username): $message"))
                println(s"$username broadcast a message:- $message")
              }
              optionSelection
            }

            def directMessage = {
              var receiver = in.readLine()
              var message = ""
              while ({ message = in.readLine(); message != null && message != "exit" }) {
                println(s"$username sends $message to $receiver")
                new PrintWriter(connectedClients(receiver).getOutputStream, true).println(s"$username: $message") // Echo the message back to the client
              }

              optionSelection
            }
          } catch {
            case e: Exception => println(s"Error: ${e.getMessage}")
          } finally {
            println(s"${username} is disconnected")
            connectedClients.filter((s) => s._1 != username).map((s) => new PrintWriter(s._2.getOutputStream, true).println(s"Server: ${username} is disconnected"))
            connectedClients -= username
            in.close()
            out.close()
            clientSocket.close()
          }
      }
      // server.close()
    } catch {
      case e: Exception => println("An error occurred: " + e.getMessage)
    }
  }
}
