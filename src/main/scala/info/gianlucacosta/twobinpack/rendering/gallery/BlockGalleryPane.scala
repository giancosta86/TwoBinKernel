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

import javafx.beans.property.{BooleanProperty, SimpleBooleanProperty}

import info.gianlucacosta.twobinpack.core.{BlockGallery, ColorPalette}

import scalafx.Includes._
import scalafx.scene.layout.FlowPane

/**
  * Renders a block gallery.
  *
  * Considering that BlockGallery is immutable, rendering a new block gallery
  * requires creating a new pane.
  *
  * Can be styled via the <i>blockGalleryPane</i> CSS class.
  *
  * @param blockGallery The block gallery to render
  * @param colorPalette The color palette used to paint the blocks
  * @param resolution   The number of pixels per quantum units
  */
class BlockGalleryPane(
                        blockGallery: BlockGallery,
                        colorPalette: ColorPalette,
                        val resolution: Int
                      ) extends FlowPane {

  require(
    blockGallery.blockPool == colorPalette.blockPool,
    "The block gallery and the color palette must share the same block pool"
  )


  styleClass +=
    "blockGalleryPane"


  hgap =
    65

  vgap =
    30


  private val _interactive =
    new SimpleBooleanProperty(true)

  /**
    * If interactivity is set to false, it will be impossible to drag the blocks
    *
    * @return
    */
  def interactive: BooleanProperty =
  _interactive

  def interactive_=(newValue: Boolean) =
    _interactive() =
      newValue


  blockGallery.availableBlocks
    .toList
    .sortBy {
      case (blockDimension, quantity) =>
        blockDimension
    }
    .foreach {
      case (blockDimension, quantity) =>
        val color =
          colorPalette.getColor(blockDimension)

        val blockRenderer =
          new BlockRenderer(
            this,
            blockDimension,
            quantity,
            color
          )

        children.add(blockRenderer)
    }
}
