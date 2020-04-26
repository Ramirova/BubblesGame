package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class BubbleColorGeneratorTest extends FlatSpec with Matchers {

  "generateColor" should "generate random color" in {
    val generator = BubbleColorGenerator
    val result = generator.generateColor.isInstanceOf[Color]
    result shouldBe true
  }
}
