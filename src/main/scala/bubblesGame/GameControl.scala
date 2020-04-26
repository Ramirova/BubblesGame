package bubblesGame

import doodle.core.Color

case class GameControl(screenWidth: Int) {

  val ballRadius = 20.0
  val minimumBubblesToBlow = 3
  var indicator = 0

  val gridControl: GridControl = GridControl(screenWidth, ballRadius)

  def checkHit(x: Double, y: Double, color: Color): Boolean = {
    gridControl.findHittingBubble(x, y) match {
      case Some(hittingBubble) => {
        handleHit(x, y, hittingBubble, color)
        return true
      }
      case None => return false
    }
    false
  }

  def handleHit(x: Double, y: Double, toBubble: Bubble, color: Color): Unit = {
    val hitFromLeftSide = x < toBubble.xCord
    val newBubbleCoordinates = if (hitFromLeftSide) leftHit(toBubble) else rightHit(toBubble)
    val coordinates = flipOfTheWallIfNeeded(newBubbleCoordinates)

    gridControl.addBubble(coordinates, color) match {
      case Some(newBubble) => blowNeighboursIfNeeded(newBubble)
      case None =>
    }
  }

  def flipOfTheWallIfNeeded(gridLocation: GridLocation): GridLocation = {
    if (gridLocation.column < 0) return GridLocation(gridLocation.row, gridLocation.column + 1)
    if (gridLocation.column + 1 > gridControl.grid(0).length) return GridLocation(gridLocation.row, gridLocation.column - 1)
    gridLocation
  }

  private def rightHit(toBubble: Bubble) = GridLocation(toBubble.row + 1, toBubble.column + (if (toBubble.shifted) 1 else 0))
  private def leftHit(toBubble: Bubble) = GridLocation(toBubble.row + 1, toBubble.column + (if (toBubble.shifted) 0 else -1))

  def blowNeighboursIfNeeded(newBubble: Bubble): Unit = {
    var toBlow = Vector[Bubble]()
    var reviewed = Vector[Bubble]()
    val blowingColor = newBubble.color
    checkNeighbours(newBubble)

    if (toBlow.length >= minimumBubblesToBlow) gridControl.blowBubbles(toBlow)
    else {
      toBlow = Vector[Bubble]()
      reviewed = Vector[Bubble]()
    }

    def checkNeighbours(bubble: Bubble): Unit = {
      val neighbours = gridControl.getNeighbours(bubble)
      for (neighbour <- neighbours) {
        if (neighbour.color == blowingColor && !reviewed.contains(neighbour)) {
          toBlow = toBlow :+ neighbour
          reviewed = reviewed :+ neighbour
          checkNeighbours(neighbour)
        } else {
          reviewed = reviewed :+ neighbour
        }
      }
    }
  }
}