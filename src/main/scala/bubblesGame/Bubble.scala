package bubblesGame
import doodle.core.Color

import scala.language.experimental.macros

case class Bubble(row: Int, column: Int, color: Color, radius: Double) {

  def xCord: Double = {
    val xOffset = if (row % 2 != 0) radius else 0
    column * radius * 2 + radius + xOffset
  }

  val yCord: Double = row * radius * 2 + radius
  val shifted: Boolean = row % 2 != 0
  var willBlow: Boolean = false
  var reviewed: Boolean = false
}

object Bubble {
  def apply(row: Int, column: Int, color: Color, radius: Double): Bubble = new Bubble(row, column, color, radius)

  def apply(row: Int, column: Int, radius: Double): Bubble = {
    Bubble(row, column, BubbleColorGenerator.generateColor, radius)
  }
}