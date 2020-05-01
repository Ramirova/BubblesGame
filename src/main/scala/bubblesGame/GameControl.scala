package bubblesGame

import doodle.core.Color

case class GameControl(screenWidth: Int) {

  val ballRadius = 20.0
  val minimumBubblesToBlow = 3
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

  private def handleHit(x: Double, y: Double, toBubble: Bubble, color: Color): Unit = {
    val hitFromLeftSide = x < toBubble.xCord
    val newBubbleCoordinates = if (hitFromLeftSide) leftHit(toBubble) else rightHit(toBubble)
    val coordinates = flipOfTheWallIfNeeded(newBubbleCoordinates)

    gridControl.addBubble(coordinates, color) match {
      case Some(newBubble) => blowNeighboursIfNeeded(newBubble)
      case None =>
    }
  }

  private def flipOfTheWallIfNeeded(gridLocation: GridLocation): GridLocation = {
    if (gridLocation.column < 0) return GridLocation(gridLocation.row, gridLocation.column + 1)
    if (gridLocation.column + 1 > gridControl.grid(0).length) return GridLocation(gridLocation.row, gridLocation.column - 1)
    gridLocation
  }

  private def rightHit(toBubble: Bubble) = GridLocation(toBubble.row + 1, toBubble.column + (if (toBubble.shifted) 1 else 0))
  private def leftHit(toBubble: Bubble) = GridLocation(toBubble.row + 1, toBubble.column + (if (toBubble.shifted) 0 else -1))

  private def blowNeighboursIfNeeded(newBubble: Bubble): Unit = {
    val blowingColor = newBubble.color
    lazy val (toBlow, _) = checkNeighbours(newBubble, List.empty, List.empty)

    if (toBlow.length >= minimumBubblesToBlow) gridControl.blowBubbles(toBlow.toVector)

    def checkNeighbours(bubble: Bubble, toBlow: List[Bubble], reviewed: List[Bubble]): (List[Bubble], List[Bubble]) = {
      val neighbours = gridControl.getNeighbours(bubble)
      neighbours.foldLeft((toBlow, reviewed)) { (acc, neighbour) =>
        val (blow, rev) = acc
        if (neighbour.color == blowingColor && !reviewed.contains(neighbour)) {
          val (blowU, revU) = checkNeighbours(neighbour, List(neighbour), neighbour :: rev)
          (blow ++ blowU, rev ++ revU)
        } else {
          (blow, neighbour :: rev)
        }
      }
    }
  }
}