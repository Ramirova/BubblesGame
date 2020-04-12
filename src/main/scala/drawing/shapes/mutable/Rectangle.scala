package drawing.shapes.mutable

import doodle.core.Angle

import drawing.core.Helper.Point
import drawing.core.{Canvas, Vector2D}

final case class Rectangle(override var x: Double, override var y: Double, var width: Double, var height: Double)(implicit override val canvas: Canvas) extends MutableShape {

  /**
    * Describes the rotation of the shape with respect to its width
    * @return a pair containing the x and y values of the rotation
    */
  private def xRotationTransform = (height*Math.cos(angle.toRadians), height*Math.sin(angle.toRadians))

  /**
    * Describes the rotation of the shape with respect to its height
    * @return a pair containing the x and y values of the rotation
    */
  private def yRotationTransform = (width*Math.cos((angle-Angle.degrees(90)).toRadians), width*Math.sin((angle-Angle.degrees(90)).toRadians))

  /**
    * Returns the top right corner of the rectangle
    * @return (x, y) coordinates of the top right corner
    */
  private def bottomRightCorner = (x + xRotationTransform._1, y + xRotationTransform._2)

  /**
    * Returns the bottom left corner of the rectangle
    * @return (x, y) coordinates of the bottom left corner
    */
  private def topLeftCorner = (x + yRotationTransform._1, y + yRotationTransform._2)

  /**
    * Returns the bottom right corner of the rectangle
    * @return (x, y) coordinates of the bottom right corner
    */
  private def topRightCorner = (topLeftCorner._1 + xRotationTransform._1, topLeftCorner._2 + xRotationTransform._2)

  /**
    * Defines the path to draw the rectangle onto the canvas
    * without actually printing it
    */
  private def getPath = {
    canvas.context.beginPath
    canvas.context.moveTo(x, y)

    // Draw the bottom line
    canvas.context.lineTo(bottomRightCorner._1, bottomRightCorner._2)

    // Draw the right line
    canvas.context.lineTo(bottomRightCorner._1 + yRotationTransform._1, bottomRightCorner._2 + yRotationTransform._2)
    canvas.context.moveTo(x, y)

    // Draw the left line
    canvas.context.lineTo(topLeftCorner._1, topLeftCorner._2)

    // Draw the top line
    canvas.context.lineTo(topRightCorner._1, topRightCorner._2)
  }

  /**
    * Returns the position of the center of the shape
    * @return the position of the center of the shape
    */
  override def center: Point = (x + width/2, y + height/2)

  /**
    * Actually draws the shape on the canvas
    * @param canvas the canvas to draw on
    */
  override def draw(implicit canvas: Canvas): Unit = {
    getPath
    canvas.context.fillStyle = fillColor.toCanvas
    canvas.context.fill
    canvas.context.strokeStyle = borderColor.toCanvas
    canvas.context.stroke
  }

  /**
    * Returns true iff the point (x, y) belongs to the shape
    * @param x the x coordinate of the point
    * @param y the y coordinate of the point
    */
  override def contains(x: Double, y: Double): Boolean = {
    getPath
    canvas.context.isPointInPath(x, y)
  }

  /**
    * Returns a list containing a normal vector for each edge of the shape
    * @return a list of normal vectors
    */
  override def getNormalEdgesVectors = List(
    Vector2D.fromPoints((x, y), topLeftCorner),
    Vector2D.fromPoints((x, y), bottomRightCorner),
    Vector2D.fromPoints(topRightCorner, topLeftCorner),
    Vector2D.fromPoints(topRightCorner, bottomRightCorner)).map(_.normalize)

  /**
    * Returns a list containing vectors from the center of the shape to each one of its corners
    * @return a list of center-corner vectors for every corner
    */
  override def getCornerVectors = List(
    Vector2D(x, y),
    Vector2D(topLeftCorner._1, topLeftCorner._2),
    Vector2D(topRightCorner._1, topRightCorner._2),
    Vector2D(bottomRightCorner._1, bottomRightCorner._2)
  )
}