package drawing.core

import scala.concurrent.duration._
import drawing.animation.Animation
import scala.concurrent.duration.Duration
import scalajs.js.timers._

/**
  * TimeControls are methods that allow to execute a certain portion of the code
  * in a specific timely context
  */
object TimeControls {

  /**
    * Executes the code immediately. Calls canvas.update in the end to update the drawing
    * of shapes that might have been modified in the main program.
    * @param f the portion of code we want to execute
    * @param canvas the canvas to draw onto
    */
  def ready(f: => Unit)(implicit canvas: Canvas): Unit = {
    f
    every(20.milliseconds){_ => canvas.update}
  }

  /**
    * Waits for a certain durations before executing the given portion of code
    * @param duration the waiting time until the code is executed
    * @param f the portion of code we want to execute
    * @param canvas the canvas to draw onto
    */
  def after(duration: Duration)(f: => Unit)(implicit canvas: Canvas): Unit ={
    setTimeout(duration.toMillis){
      f
      canvas.update
    }
  }

  /**
    * Executes a portion without ever stopping. Waits for a certain duration between two executions.
    * A waiting duration of 20.milliseconds gives pretty good results for animations in practice.
    * @param duration the duration we want to wait for
    * @param counter the current counter of the number of executions
    * @param f the portion of code we want to execute
    * @param canvas the canvas to draw onto
    */
  def every(duration: Duration, counter: Int = 0)(f: Int => Unit)(implicit canvas: Canvas): Unit ={
    setTimeout(duration.toMillis){
      f(counter)
      canvas.update
      every(duration, counter+1)(f)
    }
  }

  /**
    * Executes a portion without ever stopping. Waits for a certain duration between two executions.
    * A waiting duration of 20.milliseconds gives pretty good results for animations in practice.
    * This is a special version of every() that does not update the canvas automatically
    * @param duration the duration we want to wait for
    * @param counter the current counter of the number of executions
    * @param f the portion of code we want to execute
    * @param canvas the canvas to draw onto
    */
  def everyIncr(duration: Duration, counter: Int = 0)(f: Int => Unit)(implicit canvas: Canvas): Unit ={
    setTimeout(duration.toMillis){
      f(counter)
      everyIncr(duration, counter+1)(f)
    }
  }

  def everySeq(duration: Duration, animations: Seq[Animation], counter: Int = 0)(f: (Int, Seq[Animation]) => Seq[Animation]): Unit ={
    setTimeout(duration.toMillis){
      everySeq(duration, f(counter, animations), counter+1)(f)
    }
  }

  /**
    * Repeats the execution of a portion of code while a certain condition is verified. Waits for a certain duration
    * between two executions.
    * @param condition the stopping condition
    * @param waitingTime the duration we want to wait for
    * @param counter the current counter of the number of executions
    * @param f the portion of code we want to execute
    * @param canvas the canvas to draw onto
    */
  def repeatWhile(condition: => Boolean, waitingTime: Duration, counter: Int = 1)(f: Int => Unit)(implicit canvas: Canvas): Unit ={
    if(condition){
      f(counter)
      canvas.update
      setTimeout(waitingTime.toMillis){
        repeatWhile(condition, waitingTime, counter+1)(f)
      }
    }
  }

  /**
    * Repeats the execution of a portion of code for a fixed number of times. Waits for a certain duration
    * between two executions.
    * @param n the number of iterations
    * @param waitingTime the duration we want to wait for
    * @param f the portion of code we want to execute
    * @param canvas the canvas to draw onto
    */
  def repeat(n: Int, waitingTime: Duration)(f: Int => Unit)(implicit canvas: Canvas): Unit ={
    def iter(counter: Int): Unit ={
      if(counter < n){
        setTimeout(waitingTime.toMillis){
          f(counter)
          canvas.update
          iter(counter+1)
        }
      }
    }
    iter(0)
  }
}
