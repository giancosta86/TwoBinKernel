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

package info.gianlucacosta.twobinpack.rendering.gallery

import info.gianlucacosta.twobinpack.core.BlockDimension
import info.gianlucacosta.twobinpack.rendering.{BlockDataFormat, BlockDragData}

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.input._
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, FontWeight}


private object BlockRenderer {
  /**
    * Font used by the description label
    */
  private val descriptionFont =
  Font.font(
    "Arial",
    FontWeight.Bold,
    14
  )
}

/**
  * Renders a block in the gallery
  *
  * @param blockGalleryPane The reference BlockGalleryPane
  * @param blockDimension   The block dimension
  * @param quantity         The number of instances of the block in the gallery
  * @param color            The block color
  */
private class BlockRenderer(
                             blockGalleryPane: BlockGalleryPane,
                             blockDimension: BlockDimension,
                             quantity: Int,
                             color: Color) extends VBox {

  alignment =
    Pos.Center

  spacing =
    7

  padding =
    Insets(10)


  private val sampleRectangle =
    new Rectangle {
      width =
        blockDimension.width * blockGalleryPane.resolution

      height =
        blockDimension.height * blockGalleryPane.resolution


      fill =
        color

      stroke =
        Color.Black


      handleEvent(MouseEvent.DragDetected) {
        (event: MouseEvent) => {
          if (blockGalleryPane.interactive()) {
            val dragboard =
              startDragAndDrop(TransferMode.Move)

            dragboard.content =
              new ClipboardContent {
                put(
                  BlockDataFormat,

                  BlockDragData(
                    blockDimension,
                    quantity
                  )
                )
              }
          }

          event.consume()
        }
      }
    }


  private val descriptionLabel =
    new Label {
      private val blockName =
        if (quantity == 1)
          "block"
        else
          "blocks"

      text =
        s"${quantity} ${blockName} ${blockDimension.width} x ${blockDimension.height}"

      font =
        BlockRenderer.descriptionFont
    }

  children = List(
    sampleRectangle,
    descriptionLabel
  )
}
