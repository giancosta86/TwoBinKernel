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

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}


private object BlockItemRenderer {
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
  * Renders a block item (that is, block and the related label) in the gallery
  *
  * @param blockGalleryPane The reference BlockGalleryPane
  * @param blockDimension   The block dimension
  * @param quantity         The number of instances of the block in the gallery
  * @param color            The block color
  * @param labelText        The text to show in the label
  */
private class BlockItemRenderer(
                                 blockGalleryPane: BlockGalleryPane,
                                 blockDimension: BlockDimension,
                                 quantity: Int,
                                 color: Color,
                                 labelText: String
                               ) extends VBox {

  styleClass +=
    "blockItem"

  alignment =
    Pos.Center

  spacing =
    7

  padding =
    Insets(10)


  private val blockRenderer =
    new BlockRenderer(
      blockDimension,
      blockGalleryPane.resolution,
      color,
      quantity,
      blockGalleryPane.interactive()
    )


  private val descriptionLabel =
    new Label {
      text =
        labelText

      font =
        BlockItemRenderer.descriptionFont
    }


  children = List(
    blockRenderer,
    descriptionLabel
  )
}
