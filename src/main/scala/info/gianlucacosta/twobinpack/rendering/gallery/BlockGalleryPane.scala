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
  * @param blockGallery    The block gallery to render
  * @param colorPalette    The color palette used to paint the blocks
  * @param resolution      The number of pixels per quantum units
  * @param labelTextFormat The string to be shown in the label related to each block dimension.
  *                        It can contain a few placeholders:
  *                        <ul>
  *                        <li><b>%Q</b>: The quantity of blocks (of such dimension) available</li>
  *                        <li><b>%B</b>: The string "block" or "blocks", according to the actual block quantity</li>
  *                        <li><b>%W</b>: The dimension's width</li>
  *                        <li><b>%H</b>: The dimension's height</li>
  *                        </ul>
  *                        If it is omitted, a default format string is provided.
  * @param sortAscending   If true (the default), blocks will be sorted by increasing dimension
  */
class BlockGalleryPane(
                        blockGallery: BlockGallery,
                        colorPalette: ColorPalette,
                        val resolution: Int,
                        val labelTextFormat: String = s"%Q %B %W x %H",
                        sortAscending: Boolean = true
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


  {
    val ascendingSortedBlocks =
      blockGallery.availableBlocks
        .toList
        .sortBy {
          case (blockDimension, quantity) =>
            blockDimension
        }


    val sortedBlocks =
      if (sortAscending)
        ascendingSortedBlocks
      else
        ascendingSortedBlocks.reverse


    sortedBlocks
      .foreach {
        case (blockDimension, quantity) =>
          val color =
            colorPalette.getColor(blockDimension)

          val labelText =
            labelTextFormat
              .replaceAll("%Q", quantity.toString)
              .replaceAll("%B",
                if (quantity == 1)
                  "block"
                else
                  "blocks"
              )
              .replaceAll("%W", blockDimension.width.toString)
              .replaceAll("%H", blockDimension.height.toString)

          val blockItemRenderer =
            new BlockItemRenderer(
              this,
              blockDimension,
              quantity,
              color,
              labelText
            )

          children.add(blockItemRenderer)
      }
  }
}
