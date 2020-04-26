package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class GameControlTest extends FlatSpec with Matchers {

  "checkHit" should "be correct" in {
    val gameControl = GameControl(100)

    gameControl.checkHit(0, 0, Color.red) shouldBe true
    gameControl.checkHit(400, 400, Color.red) shouldBe false
  }
}
