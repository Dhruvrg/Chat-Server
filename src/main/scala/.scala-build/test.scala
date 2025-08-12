import scala.collection.mutable

object test extends App{
  val username = "dhruv"
  val groups: mutable.Map[String, List[String]] = mutable.Map()
  groups += "rbl" -> List("dhruv", "harsh", "ujjwal")
  groups += "abl" -> List("vinay", "dhruv", "yash")
  groups += "pbl" -> List("vinay", "ujjwal")
  groups += "gbl" -> List()
  println(groups.filter((ele) => (ele._2.filter(_ == username).size) == 1).map((ele) => ele._1).mkString(","))
}
