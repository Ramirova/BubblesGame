package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class BubbleTest extends FlatSpec with Matchers {

  "x offset in even and odd rows" should "be calculated right" in {
    val oddRowBubble = Bubble(0, 0, Color.red, 10)
    val evenRowBubble = Bubble(1, 0, Color.red, 10)
    val oddRowOffsetIsCorrect = oddRowBubble.xCord == 20
    val evenRowOffsetIsCorrect = oddRowBubble.xCord == 10
    oddRowOffsetIsCorrect shouldBe true
    evenRowOffsetIsCorrect shouldBe true
  }

  "y coordinate" should "be correct" in {
    val bubble = Bubble(1, 1, Color.red, 10)
    val yIsCorrect = bubble.yCord == 30
    yIsCorrect shouldBe true
  }
}