package drawing.animation

import drawing.shapes.Shape

trait Animation {
  def next: Animation
  def shape: Shape
}
