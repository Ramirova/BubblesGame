package drawing.test

object TestHelper {
  /**
    * Helper method to test for equality between Double values
    * @param x first value
    * @param y second value
    * @return true iff x and y are almost equal
    */
  def reasonablyEqual(x: Double, y: Double) = Math.abs(x - y) < 0.000001
}
