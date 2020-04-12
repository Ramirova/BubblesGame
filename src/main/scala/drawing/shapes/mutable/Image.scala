package drawing.shapes.mutable

import doodle.core.Angle
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLImageElement

import drawing.core.Helper.Point
import drawing.core.{Canvas, Vector2D}

final case class Image(imageURL: String, override var x: Double, override var y: Double, var width: Double, var height: Double)(implicit override val canvas: Canvas) extends MutableShape {
  /**
    * Is the image flipped vertically?
    */
  private var xFlipped = false

  /**
    * Is the image flipped horizontally?
    */
  private var yFlipped = false

  /**
    * Contains the actual HTML element to store the image
    */
  private lazy val image: HTMLImageElement = {
    val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
    image.src = imageURL
    image
  }

  /**
    * Describes the rotation of the shape with respect to its width
    *
    * @return a pair containing the x and y values of the rotation
    */
  private def xRotationTransform = (height * Math.cos((angle + Angle.degrees(90)).toRadians), height * Math.sin((angle + Angle.degrees(90)).toRadians))

  /**
    * Describes the rotation of the shape with respect to its height
    *
    * @return a pair containing the x and y values of the rotation
    */
  private def yRotationTransform = (width * Math.cos(angle.toRadians), width * Math.sin(angle.toRadians))

  /**
    * Returns the top right corner of the rectangle
    *
    * @return (x, y) coordinates of the top right corner
    */
  private def bottomRightCorner = (x + xRotationTransform._1, y + xRotationTransform._2)

  /**
    * Returns the bottom left corner of the rectangle
    *
    * @return (x, y) coordinates of the bottom left corner
    */
  private def topLeftCorner = (x + yRotationTransform._1, y + yRotationTransform._2)

  /**
    * Returns the bottom right corner of the rectangle
    *
    * @return (x, y) coordinates of the bottom right corner
    */
  private def topRightCorner = (topLeftCorner._1 + xRotationTransform._1, topLeftCorner._2 + xRotationTransform._2)

  /**
    * Describes a rectangle path containing the image so that we can detect its edges
    */
  private def getPath = {
    canvas.context.beginPath
    canvas.context.moveTo(x, y)

    // Draw the bottom line
    canvas.context.lineTo(bottomRightCorner._1, bottomRightCorner._2)

    // Draw the right line
    canvas.context.lineTo(bottomRightCorner._1 + yRotationTransform._1, bottomRightCorner._2 + yRotationTransform._2)
    canvas.context.moveTo(x, y)

    // Draw the left line
    canvas.context.lineTo(topLeftCorner._1, topLeftCorner._2)

    // Draw the top line
    canvas.context.lineTo(topRightCorner._1, topRightCorner._2)
  }

  /**
    * Returns the position of the center of the shape
    *
    * @return the position of the center of the shape
    */
  override def center: Point = (x + image.width / 2, y + image.height / 2)

  /**
    * Actually draws the shape on the canvas
    *
    * @param canvas the canvas to draw on
    */
  override def draw(implicit canvas: Canvas): Unit = {
    val (xCoef, xTranslate): (Double, Double) = if(xFlipped) (-1, width) else (1, 0)
    val (yCoef, yTranslate): (Double, Double) = if(yFlipped) (-1, height) else (1, 0)

    canvas.context.translate(x, y)
    canvas.context.rotate(angle.toRadians)
    canvas.context.scale(xCoef, yCoef)
    canvas.context.drawImage(image, -xTranslate, -yTranslate, width, height)
    canvas.context.setTransform(1, 0, 0, 1, 0, 0)
  }

  /**
    * Returns true iff the point (x, y) belongs to the shape
    *
    * @param x the x coordinate of the point
    * @param y the y coordinate of the point
    */
  override def contains(x: Double, y: Double): Boolean = {
    getPath
    canvas.context.isPointInPath(x, y)
  }

  /**
    * Flips the image vertically
    */
  def flipVertically = xFlipped = !xFlipped

  /**
    * Flips the image horizontally
    */
  def flipHorizontally = yFlipped = !yFlipped

  /**
    * Returns a list containing a normal vector for each edge of the shape
    * @return a list of normal vectors
    */
  override def getNormalEdgesVectors = List(
    Vector2D.fromPoints((x, y), topLeftCorner),
    Vector2D.fromPoints((x, y), bottomRightCorner),
    Vector2D.fromPoints(topRightCorner, topLeftCorner),
    Vector2D.fromPoints(topRightCorner, bottomRightCorner)).map(_.normal)

  /**
    * Returns a list containing vectors from the center of the shape to each one of its corners
    * @return a list of center-corner vectors for every corner
    */
  override def getCornerVectors = List(
    Vector2D(x, y),
    Vector2D(topLeftCorner._1, topLeftCorner._2),
    Vector2D(topRightCorner._1, topRightCorner._2),
    Vector2D(bottomRightCorner._1, bottomRightCorner._2)
  )
}

object Image{
  val Unicorn = "http://woofjs.com/docs/images/unicorn.png"
  val BB8 = "http://woofjs.com/docs/images/BB8.png"
  val Sushi = "./docs/images/sushi.png"
  val NyanCat = "http://woofjs.com/docs/images/nyancat.png"
  val Car = "http://woofjs.com/docs/images/car.png"
  val Pikachu = "http://woofjs.com/docs/images/pikachu.png"
  val Taco = "http://woofjs.com/docs/images/taco.png"
  val IceCream = "http://woofjs.com/docs/images/icecream.png"
  val Minecraft = "http://woofjs.com/docs/images/minecraft.png"
  val Mario = "http://woofjs.com/docs/images/mario.png"
  val Cupcake = "http://woofjs.com/docs/images/cupcake.png"
  val Snitch = "http://woofjs.com/docs/images/snitch.png"
  val Bird = "http://woofjs.com/docs/images/bird.png"
  val Puppy = "http://woofjs.com/docs/images/puppy.png"
  val Pusheen = "http://woofjs.com/docs/images/pusheen.png"
  val Spiderman = "http://woofjs.com/docs/images/spiderman.png"
  val Apple = "http://woofjs.com/docs/images/apple.png"
  val Spaceship = "http://woofjs.com/docs/images/space_ship.png"
  val CoolEmoji = "http://woofjs.com/docs/images/emoji-glasses.png"
  val LoveEmoji = "http://woofjs.com/docs/images/emoji.png"
  val LaughEmoji = "http://woofjs.com/docs/images/emojilaugh.png"
  val PartyEmoji = "http://woofjs.com/docs/images/emoji-party.png"
  val PuppyEmoji = "http://woofjs.com/docs/images/emojipuppy.png"
  val SillyEmoji = "http://woofjs.com/docs/images/emojieyes.jpg"
  val Dragon = "http://woofjs.com/docs/images/dragon.png"
  val Elephant = "http://woofjs.com/docs/images/elephant.png"
  val Soccer = "http://woofjs.com/docs/images/soccer.png"
  val Rocket = "http://woofjs.com/docs/images/rocket.png"
  val Crab = "http://woofjs.com/docs/images/crab.png"
  val Fish = "http://woofjs.com/docs/images/fish.png"
  val Fish2 = "http://woofjs.com/docs/images/squidchase-fish.png"
  val BottomPipe = "http://woofjs.com/docs/images/bottomPipe.png"
  val TopPipe = "http://woofjs.com/docs/images/topPipe.png"
  val Explosion = "http://woofjs.com/docs/images/explosion.png"
  val Bomb = "http://woofjs.com/docs/images/boss-bomb.png"
  val BossLaser = "http://woofjs.com/docs/images/boss-blaser.png"
  val BossShip = "http://woofjs.com/docs/images/boss-boss.png"
  val Laser = "http://woofjs.com/docs/images/boss-laser.png"
  val Ship = "http://woofjs.com/docs/boss-ship/explosion.png"
  val BossWarning = "http://woofjs.com/docs/images/boss-warning.jpg"
  val CandyBowl = "http://woofjs.com/docs/images/bubbles-bowl.png"
  val Candy = "http://woofjs.com/docs/images/bubbles-candy.png"
  val Girl = "http://woofjs.com/docs/images/bubbles-girl.png"
  val BubbleWand = "http://woofjs.com/docs/images/bubbles-wand.png"
  val Mint = "http://woofjs.com/docs/images/candyland-mint.png"
  val Lollipop = "http://woofjs.com/docs/images/candyland-obstacle.png"
  val Fly = "http://woofjs.com/docs/images/fly-fly.png"
  val Frog = "http://woofjs.com/docs/images/fly-frog.png"
  val Lilypad = "http://woofjs.com/docs/images/fly-lilypad.png"
  val Dove = "http://woofjs.com/docs/images/magic-dove.jpg"
  val Hat = "http://woofjs.com/docs/images/magic-hat.png"
  val PicnicBasket = "http://woofjs.com/docs/images/picnic-basket.png"
  val Cheese = "http://woofjs.com/docs/images/picnic-cheese.png"
  val Picnic = "http://woofjs.com/docs/images/picnic-eat.png"
  val Milk = "http://woofjs.com/docs/images/picnic-milk.png"
  val Sandwich = "http://woofjs.com/docs/images/picnic-sandwich.png"
  val Watermelon = "http://woofjs.com/docs/images/picnic-watermelon.png"
  val Boss = "http://woofjs.com/docs/images/platformer-boss.png"
  val Boss2 = "http://woofjs.com/docs/images/platformer-boss.png"
  val Enemy = "http://woofjs.com/docs/images/platformer-enemy.png"
  val Enemy2 = "http://woofjs.com/docs/images/platformer-enemy2.png"
  val GameOver = "http://woofjs.com/docs/images/platformer-game-over.png"
  val GameOver2 = "http://woofjs.com/docs/images/rr-gameover.png"
  val PixelHeart = "http://woofjs.com/docs/images/platformer-heart.png"
  val NextLevel = "http://woofjs.com/docs/images/platformer-next-level.png"
  val Player = "http://woofjs.com/docs/images/platformer-player.png"
  val YouWin = "http://woofjs.com/docs/images/platformer-you-win.png"
  val Canoe = "http://woofjs.com/docs/images/river-canoe1.png"
  val Coconut = "http://woofjs.com/docs/images/river-coconut.png"
  val Gator = "http://woofjs.com/docs/images/river-gator.png"
  val Tree = "http://woofjs.com/docs/images/river-tree.png"
  val Planet = "http://woofjs.com/docs/images/rr-planet.png"
  val Star = "http://woofjs.com/docs/images/rr-star.png"
  val Squid = "http://woofjs.com/docs/images/squidchase-squid.png"
  val Splat = "http://woofjs.com/docs/images/tomato-attack-splat.png"
  val Tomato = "http://woofjs.com/docs/images/tomato-attack-tomato.png"
}

object Background {
  val Beach = "http://woofjs.com/docs/images/beach.jpg"
  val NYC = "http://woofjs.com/docs/images/nyc.jpg"
  val CandyLand = "http://woofjs.com/docs/images/candyland.jpg"
  val Space = "http://woofjs.com/docs/images/boss-backdrop.jpg"
  val Minecraft = "http://woofjs.com/docs/images/minecraft.jpg"
  val Snow = "http://woofjs.com/docs/images/snow.jpg"
  val OuterSpace = "http://woofjs.com/docs/images/outerspace.jpg"
  val Ocean = "http://woofjs.com/docs/images/ocean.jpg"
  val Play = "http://woofjs.com/docs/images/bubbles-backdrop.jpeg"
  val DarkSpace = "http://woofjs.com/docs/images/rr-backdrop.jpg"
  val Farm = "http://woofjs.com/docs/images/tomato-attack-backdrop.jpg"
  val Sky = "http://woofjs.com/docs/images/war-backdrop.png"
  val Sea = "http://woofjs.com/docs/images/hungrycrab-backdrop.jpg"
  val Platformer = "http://woofjs.com/docs/images/platformer-backdrop1.png"
  val Forest = "http://woofjs.com/docs/images/platformer-backdrop2.png"
  val Emoji = "http://woofjs.com/docs/images/emojibackground.jpg"
  val Galaxy = "http://woofjs.com/docs/images/galaxy.jpg"
  val HarryPotter = "http://woofjs.com/docs/images/harrypotter.jpg"
  val StarWars = "http://woofjs.com/docs/images/starwars.jpg"
  val Mario = "http://woofjs.com/docs/images/marioback.jpg"
  val Soccer = "http://woofjs.com/docs/images/soccer.jpg"
  val FlappyBack = "http://woofjs.com/docs/images/flappyback.png"
  val Desert = "http://woofjs.com/docs/images/desert.jpg"
  val Grass = "http://woofjs.com/docs/images/grass.jpg"
}