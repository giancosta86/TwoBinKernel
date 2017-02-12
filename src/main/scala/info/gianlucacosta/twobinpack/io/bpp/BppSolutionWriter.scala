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
import info.gianlucacosta.twobinpack.core.Solution

/**
  * Writes a BPP solution using a basic text format
  */
class BppSolutionWriter(targetWriter: Writer) extends DecoratorWriter(targetWriter) {
  def writeSolution(solution: Solution): Unit = {
    val blockSizesPerBin: Map[Int, List[Int]] =
      solution
        .blocks
        .groupBy(anchoredBlock => {
          val binOrdinal =
            anchoredBlock.left + 1

          binOrdinal
        })
        .map {
          case (binOrdinal, blockSet) =>
            val sortedBlockSizes =
              blockSet
                .map(_.dimension.height)
                .toList
                .sorted
                .reverse

            binOrdinal -> sortedBlockSizes
        }

    val maxBinOrdinalUsed =
      blockSizesPerBin
        .keys
        .max

    Range.inclusive(1, maxBinOrdinalUsed).foreach(binOrdinal => {
      val currentBinBlockSizes =
        blockSizesPerBin.getOrElse(
          binOrdinal,
          List()
        )

      targetWriter.write(s"${binOrdinal}) ${currentBinBlockSizes.mkString(" ")}\r\n")
    })


    targetWriter.write("\r\n")
  }
}
