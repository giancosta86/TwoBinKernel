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

object FrameTemplate {
  /**
    * A default pool of fairly pleasant colors
    */
  val SuggestedBlockColorsPool: List[Color] = List(
    Color.Yellow,
    Color.Orchid,
    Color.LightGreen,
    Color.PaleTurquoise,
    Color.Red,
    Color.MediumBlue,
    Color.Khaki,
    Color.Teal,
    Color.Orange,
    Color.DodgerBlue,
    Color.HotPink,
    Color.ForestGreen,
    Color.DarkViolet,
    Color.FireBrick,
    Color.LightPink,
    Color.OliveDrab,
    Color.Fuchsia,
    Color.Plum
  )
}

/**
  * Model information for creating a Frame
  *
  * @param initialDimension The initial dimension.
  *                         In Knapsack mode, the dimension remains constant,
  *                         whereas in Strip the width can arbitrarily grow
  * @param frameMode        The frame mode
  * @param blockPool        The available blocks
  * @param blockColorsPool  The pool of colors used to paint the blocks
  * @param resolution       How many pixels are used to render a quantum unit (both horizontally and vertically)
  */
case class FrameTemplate(
                          initialDimension: FrameDimension,
                          frameMode: FrameMode,
                          blockPool: BlockPool,
                          blockColorsPool: List[Color],
                          resolution: Int
                        ) {
  require(
    blockColorsPool.nonEmpty,
    "At least one color must be provided"
  )

  require(
    Problem.MinResolution <= resolution && resolution <= Problem.MaxResolution,
    s"The resolution must be in the range [${Problem.MinResolution}..${Problem.MaxResolution}]"
  )

  @transient
  lazy val blockGallery: BlockGallery =
    new BlockGallery(
      blockPool
    )

  @transient
  lazy val colorPalette: ColorPalette =
    ColorPalette(
      blockPool,
      blockColorsPool
    )
}
