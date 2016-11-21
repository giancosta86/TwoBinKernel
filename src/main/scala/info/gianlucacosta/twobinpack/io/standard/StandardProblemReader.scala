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

package info.gianlucacosta.twobinpack.io.standard

import java.io.BufferedReader

import info.gianlucacosta.helios.io.DecoratorReader
import info.gianlucacosta.twobinpack.core._


/**
  * Reader providing a dedicated method for reading standard problems from text files
  *
  * @param sourceReader
  */
class StandardProblemReader(sourceReader: BufferedReader) extends DecoratorReader(sourceReader) {
  def readStandardProblem(): StandardProblem = {
    //The first line is just skipped
    sourceReader.readLine()

    val frameLine =
      sourceReader.readLine().trim

    val frameParams =
      frameLine.split(raw"\s+")

    val frameHeight =
      frameParams(0).toInt

    val frameWidth =
      frameParams(1).toInt

    val initialFrameDimension =
      FrameDimension(
        frameWidth,
        frameHeight
      )


    val blockDimensions: Map[BlockDimension, Int] =
      parseLineBlock(trimmedLine => {
        val blockParams =
          trimmedLine.split(raw"\s+")


        val blockWidth =
          blockParams(1).toInt

        val blockHeight =
          blockParams(2).toInt


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


    StandardProblem(
      initialFrameDimension,
      blockDimensions
    )
  }
}
