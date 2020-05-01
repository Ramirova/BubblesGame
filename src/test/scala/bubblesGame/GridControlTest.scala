package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class GridControlTest extends FlatSpec with Matchers {

  "grid size" should "be correct depending on screen width" in {
    val smallGridControl = GridControl(200, 20)
    val bigGridControl = GridControl(600, 20)

    smallGridControl.grid.length shouldBe 17
    smallGridControl.grid(0).length shouldBe 5

    bigGridControl.grid.length shouldBe 21
    bigGridControl.grid(0).length shouldBe 15
  }

  "findHittingBubble for bubble in grid" should "return true" in {
    val gridControl = GridControl(200, 20)

    gridControl.findHittingBubble(10, 10).isDefined shouldBe true
  }

  "findHittingBubble for bubble not in grid" should "be false" in {
    val gridControl = GridControl(200, 20)

    gridControl.findHittingBubble(400, 400).isDefined shouldBe false
  }

  "addBubble" should "be correct" in {
    val gridControl = GridControl(200, 20)

    gridControl.addBubble(GridLocation(0, 0), Color.red).isDefined shouldBe true
    gridControl.addBubble(GridLocation(0, 0), Color.red).get.color == Color.red shouldBe true
    gridControl.addBubble(GridLocation(100, 0), Color.red).isDefined shouldBe false
  }

  "getNeighbours for bubble deep in grid" should "return 6 neighbours" in {
    val gridControl = GridControl(600, 20.0)
    val centerBubble = Bubble(2, 2, Color.red, 20)

    gridControl.getNeighbours(centerBubble).length shouldBe 6
  }

  "getNeighbours for bubble on edge" should "return 4 neighbours" in {
    val gridControl = GridControl(600, 20.0)
    val edgeBubble = Bubble(0, 1, Color.red, 20)

    gridControl.getNeighbours(edgeBubble).length shouldBe 4
  }

  "getNeighbours for bubble in corner" should "return 2 neighbours" in {
    val gridControl = GridControl(600, 20.0)
    val сornerBubble = Bubble(0, 0, Color.red, 20)

    gridControl.getNeighbours(сornerBubble).length shouldBe 2
  }

  "blowBubbles" should "blow bubbles in grid" in {
    val gridControl = GridControl(600, 20)
    val bubblesToBlow = Vector[Bubble](gridControl.grid(0)(0).get, gridControl.grid(1)(1).get)
    gridControl.blowBubbles(bubblesToBlow)

    gridControl.grid(0)(0).isDefined shouldBe false
    gridControl.grid(1)(1).isDefined shouldBe false
  }
}