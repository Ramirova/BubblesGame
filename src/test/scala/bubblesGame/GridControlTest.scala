package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class GridControlTest extends FlatSpec with Matchers {

  "findHittingBubble" should "be correct" in {
    val gridControl = GridControl(200, 20)

    gridControl.findHittingBubble(100, 100).isDefined shouldBe true
    gridControl.findHittingBubble(400, 400).isDefined shouldBe false
  }

  "addBubble" should "be correct" in {
    val gridControl = GridControl(200, 20)

    gridControl.addBubble(GridLocation(0, 0), Color.red).isDefined shouldBe true
    gridControl.addBubble(GridLocation(0, 0), Color.red).get.color == Color.red shouldBe true
    gridControl.addBubble(GridLocation(100, 0), Color.red).isDefined shouldBe false
  }

  "getNeighbours" should "be correct" in {
    val gridControl = GridControl(200, 20)
    val centerBubble = Bubble(2, 2, Color.red, 10)
    val edgeBubble = Bubble(2, 2, Color.red, 10)

    gridControl.getNeighbours(centerBubble).length == 8 shouldBe true
    gridControl.getNeighbours(edgeBubble).length == 3 shouldBe false
  }


  "blowBubbles" should "be correct" in {
    val gridControl = GridControl(200, 20)
    val bubblesToBlow = Vector[Bubble](gridControl.grid(2)(2).get, gridControl.grid(2)(2).get)
    gridControl.blowBubbles(bubblesToBlow)

    gridControl.grid(2)(2).isDefined shouldBe false
    gridControl.grid(2)(3).isDefined shouldBe false
  }
}