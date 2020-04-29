package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class GameControlTest extends FlatSpec with Matchers {

  "checkHit" should "be correct" in {
    val gameControl = GameControl(600)

    gameControl.checkHit(400, 400, Color.red) shouldBe false
  }
}
