package drawing.core

import org.scalajs.dom
import org.scalajs.dom.html.{Canvas => JSCanvas}
import org.scalajs.dom.raw.{HTMLCanvasElement, KeyboardEvent, MouseEvent}
import org.scalajs.dom.{CanvasRenderingContext2D, document}

import scala.collection.mutable.LinkedHashSet
import drawing.shapes.mutable.MutableShape

/**
  * The canvas class represents the drawing area of a web page.
  * It is uniquely identified by the provided id and has a width and a height.
  * Moreover, it uses internally a ListBuffer to store the list of the shapes to draw.
  * @param element HTML element representing the canvas
  * @param shapes a list of shapes to by drawn into the canvas
  */
class Canvas(val element: HTMLCanvasElement, var shapes: LinkedHashSet[MutableShape] = LinkedHashSet.empty) {
  /**
    * And here is the drawing context (in which we will actually draw)
    */
  lazy val context = element.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  /**
    * Returns the width of the element
    */
  val width = element.width

  /**
    * Returns the height of the element
    */
  val height = element.height

  /**
    * X-coordinate (respectively t0 the canvas) of the mouse
    */
  var mouseX: Double = 0

  /**
    * Y-coordinate (respectively to the canvas) of the mouse
    */
  var mouseY: Double = 0

  /**
    * A set containing the keyboard keys that are currently pressed
    */
  var keysDown: Set[String] = Set()

  /**
    * This clears the canvas
    */
  def clear = context.clearRect(0, 0, width, height)

  /**
    * Clears the canvas and redraw all the shapes
    */
  def update = {
    clear
    shapes.filter(_.showing).foreach(_.draw(this))
  }
}

object Canvas{
  /**
    * Creates a canvas from its direct HTML element
    * @param element the HTML element representing the canvas
    * @return a canvas object
    */
  def apply(element: HTMLCanvasElement) = sensify(new Canvas(element))

  /**
    * Creates a canvas from its direct HTML element and assigning it the given size
    * @param element the HTML element representing the canvas
    * @param width the desired width
    * @param height the desired height
    * @return a canvas object
    */
  def apply(element: HTMLCanvasElement, width: Int, height: Int) = {
    element.width = width
    element.height = height
    sensify(new Canvas(element))
  }

  /**
    * Creates a canvas from an existing HTML element id
    * @param id the id of the element representing the canvas
    * @param width the desired width
    * @param height the desired height
    * @return a canvas object
    */
  def apply(id: String, width: Int, height: Int): Canvas =  {
    // Get the canvas and sets its size
    val c = document.getElementById(id).asInstanceOf[JSCanvas]
    c.height = height
    c.width = width

    sensify(new Canvas(c))
  }

  /**
    * Make a canvas sensitive to possible human interactions
    * @param canvas the canvas to be sensified
    * @return updated canvas with possible human interactions
    */
  private def sensify(canvas: Canvas): Canvas = {
    // Keeps up to date the coordinates of the mouse (relatively to the canvas)
    canvas.element.onmousemove = { (e: MouseEvent) =>
      canvas.mouseX = e.clientX - canvas.element.getBoundingClientRect().left
      canvas.mouseY = e.clientY - canvas.element.getBoundingClientRect().top
    }

    // Keep up to date the set of currently pressed keyboard keys
    dom.window.onkeydown = { (e: KeyboardEvent) =>
      canvas.keysDown += e.key
    }

    dom.window.onkeyup = {( e:KeyboardEvent) =>
      canvas.keysDown -= e.key
    }

    // Redirects the onMouseDown, onMouseUp, onMouseOver events to the shapes
    canvas.element.onmousedown = _ => canvas.shapes.filter(_.contains(canvas.mouseX, canvas.mouseY)).foreach(_.onMouseDown.apply())
    canvas.element.onmouseup = _ => canvas.shapes.filter(_.contains(canvas.mouseX, canvas.mouseY)).foreach(_.onMouseUp.apply())

    // Returns the canvas
    canvas
  }
}
