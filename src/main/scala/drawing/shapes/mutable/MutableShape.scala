package drawing.shapes.mutable

import doodle.core.{Angle, Color}

import drawing.core.Helper._
import drawing.core.{Canvas, Helper, Vector2D}
import drawing.shapes.Shape

/**
  * This is abstract class is meant to define the API relative to imperative style graphical programming.
  * It should be close to the WoofJS API and therefore relies on mutable elements.
  * Shapes are drawn into <canvas> tags using Scala.js.
  */
abstract class MutableShape(implicit val canvas: Canvas) extends Shape {
  var x: Double
  var y: Double
  var angle: Angle = Angle(0)
  var showing: Boolean = true
  var fillColor = Color.white
  var borderColor = Color.black
  var onMouseDown: () => Unit = () => {}
  var onMouseUp: () => Unit = () => {}

  canvas.shapes += this
  this.draw

  /**
    * Returns the position of the center of the shape
    * @return the position of the center of the shape
    */
  def center: Point

  /**
    * Moves the shapes of a distance d in its own direction
    *
    * @param d the distance to move the shape
    */
  final def move(d: Double): Unit = {
    val radAngle = angle.toRadians
    x += d*Math.cos(radAngle)
    y += d*Math.sin(radAngle)
  }

  /**
    * Moves the shapes of distance dx along the x axis and dx along the dy axis
    *
    * @param dx the distance along the x axis
    * @param dy the distance along the y axis
    */
  final def move(dx: Double, dy: Double): Unit = {
    x += dx
    y += dy
  }

  /**
    * Moves the shape to the point of given coordinates
    *
    * @param x the x coordinate of the new position
    * @param y the y coordinate of the new position
    */
  final def moveTo(x: Double, y: Double): Unit = {
    this.x = x
    this.y = y
  }

  /**
    * Rotates the shape to the left of the given angle (in degrees)
    * @param angle the angle (in degrees)
    */
  final def turnLeft(angle: Double): Unit = this.angle = this.angle - Angle.degrees(angle)

  /**
    * Rotates the shape to the right of the given angle (in degrees)
    * @param angle the angle (in degrees)
    */
  final def turnRight(angle: Double): Unit = this.angle = this.angle + Angle.degrees(angle)

  /**
    * Rotates the shape so that it points towards the given point
    * @param x the x coordinate of the point to be pointed
    * @param y the y coordinate of the point to be pointed
    */
  final def pointTowards(x: Double, y: Double): Unit = angle = Angle.radians(Math.atan2(y - this.y, x - this.x))

  /**
    * Rotates the shape so that it points towards another given shape
    * @param other the shape to be pointed
    */
  final def pointTowards(other: MutableShape): Unit = pointTowards(other.center._1, other.center._2)

  /**
    * Calculates the distance between the center of two shapes
    *
    * @param other the shape we want to calculate the distance to
    */
  final def distanceTo(other: MutableShape): Unit = distanceBetween((x, y), other.center)

  /**
    * Shows the shape on the screen
    */
  final def show() = showing = true

  /**
    * Hide the shape from the screen
    */
  final def hide() = showing = false

  /**
    * Actually draws the shape on the canvas
    * @param canvas the canvas to draw on
    */
  def draw(implicit canvas: Canvas): Unit

  /**
    * Returns true iff the point (x, y) belongs to the shape
    * @param x the x coordinate of the point
    * @param y the y coordinate of the point
    */
  def contains(x: Double, y: Double): Boolean

  /**
    * Returns a list containing a normal vector for each edge of the shape
    * @return a list of normal vectors
    */
  def getNormalEdgesVectors: List[Vector2D]

  /**
    * Returns a list containing vectors from the center of the shape to each one of its corners
    * @return a list of center-corner vectors for every corner
    */
  def getCornerVectors: List[Vector2D]

  /**
    * Decides whether two shapes touch each other or not (core of SAT algorithm)
    * @param other another shape
    * @return true if the two shapes touch each other, false otherwise
    */
  final def touches(other: MutableShape): Boolean = {
    val axis = getNormalEdgesVectors ::: other.getNormalEdgesVectors

    val projections = axis.map(a => (
      getCornerVectors.map(_.projectedOn(a).norm),
      other.getCornerVectors.map(_.projectedOn(a).norm)))

    projections.forall(norm => !(norm._2.min > norm._1.max || norm._2.max < norm._1.min))
  }

  /**
    * Decides whether the shape touches an edge of the canvas or not (inspired from SAT algorithm)
    * @return true if the shapes touches an edge of the canvas, false otherwise
    */
  final def touchesEdge: Boolean = {
    val axis = List((Vector2D(0, 1), canvas.height), (Vector2D(1, 0), canvas.width), (Vector2D(0, -1), 0), (Vector2D(-1, 0), 0))

    val projections = for{
      a <- axis
    } yield((getCornerVectors.map(_.projectedOn(a._1).signedNorm),a._2))

    projections.exists(v => {
      (v._1.max >= v._2 && v._1.min <= v._2)
    })
  }
}