package drawing.shapes.mutable
import drawing.core.Helper.Point
import drawing.core.{Canvas, Helper, Vector2D}

final case class Oval(override var x: Double, override var y: Double, var width: Double, var height: Double)(implicit override val canvas: Canvas) extends MutableShape {
  /**
    * Defines the path to draw the oval onto the canvas
    * without actually printing it
    */
  private def getPath = {
    canvas.context.beginPath
    getPoints(steps = 50).foreach(p => canvas.context.lineTo(p._1, p._2))
  }

  /**
    * Returns the position of the center of the shape
    *
    * @return the position of the center of the shape
    */
  override def center: Point = (x, y)

  /**
    * Actually draws the shape on the canvas
    *
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
    *
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
  override def getNormalEdgesVectors = {
    val points = getPoints(steps = 30).dropRight(1)
    (points zip Helper.rotateLeft(points, 1)).map{ case (a1, a2) => Vector2D.fromPoints(a1, a2).normalize}
  }

  /**
    * Returns a list containing vectors from the center of the shape to each one of its corners
    * @return a list of center-corner vectors for every corner
    */
  override def getCornerVectors = getPoints(steps = 30).map{ case (x, y) => Vector2D(x, y)}

  /**
    * Computes the points that will allow us to create the oval
    * Found here: https://math.stackexchange.com/questions/426150/what-is-the-general-equation-of-the-ellipse-that-is-not-in-the-origin-and-rotate/434482
    * @param steps the number of steps we want (big -> precise but heavy computational time)
    * @return the points that represent the oval
    */
  private def getPoints(steps: Int) = {
    val centerAngle = 2*Math.PI / steps
    (0 to steps).map(_*centerAngle).map(t => (x + width*Math.cos(t)*Math.cos(angle.toRadians) - height*Math.sin(t)*Math.sin(angle.toRadians), y + width*Math.cos(t)*Math.sin(angle.toRadians) + height*Math.sin(t)*Math.cos(angle.toRadians))).toList
  }
}
