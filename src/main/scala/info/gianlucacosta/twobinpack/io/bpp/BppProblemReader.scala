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

package info.gianlucacosta.twobinpack.io.bpp

import java.io.BufferedReader
import java.time.Duration
import java.util.UUID

import info.gianlucacosta.helios.io.DecoratorReader
import info.gianlucacosta.twobinpack.core._


/**
  * Reader providing a dedicated method for reading BPP problems from text files
  *
  * @param sourceReader
  */
class BppProblemReader(sourceReader: BufferedReader) extends DecoratorReader(sourceReader) {
  def readBppProblem(
                      initialFrameWidth: Int,
                      timeLimitOption: Option[Duration],
                      name: String,
                      id: UUID
                    ): Problem = {
    val totalBlockCount =
      sourceReader.readLine().toInt

    val frameLine =
      sourceReader.readLine().trim

    val initialFrameHeight =
      frameLine.toInt


    val initialFrameDimension =
      FrameDimension(
        initialFrameWidth,
        initialFrameHeight
      )


    val blockDimensions: Map[BlockDimension, Int] =
      parseLineBlock(trimmedLine => {
        val blockWidth =
          1

        val blockHeight =
          trimmedLine.toInt


        BlockDimension(
          blockWidth,
          blockHeight
        )
      })
        .groupBy(blockdimension => blockdimension)
        .map {
          case (blockDimension, groupedBlocks) =>
            blockDimension -> groupedBlocks.length
        }


    val blockPool =
      BlockPool.create(
        canRotateBlocks = false,
        draftBlocks = blockDimensions
      )

    require(
      blockPool.totalBlockCount == totalBlockCount,
      "The declared total block count does not match the actual number of declared blocks"
    )

    val frameTemplate =
      FrameTemplate(
        initialFrameDimension,
        FrameMode.Strip,
        blockPool,
        FrameTemplate.SuggestedBlockColorsPool,
        Problem.SuggestedResolution
      )


    val problem =
      Problem(
        frameTemplate,
        timeLimitOption,
        name,
        id
      )

    require(
      problem.isBinPacking,
      "The problem is not a Bin Packing Problem"
    )

    problem
  }
}
