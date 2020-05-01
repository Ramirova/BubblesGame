package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class GameControlTest extends FlatSpec with Matchers {

  "checkHit for coordinates not in grid" should "be false" in {
    val gameControl = GameControl(600)

    gameControl.checkHit(400, 400, Color.red) shouldBe false
  }

  "checkHit for coordinates in grid" should "be true" in {
    val gameControl = GameControl(600)

    gameControl.checkHit(100, 100, Color.red) shouldBe true
  }
}
