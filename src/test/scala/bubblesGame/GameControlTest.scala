package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class GameControlTest extends FlatSpec with Matchers {

  "checkHit" should "be correct" in {
    val gameControl = GameControl(600)

    gameControl.checkHit(100, 100, Color.red) shouldBe true
    gameControl.checkHit(200, 200, Color.red) shouldBe false
  }
}
