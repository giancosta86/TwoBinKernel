/*^
  ===========================================================================
  TwoBinKernel
  ===========================================================================
  Copyright (C) 2016-2017 Gianluca Costa
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
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.{ClipboardContent, MouseEvent, TransferMode}
import scalafx.scene.paint.Color


/**
  * Renders a block in the block gallery
  */
private class BlockRenderer(
                             blockDimension: BlockDimension,
                             resolution: Int,
                             color: Color,
                             quantity: Int,
                             interactive: Boolean
                           ) extends Canvas {
  width =
    blockDimension.width * resolution

  height =
    blockDimension.height * resolution


  {
    val gc =
      graphicsContext2D

    gc.stroke =
      Color.Black

    gc.lineWidth =
      2

    gc.fill =
      color

    gc.fillRect(
      0,
      0,
      width() - 1,
      height() - 1
    )


    gc.strokeRect(
      1,
      1,
      width() - 2,
      height() - 2
    )


    gc.lineWidth =
      1


    gc.stroke =
      Color.Black


    Range(1, height().toInt - 1, resolution).foreach(lineY => {
      gc.strokeLine(
        0,
        lineY,

        width(),
        lineY
      )
    })
  }


  handleEvent(MouseEvent.DragDetected) {
    (event: MouseEvent) => {
      if (interactive) {
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
