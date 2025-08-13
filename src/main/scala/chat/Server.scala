package chat

import chat.serverUtils.{BroadCast, CloseClient, CreateGroup, DirectMessage, GroupMessage, ListUsers}

import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import java.net.{ServerSocket, Socket}
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

object Server {
  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.global
    val connectedClients: mutable.Map[String, Socket] = mutable.Map()
    val groups: mutable.Map[String, List[String]] = mutable.Map()

    try {
      val server = new ServerSocket(11111) 
      println("Server is running...")

      while (true) {
        val clientSocket = server.accept()
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
              if ({ option = in.readLine(); option != null }) {
                option match {
                  case DirectMessage(str) => DirectMessage.execute(in, connectedClients, username); optionSelection
                  case BroadCast(str) => BroadCast.execute(in, connectedClients, username); optionSelection
                  case GroupMessage(str) => GroupMessage.execute(in, out, groups, connectedClients, username); optionSelection
                  case CreateGroup(str) => CreateGroup.execute(in, out, groups, connectedClients, username); optionSelection
                  case ListUsers(str) => optionSelection
                  case CloseClient(str) => // exit
                  case _   => println("Choose something else"); optionSelection
                }
              }
            }
            optionSelection

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
    } finally {
      connectedClients.map((s) => new PrintWriter(s._2.getOutputStream, true).println(s"Server: Server is disconnected"))
    }
  }
}
