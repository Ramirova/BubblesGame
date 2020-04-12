package drawing.game

import bubblesGame.GameControl
import org.scalajs.dom.ext.KeyValue._
import doodle.core.Color
import scala.concurrent.duration._
import drawing.shapes.mutable.{Background, Circle, Image, Oval, Rectangle}
import drawing.core.TimeControls._

object BubblesGame extends Helper {
  override def run: Unit = {
    // Background
    Image(Background.DarkSpace, 0, 0, width, height)

    // Game state
    var ballFlies = false

    val gameControl = GameControl(width)



    def drawGrid(): Array[Option[Oval]] = {
      gameControl.grid.flatten.collect {
        case Some(bubble) =>
          val bubbleImage = Circle(bubble.xCord, bubble.yCord, bubble.radius)
          bubbleImage.fillColor = bubble.color
          Some(bubbleImage)
      }
    }

    // Setup bubbles grid
    val grid = drawGrid()

    def clearGrid(): Unit = {
      grid.collect {
        case Some(bubble) =>
          canvas.shapes.remove(bubble)
          bubble.hide()
      }
    }

    // Define the bar
    val bar = Rectangle(width/2, height - gameControl.ballRadius, 100, 5)
    bar.fillColor = Color.greenYellow
    var dy = -Math.cos(bar.angle.toRadians)
    var dx = Math.sin(bar.angle.toRadians)

    def getNewBall: Oval = {
      val newBubble = gameControl.getNewBubble
      val newBall = Circle(width/2, height - newBubble.radius, newBubble.radius)
      newBall.fillColor = newBubble.color

      dy = -Math.cos(bar.angle.toRadians)
      dx = Math.sin(bar.angle.toRadians)

      newBall
    }

    // Define flying ball
    var ball = getNewBall

    // GAME
    every(20.milliseconds){_ => {
      if (gameControl.checkHit(ball.center._1, ball.center._2, ball.fillColor)) {
        canvas.shapes.remove(ball)
        ball.hide()
        ball = getNewBall
        clearGrid()
        drawGrid()
        ballFlies = false

        val b = gameControl.indicator
        val indicator = Circle(50, 50, b * 10)
        indicator.fillColor = Color.red
      }

      if (ballFlies) manageEdges()

      if (canvas.keysDown.contains(ArrowLeft)) {
        bar.turnLeft(1)
        updateBallAcceleration()
      }

      if (canvas.keysDown.contains(ArrowRight)) {
        bar.turnRight(1)
        updateBallAcceleration()
      }

      if (canvas.keysDown.contains(Spacebar))
        ballFlies = true
    }}

    def updateBallAcceleration() {
      if (!ballFlies) {
        dy = -Math.cos(bar.angle.toRadians)
        dx = Math.sin(bar.angle.toRadians)
      }
    }

    def manageEdges(): Unit = {
      if (ball.touchesEdge) {
        if (ball.y <= 5 || ball.y >= height-5) {
          dy = -dy

        }
        if (ball.x <= 5 || ball.x >= width-5) {
          dx = -dx
        }
      }
      ball.move(dx * 12, dy * 12)
    }
  }
}
