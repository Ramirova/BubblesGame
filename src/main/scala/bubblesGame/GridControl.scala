package bubblesGame

import doodle.core.Color

case class GridLocation(row: Int, column: Int)

case class GridControl(screenWidth: Int, ballRadius: Double) {

  val emptyLinesAllowed = 16

  var grid: Vector[Vector[Option[Bubble]]] = {
    val (rows, columns) = calculateGridSize(screenWidth)
    Vector.tabulate(rows, columns)(getBubbleInGrid(columns, rows))
  }

  def findHittingBubble(x: Double, y: Double): Option[Bubble] = {
    val row = Math.floor(y / 2).toInt / ballRadius.toInt
    val xOffset = if (row % 2 != 0) ballRadius else 0
    val column = Math.floor((x - xOffset) / 2).toInt / ballRadius.toInt

    if (row < grid.length & column < grid(0).length & row >= 0 & column >= 0) {
      grid(row)(column)
    } else {
      None
    }
  }

  def addBubble(gridLocation: GridLocation, color: Color): Option[Bubble] = {
    if (gridLocation.row < grid.length) {
      val newBubble = Bubble(gridLocation.row, gridLocation.column, color, ballRadius)
      grid = grid.updated(gridLocation.row, grid(gridLocation.row).updated(gridLocation.column, Option(newBubble)))
      Option(newBubble)
    } else { None }
  }

  private def calculateGridSize(screenWidth: Int): (Int, Int) = {
    val columns = screenWidth / ballRadius / 2
    val rows = columns / 3
    (rows.toInt + emptyLinesAllowed, columns.toInt)
  }

  private def getBubbleInGrid(gridWidth: Int, gridHeight: Int)(row: Int, column: Int): Option[Bubble] = {
    if (row >= (gridHeight - emptyLinesAllowed) || (row % 2 != 0 & column == (gridWidth - 1))) return None
    Option(Bubble(row, column, radius = ballRadius))
  }

  def getNeighbours(bubble: Bubble): Vector[Bubble] = {
    var result: Vector[Bubble] = Vector[Bubble]()
    val maxR = grid.length - 1
    val maxC = grid(0).length - 1
    val (r, c) = (bubble.row, bubble.column)
    if (r > 0 && c > 0 && grid(r - 1)(c - 1).isDefined) { result = result ++ grid(r - 1)(c - 1) }
    if (r > 0 && grid(r - 1)(c).isDefined) { result = result ++ grid(r - 1)(c) }
    if (r > 0 && (c + 1 < maxC) && grid(r - 1)(c + 1).isDefined) { result = result ++ grid(r - 1)(c + 1) }
    if (c > 0 && grid(r)(c - 1).isDefined) { result = result ++ grid(r)(c - 1) }
    if (c + 1 < maxC && grid(r)(c + 1).isDefined) { result = result ++ grid(r)(c + 1) }
    if (r + 1 < maxR && c > 0 & grid(r + 1)(c - 1).isDefined) { result = result ++ grid(r + 1)(c - 1) }
    if (r + 1 < maxR && grid(r + 1)(c).isDefined) { result = result ++ grid(r + 1)(c) }
    if (r + 1 < maxR && (c + 1 < maxC) && grid(r + 1)(c + 1).isDefined) { result = result ++ grid(r + 1)(c + 1) }
    result
  }

  def blowBubbles(bubbles: Vector[Bubble]): Unit = {
    grid.flatten.collect {
      case Some(bubble) =>
        if (bubbles.contains(bubble)) {
          grid = grid.updated(bubble.row, grid(bubble.row).updated(bubble.column, None))
        }
    }
  }
}
