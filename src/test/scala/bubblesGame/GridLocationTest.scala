package bubblesGame

import doodle.core.Color
import org.scalatest.{FlatSpec, Matchers}

class  GridLocationTest extends FlatSpec with Matchers {

  "GridLocation" should "store right coordinated" in {
    val gridLocation = GridLocation(2, 3)
    gridLocation.row == 2 shouldBe true
    gridLocation.column == 3 shouldBe true
  }
}
