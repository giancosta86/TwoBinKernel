/*^
  ===========================================================================
  TwoBinKernel
  ===========================================================================
  Copyright (C) 2016 Gianluca Costa
  ===========================================================================
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  ===========================================================================
*/

package info.gianlucacosta.twobinpack.rendering.frame

import javafx.beans.property._

import info.gianlucacosta.twobinpack.core._
import info.gianlucacosta.twobinpack.rendering.{BlockDataFormat, BlockDragData}

import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.VPos
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.input._
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight, TextAlignment}


/**
  * The drawing area - showing anchored blocks - that the user can
  * interactively edit.
  *
  * @param frameTemplate The set of parameters used to build the Frame
  */
class Frame(val frameTemplate: FrameTemplate) extends Canvas {
  private val _interactive =
    new SimpleBooleanProperty(true)

  /**
    * When true (the default), the user can interactively add, remove and move blocks.
    *
    * @return
    */
  def interactive: BooleanProperty =
  _interactive

  def interactive_=(newValue: Boolean) =
    interactive() =
      newValue


  private val _resolution =
    new SimpleIntegerProperty(frameTemplate.resolution)

  /**
    * The pixels employed to render a quantum unit - both horizontally and vertically
    *
    * @return
    */
  def resolution: IntegerProperty =
  _resolution

  def resolution_=(newValue: Int) =
    resolution() =
      newValue


  private val blockBorderSize =
    new SimpleDoubleProperty(0)

  blockBorderSize <==
    resolution / 10


  private val _blockGallery =
    new SimpleObjectProperty[BlockGallery](frameTemplate.blockGallery)

  /**
    * The current block gallery - showing the blocks that can be added to the frame
    *
    * @return
    */
  def blockGallery: ReadOnlyObjectProperty[BlockGallery] =
  _blockGallery

  private def blockGallery_=(newValue: BlockGallery) =
    _blockGallery() =
      newValue


  private val _blocks =
    new SimpleObjectProperty[Set[AnchoredBlock]](Set())

  /**
    * The blocks currently anchored within the set
    *
    * @return
    */
  def blocks: ReadOnlyObjectProperty[Set[AnchoredBlock]] =
  _blocks

  _blocks.onChange {
    render()
  }


  /**
    * Adds an anchored block to the frame.
    *
    * @param block The block to add; it must be available in the gallery
    */
  def addBlock(block: AnchoredBlock): Unit = {
    require(
      blocks().forall(!_.overlaps(block)),
      "The new block cannot overlap blocks already in the frame"
    )

    blockGallery =
      blockGallery().removeBlock(block.dimension)

    _blocks() +=
      block
  }


  /**
    * Removes and anchored block from the frame
    *
    * @param block
    */
  def removeBlock(block: AnchoredBlock): Unit = {
    require(
      blocks().contains(block),
      s"Block ${block} should be in the frame"
    )

    blockGallery =
      blockGallery().addBlock(block.dimension)

    _blocks() -=
      block
  }


  /**
    * Removes all the anchored blocks from the frame
    */
  def clearBlocks(): Unit = {
    blockGallery =
      frameTemplate.blockGallery

    _blocks() =
      Set()
  }


  private val _potentialCacheOption =
    new SimpleObjectProperty[Option[PotentialCache]](None)

  private def potentialCacheOption: ObjectProperty[Option[PotentialCache]] =
    _potentialCacheOption

  private def potentialCacheOption_=(newValue: Option[PotentialCache]) =
    _potentialCacheOption() =
      newValue


  private val _potentialBlockOption =
    new SimpleObjectProperty[Option[AnchoredBlock]](None)

  private def potentialBlockOption: ObjectProperty[Option[AnchoredBlock]] =
    _potentialBlockOption

  potentialBlockOption <==
    Bindings.createObjectBinding(
      () => {
        potentialCacheOption().flatMap(potentialCache =>
          potentialCache.potentialBlockOption
        )
      },

      potentialCacheOption
    )

  potentialBlockOption.onChange {
    render()
  }


  private val _potentialBlockFont =
    new SimpleObjectProperty[Font](
      createPotentialBlockFont(frameTemplate.resolution)
    )

  private def potentialBlockFont: SimpleObjectProperty[Font] =
    _potentialBlockFont


  potentialBlockFont <==
    Bindings.createObjectBinding[Font](
      () =>
        createPotentialBlockFont(resolution()),

      resolution
    )


  private def createPotentialBlockFont(resolutionValue: Int): Font =
    Font(
      "Arial",
      FontWeight.Bold,
      resolutionValue / 2
    )


  potentialBlockFont.onChange {
    render()
  }


  private val _quantizedWidth =
    new SimpleIntegerProperty(frameTemplate.initialDimension.width)

  /**
    * The width of the Frame expressed in terms of the basic quantum unit
    *
    * @return
    */
  def quantizedWidth: ReadOnlyIntegerProperty =
  _quantizedWidth

  _quantizedWidth <==
    Bindings.createIntegerBinding(
      () => {
        frameTemplate.frameMode match {
          case FrameMode.Knapsack =>
            frameTemplate.initialDimension.width

          case FrameMode.Strip =>
            val maxBlockRight =
              if (blocks().nonEmpty)
                blocks()
                  .map(_.right)
                  .max
              else
                -1

            val interactiveAdditionalColumn =
              if (interactive())
                1
              else
                0

            List(
              frameTemplate.initialDimension.width,

              maxBlockRight +
                interactiveAdditionalColumn +
                1, //Because of the 0-based anchoring

              potentialBlockOption()
                .map(_.right + interactiveAdditionalColumn + 1)
                .getOrElse(-1)
            )
              .max
        }
      },

      interactive,
      blocks,
      potentialBlockOption
    )


  val _quantizedHeight =
    new SimpleIntegerProperty(frameTemplate.initialDimension.height)

  /**
    * The height of the Frame expressed in terms of the basic quantum unit
    *
    * @return
    */
  def quantizedHeight: ReadOnlyIntegerProperty =
  _quantizedHeight


  private val _drawingWidth =
    new SimpleDoubleProperty(quantizedWidth() * resolution())

  /**
    * The width of the frame (in FX rendering) dedicated to actually drawing
    * the frame content
    *
    * @return
    */
  private def drawingWidth: ReadOnlyDoubleProperty =
  _drawingWidth

  _drawingWidth <==
    quantizedWidth * resolution


  private val _drawingHeight =
    new SimpleDoubleProperty(quantizedHeight() * resolution())

  private def drawingHeight: ReadOnlyDoubleProperty =
    _drawingHeight

  _drawingHeight <==
    quantizedHeight * resolution


  width <==
    Bindings.createDoubleBinding(
      () => {
        val antiScrollingGlitchAdditionalWidth: Int =
          frameTemplate.frameMode match {
            case FrameMode.Knapsack =>
              0

            case FrameMode.Strip =>
              if (interactive())
                math.max(
                  40,
                  resolution()
                )
              else
                0
          }

        drawingWidth() +
          antiScrollingGlitchAdditionalWidth +
          blockBorderSize() / 2 //To correctly draw grids and blocks on the edge
      },

      interactive,
      drawingWidth
    )


  width.onChange {
    render()
  }


  height <==
    Bindings.createDoubleBinding(
      () => {
        drawingHeight() +
          blockBorderSize() / 2 //To correctly draw grids and blocks on the edge
      },

      drawingHeight
    )

  height.onChange {
    render()
  }


  render()


  /**
    * Renders the frame
    */
  private def render(): Unit = {
    clearCanvas(graphicsContext2D)
    drawBackground(graphicsContext2D)
    drawBackgroundGrid(graphicsContext2D)
    drawBlocks(graphicsContext2D)

    potentialBlockOption().foreach(potentialBlock => {
      drawPotentialBlock(graphicsContext2D, potentialBlock)
    })
  }


  private def clearCanvas(gc: GraphicsContext): Unit = {
    gc.clearRect(
      0,
      0,
      width(),
      height()
    )
  }


  private def drawBackground(gc: GraphicsContext): Unit = {
    gc.fill =
      Color.White

    gc.fillRect(
      0,
      0,
      drawingWidth(),
      drawingHeight()
    )
  }


  private def drawBackgroundGrid(gc: GraphicsContext): Unit = {
    gc.lineWidth =
      1

    gc.stroke =
      Color.Gray

    Range(0, drawingWidth.toInt + 1, resolution()).foreach(lineX => {
      gc.strokeLine(
        lineX,
        0,

        lineX,
        drawingHeight()
      )
    })


    Range(0, drawingHeight.toInt + 1, resolution()).foreach(lineY => {
      gc.strokeLine(
        0,
        lineY,

        drawingWidth(),
        lineY
      )
    })
  }


  private def drawBlocks(gc: GraphicsContext): Unit = {
    gc.lineWidth =
      blockBorderSize()

    gc.stroke =
      Color.Black

    blocks().foreach(block => {
      gc.fill =
        frameTemplate.colorPalette.getColor(block.dimension)


      val rectLeft =
        block.left * resolution() + gc.lineWidth / 2

      val rectTop =
        block.top * resolution() + gc.lineWidth / 2


      val rectWidth =
        block.dimension.width * resolution() - gc.lineWidth / 2

      val rectHeight =
        block.dimension.height * resolution() - gc.lineWidth / 2


      gc.fillRect(
        rectLeft,
        rectTop,
        rectWidth,
        rectHeight
      )

      gc.strokeRect(
        rectLeft,
        rectTop,
        rectWidth,
        rectHeight
      )
    })
  }


  private def drawPotentialBlock(gc: GraphicsContext, potentialBlock: AnchoredBlock): Unit = {
    gc.fill =
      frameTemplate.colorPalette.getColor(potentialBlock.dimension).opacity(0.6)

    val rectLeft =
      potentialBlock.left * resolution()

    val rectTop =
      potentialBlock.top * resolution()


    val rectWidth =
      potentialBlock.dimension.width * resolution()

    val rectHeight =
      potentialBlock.dimension.height * resolution()


    gc.fillRect(
      rectLeft,
      rectTop,
      rectWidth,
      rectHeight
    )


    gc.font =
      potentialBlockFont()

    gc.fill =
      Color.Black

    gc.textAlign =
      TextAlignment.Center

    gc.textBaseline =
      VPos.Center


    gc.fillText(
      potentialCacheOption().get.potentialBlockQuantityOption.get.toString,
      rectLeft + rectWidth / 2,
      rectTop + rectHeight / 2
    )
  }


  /**
    * Given the local point in FX coordinates, returns the quantized point containing it
    *
    * @param x The x coordinate
    * @param y The y coordinate
    * @return A point in quantum unit coordinates
    */
  private def getQuantizedPointContaining(x: Double, y: Double): QuantizedPoint2D = {
    val inFrameQuantizedX: Int =
      math.max(
        math.min(
          (x / resolution()).toInt,
          quantizedWidth.get() - 1
        ),
        0
      )

    val inFrameQuantizedY: Int =
      math.max(
        math.min(
          (y / resolution()).toInt,
          quantizedHeight.get() - 1
        ),
        0
      )

    QuantizedPoint2D(
      inFrameQuantizedX,
      inFrameQuantizedY
    )
  }


  /**
    * Finds the block containing the given quantized point
    *
    * @param quantizedPoint A point in quantum unit coordinates
    * @return The block containing the point, or None
    */
  private def getBlockContaining(quantizedPoint: QuantizedPoint2D): Option[AnchoredBlock] =
  blocks().find(block =>
    block.containsPoint(quantizedPoint)
  )


  /**
    * Returns the maximum possible dimension for a potential block that should be anchored
    * at the given point
    *
    * @param anchor The potential anchor
    * @return The maximum dimension allowed
    */
  private def getMaxPotentialBlockDimension(anchor: QuantizedPoint2D): Option[BlockDimension] = {
    val maxPotentialBlockWidth =
      frameTemplate.frameMode match {
        case FrameMode.Knapsack =>
          frameTemplate.initialDimension.width - anchor.left

        case FrameMode.Strip =>
          Int.MaxValue
      }


    val maxPotentialBlockHeight =
      anchor.top + 1

    if (maxPotentialBlockWidth > 0 && maxPotentialBlockHeight > 0) {
      Some(
        BlockDimension(
          maxPotentialBlockWidth,
          maxPotentialBlockHeight
        )
      )
    } else
      None
  }


  handleEvent(MouseEvent.MouseClicked) {
    (event: MouseEvent) => {
      if (interactive()) {
        val clickedQuantizedPoint =
          getQuantizedPointContaining(
            event.x,
            event.y
          )

        val clickedBlockOption =
          getBlockContaining(
            clickedQuantizedPoint
          )

        clickedBlockOption match {
          case Some(clickedBlock) =>
            removeBlock(clickedBlock)

          case None =>
            potentialBlockOption().foreach(potentialBlock => {
              if (potentialBlock.containsPoint(clickedQuantizedPoint)) {
                addBlock(potentialBlock)

                potentialCacheOption =
                  None
              }
            })
        }
      }

      event.consume()
    }
  }


  handleEvent(ScrollEvent.Scroll) {
    (event: ScrollEvent) => {
      if (interactive()) {
        val anchor =
          getQuantizedPointContaining(
            event.x,
            event.y
          )

        val validatedPotentialCacheOption =
          potentialCacheOption().flatMap(potentialCache => {
            if (potentialCache.anchor == anchor)
              Some(
                potentialCache
              )
            else
              None
          })


        validatedPotentialCacheOption match {
          case Some(potentialCache) =>
            potentialCacheOption =
              Some(
                potentialCache.scrollCompatibleBlocks(-event.deltaY.toInt)
              )

          case None =>
            val maxPotentialBlockDimensionOption: Option[BlockDimension] =
              getMaxPotentialBlockDimension(anchor)


            val compatibleBlocks: List[(AnchoredBlock, Int)] =
              maxPotentialBlockDimensionOption match {
                case Some(maxPotentialBlockDimension) =>
                  BlockPositioning.getCompatibleBlocks(
                    blockGallery().availableBlocks,
                    maxPotentialBlockDimension,
                    anchor,
                    blocks()
                  )
                    .toList
                    .sortBy {
                      case (block, quantity) =>
                        block.dimension
                    }

                case None =>
                  List()
              }



            val initialScrollIndexOption: Option[Int] =
              if (compatibleBlocks.nonEmpty)
                Some(0)
              else
                None


            potentialCacheOption =
              Some(
                PotentialCache(
                  anchor,
                  compatibleBlocks,
                  initialScrollIndexOption
                )
              )

        }
      }


      event.consume()
    }
  }


  handleEvent(MouseEvent.MouseExited) {
    (event: MouseEvent) => {
      potentialCacheOption =
        None

      event.consume()
    }
  }


  handleEvent(MouseEvent.MouseMoved) {
    (event: MouseEvent) => {
      if (interactive()) {
        val currentQuantizedPoint =
          getQuantizedPointContaining(
            event.x,
            event.y
          )

        potentialCacheOption =
          potentialCacheOption().flatMap(potentialCache => {
            if (potentialCache.anchor == currentQuantizedPoint)
              Some(potentialCache)
            else
              None
          })
      }

      event.consume()
    }
  }


  handleEvent(DragEvent.DragOver) {
    (event: DragEvent) => {
      if (interactive()) {
        event.dragboard.content.get(BlockDataFormat) match {
          case Some(blockDragData: BlockDragData) =>
            val potentialAnchor =
              getQuantizedPointContaining(
                event.x,
                event.y
              )


            val maxPotentialDimensionOption =
              getMaxPotentialBlockDimension(potentialAnchor)


            val compatibleBlocks: Map[AnchoredBlock, Int] =
              maxPotentialDimensionOption match {
                case Some(maxPotentialDimension) =>
                  BlockPositioning.getCompatibleBlocks(
                    blockGallery().availableBlocks,
                    maxPotentialDimension,
                    potentialAnchor,
                    blocks()
                  )

                case None =>
                  Map()
              }


            val compatibleBlockDimensions: Set[BlockDimension] =
              compatibleBlocks
                .keySet
                .map(_.dimension)


            if (compatibleBlockDimensions.contains(blockDragData.blockDimension)) {
              potentialCacheOption =
                Some(
                  PotentialCache(
                    potentialAnchor,

                    List(
                      AnchoredBlock(
                        blockDragData.blockDimension,
                        potentialAnchor
                      ) -> blockDragData.quantity
                    ),

                    Some(0)
                  )
                )

              event.acceptTransferModes(TransferMode.Any: _*)
            } else {
              potentialCacheOption =
                None

              event.acceptTransferModes(TransferMode.None: _*)
            }


          case _ =>
          //Just do nothing
        }
      }

      event.consume()
    }
  }


  handleEvent(DragEvent.DragDropped) {
    (event: DragEvent) => {
      if (interactive()) {
        potentialBlockOption().foreach(potentialBlock => {
          addBlock(potentialBlock)

          potentialCacheOption =
            None
        })
      }

      event.consume()
    }
  }


  handleEvent(MouseEvent.DragDetected) {
    (event: MouseEvent) => {
      if (interactive()) {
        val startQuantizedPoint =
          getQuantizedPointContaining(
            event.x,
            event.y
          )


        val draggedBlockOption =
          getBlockContaining(startQuantizedPoint)


        draggedBlockOption.foreach(draggedBlock => {
          removeBlock(draggedBlock)

          val dragboard =
            startDragAndDrop(TransferMode.Move)

          dragboard.content =
            new ClipboardContent {
              put(
                BlockDataFormat,

                BlockDragData(
                  draggedBlock.dimension,
                  blockGallery().availableBlocks(draggedBlock.dimension)
                )
              )
            }
        })
      }

      event.consume()
    }
  }
}
