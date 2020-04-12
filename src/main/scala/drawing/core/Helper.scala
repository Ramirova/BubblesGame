package drawing.core

/**
  * A simple helper object for geometric computations
  */
object Helper {
  type Point = (Double, Double)

  /**
    * Computes the euclidean distance between two points in a 2D plan
    * @param p1 the first point
    * @param p2 the second point
    * @return the euclidean distance between p1 and p2
    */
  def distanceBetween(p1: Point, p2: Point) = {
    val (x1, y1) = p1
    val (x2, y2) = p2
    Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))
  }

  /**
    * Rotates a list to the left by the given number of iterations
    * @param l the list to be rotated
    * @param i the number of iterations
    * @return the list rotated by i iteration to the left
    */
  def rotateLeft[A](l: List[A], i: Int): List[A] = {
    val size = l.size
    l.drop(i % size) ++ l.take(i % size)
  }
}
