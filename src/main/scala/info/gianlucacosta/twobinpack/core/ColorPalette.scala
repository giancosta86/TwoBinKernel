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

package info.gianlucacosta.twobinpack.core

import scalafx.scene.paint.Color

/**
  * Provides colors for blocks, via its <i>getColor()</i> method.
  *
  * When rotation is enabled, it ensures that the horizontal and vertical version
  * of the same rectangular block have the same color; otherwise, they will have different
  * colors (as they actually are different block dimensions), provided that the color pool is
  * big enough: colors are assigned by cyclically iterating on the color pool.
  *
  * Furthermore, colors are deterministically assigned by pairing the list of colors with
  * the list of sorted dimensions.
  *
  * @param blockPool The related block pool
  * @param colorPool A list of colors
  */
case class ColorPalette(
                         blockPool: BlockPool,
                         colorPool: List[Color]
                       ) {

  private val colorMap: Map[BlockDimension, Color] =
    blockPool
      .blockDimensions
      .toList
      .sorted
      .zipWithIndex
      .flatMap {
        case (blockDimension, index) =>
          val color =
            colorPool(index % colorPool.length)

          if (blockPool.canRotateBlocks)
            List(
              blockDimension -> color,
              blockDimension.rotate() -> color
            )
          else List(
            blockDimension -> color
          )
      }.toMap


  def getColor(blockDimension: BlockDimension): Color =
    colorMap(blockDimension)
}
