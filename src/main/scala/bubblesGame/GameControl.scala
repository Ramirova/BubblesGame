package bubblesGame

import doodle.core.Color
import drawing.shapes.mutable.Circle

import scala.util.Random

case class GameControl(screenWidth: Int) {

  val ballRadius = 20.0
  val emptyLinesAllowed = 16

  val grid: Array[Array[Option[Bubble]]] = {
    val (rows, columns) = calculateGridSize(screenWidth)
    Array.tabulate(rows, columns)(getBubbleInGrid(columns, rows))
  }

  def checkHit(x: Double, y: Double, color: Color): Boolean = {
    val (row, column) = getGridLocation(x, y)

    if (row < grid.length & column < grid(0).length) {
      grid(row)(column) match {
        case Some(toBubble) => {
          handleHit(x, y, toBubble, color)
          true
        }
        case None => false
      }
    } else {
      false
    }
  }

  def handleHit(x: Double, y: Double, toBubble: Bubble, color: Color): Unit = {
    val leftSide = x < toBubble.xCord

    var (row, column) = if (leftSide) leftHit(toBubble) else rightHit(toBubble)

    if (column < 0) column += 1
    if (column + 1 > grid(0).length) column -= 1

    if (row < grid.length) {
      val newBubble = Bubble(row, column, color, ballRadius)
      grid(row)(column) = Option(newBubble)
      checkAndBlow(newBubble)
    }
  }

  def rightHit(toBubble: Bubble): (Int, Int) = (toBubble.row + 1, toBubble.column + (if (toBubble.shifted) 1 else 0))

  def leftHit(toBubble: Bubble): (Int, Int) = (toBubble.row + 1, toBubble.column + (if (toBubble.shifted) 0 else -1))

  def getGridLocation(x: Double, y: Double): (Int, Int) = {
    val row = Math.floor(y / 2).toInt / ballRadius.toInt
    val xOffset = if (row % 2 != 0) ballRadius else 0
    val column = Math.floor((x - xOffset) / 2).toInt / ballRadius.toInt
    (row, column)
  }

  var indicator = 0

  def checkAndBlow(newBubble: Bubble): Unit = {
    var blowingBubblesCount = 0
    val blowingColor = newBubble.color
    checkNeighbours(newBubble)
    if (blowingBubblesCount > 2) {
      blowBubbles()
    } else {
      cancelBlowing()
    }

    def checkNeighbours(bubble: Bubble): Unit = {
      val neighbours = getNeighbours(bubble)
      indicator = neighbours.length
      for (neighbour <- neighbours) {
        if (neighbour.color == blowingColor && !neighbour.reviewed) {
          blowingBubblesCount += 1
          neighbour.willBlow = true
          neighbour.reviewed = true
          checkNeighbours(neighbour)
        }
        neighbour.reviewed = true
      }
    }
  }

  def blowBubbles(): Unit = {
    grid.flatten.collect {
      case Some(bubble) =>
        bubble.reviewed = false
        if (bubble.willBlow) {
          grid(bubble.row)(bubble.column) = None
        }
    }
  }

  def cancelBlowing(): Unit = {
    grid.flatten.collect {
      case Some(bubble) =>
        bubble.willBlow = false
        bubble.reviewed = false
    }
  }

  def getNeighbours(bubble: Bubble): Array[Bubble] = {
    var result: Array[Bubble] = Array[Bubble]()
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

  def getNewBubble: Bubble = {
    generateBubble(-1, -1)
  }

  private def calculateGridSize(screenWidth: Int): (Int, Int) = {
    val columns = screenWidth / ballRadius / 2
    val rows = columns / 3
    (rows.toInt + emptyLinesAllowed, columns.toInt)
  }

  private def getBubbleInGrid(gridWidth: Int, gridHeight: Int)(row: Int, column: Int): Option[Bubble] = {
    if (row >= (gridHeight - emptyLinesAllowed) || (row % 2 != 0 & column == (gridWidth - 1))) return None
    Option(generateBubble(row, column))
  }

  private def generateBubble(row: Int, column: Int): Bubble = {

    val x: Int = Random.nextInt(5)

    val color = x match {
      case 0 => Color.blue
      case 1 => Color.green
      case 2 => Color.pink
      case 3 => Color.red
      case _ => Color.yellow
    }

    Bubble(row, column, color, ballRadius)
  }
}