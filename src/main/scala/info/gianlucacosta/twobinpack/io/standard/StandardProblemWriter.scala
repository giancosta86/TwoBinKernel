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

package info.gianlucacosta.twobinpack.io.standard

import java.io.Writer

import info.gianlucacosta.helios.io.DecoratorWriter
import info.gianlucacosta.twobinpack.core.StandardProblem

/**
  * Writer providing a dedicated method for writing standard problems to text files
  *
  * @param targetWriter
  */
class StandardProblemWriter(targetWriter: Writer) extends DecoratorWriter(targetWriter) {
  def writeStandardProblem(standardProblem: StandardProblem): Unit = {
    val blockDimensionsCount =
      standardProblem.blocks.size

    targetWriter.write(blockDimensionsCount.toString)
    targetWriter.write("\r\n")

    targetWriter.write(standardProblem.initialFrameDimension.height.toString)
    targetWriter.write(" ")
    targetWriter.write(standardProblem.initialFrameDimension.width.toString)
    targetWriter.write("\r\n")

    standardProblem.blocks
      .toList
      .sortBy {
        case (blockDimension, quantity) =>
          blockDimension
      }
      .flatMap {
        case (blockDimension, quantity) =>
          List.fill(quantity)(blockDimension)
      }
      .zipWithIndex
      .foreach {
        case (blockDimension, index) =>
          val blockOrdinal =
            index + 1

          targetWriter.write(blockOrdinal.toString)
          targetWriter.write(" ")
          targetWriter.write(blockDimension.width.toString)
          targetWriter.write(" ")
          targetWriter.write(blockDimension.height.toString)
          targetWriter.write("\r\n")
      }

    targetWriter.write("\r\n")
  }
}
