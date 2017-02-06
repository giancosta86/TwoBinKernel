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

package info.gianlucacosta.twobinpack.core

import java.time.Duration
import java.util.UUID

import scalafx.scene.paint.Color

/**
  * A standard two-dimensional packing problem - containing a reduced subset of information
  * in comparison with a default Problem
  *
  * @param initialFrameDimension The initial frame dimension
  * @param blocks                The available blocks
  */
case class StandardProblem(
                            initialFrameDimension: FrameDimension,
                            blocks: Map[BlockDimension, Int]
                          ) {
  def this(sourceProblem: Problem) = this(
    sourceProblem.frameTemplate.initialDimension,
    sourceProblem.frameTemplate.blockPool.blocks
  )

  def toProblem(
                 id: UUID,
                 name: String,
                 frameMode: FrameMode,
                 blockColorsPool: List[Color],
                 canRotateBlocks: Boolean,
                 resolution: Int,
                 timeLimitOption: Option[Duration]
               ): Problem = {

    val blockPool: BlockPool =
      BlockPool.create(
        canRotateBlocks,
        blocks
      )


    val frameTemplate =
      FrameTemplate(
        initialFrameDimension,
        frameMode,
        blockPool,
        blockColorsPool,
        resolution
      )


    Problem(
      frameTemplate,
      timeLimitOption,
      name,
      id
    )
  }
}