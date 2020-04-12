package drawing.core

import doodle.core.Angle
import org.scalatest.FunSuite

import drawing.test.TestHelper.reasonablyEqual

class Vector2DTest extends FunSuite {
  val vNull = Vector2D(0, 0)
  val vUnit = Vector2D(1, 0)
  val vDiagonal = Vector2D(1, 1)
  val v = Vector2D(3, 4)

  test("norm test"){
    assert(vNull.norm == 0)
    assert(vUnit.norm == 1)
    assert(vDiagonal.norm == Math.sqrt(2))
    assert(v.norm == 5)
  }

  test("normalize test"){
    assert(reasonablyEqual(vUnit.normalize.norm, 1))
    assert(reasonablyEqual(vDiagonal.normalize.norm, 1))
    assert(reasonablyEqual(v.normalize.norm, 1))
  }

  test("normal test"){
    assert(vNull.normal == Vector2D(0.0, -0.0))
    assert(vUnit.normal == Vector2D(0.0, -1.0))
    assert(vDiagonal.normal == Vector2D(1.0, -1.0))
    assert(v.normal == Vector2D(4.0, -3.0))
  }

  test("multipliedBy test"){
    assert(vNull.multipliedBy(2) == Vector2D(0.0, 0.0))
    assert(vUnit.multipliedBy(2) == Vector2D(2.0, 0.0))
    assert(vDiagonal.multipliedBy(2) == Vector2D(2.0, 2.0))
    assert(v.multipliedBy(2) == Vector2D(6.0, 8.0))
  }

  test("+ and - test"){
    val plusVect = Vector2D(1, 1)

    assert(vNull + plusVect == Vector2D(1.0, 1.0))
    assert(vUnit + plusVect == Vector2D(2.0, 1.0))
    assert(vDiagonal + plusVect == Vector2D(2.0, 2.0))
    assert(v + plusVect == Vector2D(4.0, 5.0))

    assert(vNull - plusVect == Vector2D(-1.0, -1.0))
    assert(vUnit - plusVect == Vector2D(0.0, -1.0))
    assert(vDiagonal - plusVect == Vector2D(0.0, 0.0))
    assert(v - plusVect == Vector2D(2.0, 3.0))
  }

  test("dot test"){
    val v2 = Vector2D(-5, 2)
    assert(v.dot(v2) == -7)
  }

  test("unary - test"){
    assert(-v == Vector2D(-3.0, -4.0))
  }

  test("angleWith test"){
    assert(reasonablyEqual(v.angleWith(v.normal).toRadians, Angle.degrees(90).toRadians))
    assert(reasonablyEqual(v.angleWith(v).toRadians, Angle.degrees(0).toRadians))
    assert(reasonablyEqual(vUnit.angleWith(vDiagonal).toRadians, Angle.degrees(45).toRadians))
  }

  test("projectedOn test"){
    val vTest = Vector2D(-2, 5)
    val (projection, test) = (v.projectedOn(vTest), Vector2D(-28.0/29, 70.0/29))

    assert(v.projectedOn(v.normal) == Vector2D(0, 0))
    assert(v.projectedOn(v) == v)
    assert(reasonablyEqual(projection.x, test.x) && reasonablyEqual(projection.y, test.y))
  }
}
