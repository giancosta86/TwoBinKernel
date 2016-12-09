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

package info.gianlucacosta.twobinpack.io.csv

import java.io.BufferedReader

import info.gianlucacosta.helios.io.DecoratorReader
import info.gianlucacosta.twobinpack.core.{AnchoredBlock, BlockDimension, QuantizedPoint2D, Solution}

abstract class SolutionCsvReaderBase(sourceReader: BufferedReader) extends DecoratorReader(sourceReader) {
  /**
    * Reads a solution from the CSV, throwing exceptions on error; in particular,
    * it should throw MissingProblemException if the problem referenced by the
    * solution cannot be found in the underlying storage.
    *
    * @return The solution read - or None if EOF was not reached
    */
  def readSolution(): Option[Solution]

  /**
    * Reads a line
    *
    * @return The line, or null on EOF
    */
  def readLine(): String =
    sourceReader.readLine()


  /**
    * Parses a block of lines, assuming each of them describes an AnchoredBlock
    *
    * @return The set of AnchoredBlock instances read from the file
    */
  protected def parseAnchoredBlocks(): Set[AnchoredBlock] =
    parseLineBlock(
      trimmedLine => {
        val blockParameters =
          trimmedLine.split(',')
            .map(_.toInt)

        AnchoredBlock(
          BlockDimension(
            blockParameters(0),
            blockParameters(1)
          ),

          QuantizedPoint2D(
            blockParameters(2),
            blockParameters(3)
          )
        )
      }
    ).toSet
}
