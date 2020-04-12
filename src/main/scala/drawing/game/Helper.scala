package drawing.game

import drawing.core.Canvas

trait Helper {
  val width = 600
  val height = 800

  implicit val canvas = Canvas("canvas", width, height)
  def run: Unit
}
