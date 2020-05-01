package drawing.game

import bubblesGame.{BubbleColorGenerator, GameControl}
import org.scalajs.dom.ext.KeyValue._
import doodle.core.Color

import scala.concurrent.duration._
import drawing.shapes.mutable.{Background, Circle, Image, Oval, Rectangle}
import drawing.core.TimeControls._

object BubblesGame extends Helper {

  // Constants
  val updateRate = 20
  val ballToEdgesMargin = 5
  val ballSpeedRate = 12

  case class BallAcceleration(dx: Double, dy: Double)

  val gameControl: GameControl = GameControl(width)

  override def run: Unit = {
    Image(Background.StarWars, 0, 0, width, height) // Background

    val grid = drawGrid() // Bubbles grid

    val bar = Rectangle(width/2, height - gameControl.ballRadius, 100, 5) // Direction arrow
    bar.fillColor = Color.greenYellow

    // Game state
    var ballFlies = false
    var ball = getNewBall // First flying ball
    var acceleration = BallAcceleration(Math.sin(bar.angle.toRadians), -Math.cos(bar.angle.toRadians))

    // Game process
    every(updateRate.milliseconds){_ => {

      if (gameControl.checkHit(ball.center._1, ball.center._2, ball.fillColor)) {
        canvas.shapes.remove(ball)
        ball.hide()
        ball = getNewBall
        clearGrid(grid)
        drawGrid()
        ballFlies = false
      }

      if (ballFlies) { acceleration = moveBall(ball, acceleration) }

      else { // Direction calibration
        if (canvas.keysDown.contains(ArrowLeft)) { bar.turnLeft(1) }
        if (canvas.keysDown.contains(ArrowRight)) { bar.turnRight(1) }
        acceleration = BallAcceleration(Math.sin(bar.angle.toRadians), -Math.cos(bar.angle.toRadians))
      }

      if (canvas.keysDown.contains(Spacebar)) ballFlies = true
    }}
  }

  def drawGrid(): Vector[Option[Oval]] = {
    gameControl.gridControl.grid.flatten.collect {
      case Some(bubble) =>
        val bubbleImage = Circle(bubble.xCord, bubble.yCord, bubble.radius)
        bubbleImage.fillColor = bubble.color
        Some(bubbleImage)
    }
  }

  def clearGrid(grid: Vector[Option[Oval]]): Unit = {
    grid.collect {
      case Some(bubble) =>
        canvas.shapes.remove(bubble)
        bubble.hide()
    }
  }

  def getNewBall: Oval = {
    val newBall = Circle(width / 2, height - gameControl.ballRadius, gameControl.ballRadius)
    newBall.fillColor = BubbleColorGenerator.generateColor
    newBall
  }

  def moveBall(ball: Oval, acceleration: BallAcceleration): BallAcceleration = {
    var dx = acceleration.dx
    var dy = acceleration.dy

    if (ball.touchesEdge) {
      if (ball.y <= ballToEdgesMargin || ball.y >= height - ballToEdgesMargin) { dy = -dy }
      if (ball.x <= ballToEdgesMargin || ball.x >= width - ballToEdgesMargin) { dx = -dx }
    }

    ball.move(dx * ballSpeedRate, dy * ballSpeedRate)
    BallAcceleration(dx, dy)
  }
}
