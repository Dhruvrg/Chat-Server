import scala.collection.mutable

@main
def main(): Unit = {
  println("Hello world!")
  var groups: mutable.Map[String, List[String]] = mutable.Map()
  groups += ("rbl" -> List("ujjwal", "dhruv"))
  groups += ("pbl" -> List("dhruv", "vinay"))
  groups += ("abl" -> List("jay", "vinay"))

  groups.map((group) => println(group))

  groups = groups.map((group) => (group._1 -> group._2.filter((user) => user != "dhruv")))

  groups.map((group) => println(group))
}

