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

import java.io.Writer

import info.gianlucacosta.helios.io.DecoratorWriter
import info.gianlucacosta.twobinpack.core.Problem

/**
  * Writer providing a dedicated method for writing BPP problems to text files
  *
  * @param targetWriter
  */
class BppProblemWriter(targetWriter: Writer) extends DecoratorWriter(targetWriter) {
  def writeBppProblem(problem: Problem): Unit = {
    require(problem.isBinPacking)

    targetWriter.write(problem.frameTemplate.blockPool.totalBlockCount.toString)
    targetWriter.write("\r\n")

    targetWriter.write(problem.frameTemplate.initialDimension.height.toString)
    targetWriter.write("\r\n")

    problem.frameTemplate.blockPool.blocks
      .toList
      .sortBy {
        case (blockDimension, quantity) =>
          blockDimension
      }
      .reverse
      .flatMap {
        case (blockDimension, quantity) =>
          List.fill(quantity)(blockDimension)
      }
      .zipWithIndex
      .foreach {
        case (blockDimension, index) =>
          targetWriter.write(blockDimension.height.toString)
          targetWriter.write("\r\n")
      }

    targetWriter.write("\r\n")
  }
}
