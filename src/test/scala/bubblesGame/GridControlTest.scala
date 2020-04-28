package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class GridControlTest extends FlatSpec with Matchers {

  "findHittingBubble" should "be correct" in {
    val gridControl = GridControl(200, 20)

    gridControl.findHittingBubble(10, 10).isDefined shouldBe true
    gridControl.findHittingBubble(400, 400).isDefined shouldBe false
  }

  "addBubble" should "be correct" in {
    val gridControl = GridControl(200, 20)

    gridControl.addBubble(GridLocation(0, 0), Color.red).isDefined shouldBe true
    gridControl.addBubble(GridLocation(0, 0), Color.red).get.color == Color.red shouldBe true
    gridControl.addBubble(GridLocation(100, 0), Color.red).isDefined shouldBe false
  }

  "getNeighbours" should "be correct" in {
    val gridControl = GridControl(600, 20.0)
    val centerBubble = Bubble(2, 2, Color.red, 20)
    val edgeBubble = Bubble(0, 1, Color.red, 20)

    gridControl.getNeighbours(centerBubble).length shouldBe 8
    gridControl.getNeighbours(edgeBubble).length shouldBe 5
  }

  "blowBubbles" should "be correct" in {
    val gridControl = GridControl(600, 20)
    val bubblesToBlow = Vector[Bubble](gridControl.grid(0)(0).get, gridControl.grid(1)(1).get)
    gridControl.blowBubbles(bubblesToBlow)

    gridControl.grid(0)(0).isDefined shouldBe false
    gridControl.grid(1)(1).isDefined shouldBe false
  }
}