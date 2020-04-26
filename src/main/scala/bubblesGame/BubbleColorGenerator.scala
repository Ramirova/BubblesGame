package bubblesGame

import doodle.core.Color

import scala.util.Random

object BubbleColorGenerator {
  def generateColor: Color = {
    val x: Int = Random.nextInt(5)
    x match {
      case 0 => Color.blue
      case 1 => Color.green
      case 2 => Color.pink
      case 3 => Color.red
      case _ => Color.yellow
    }
  }
}
