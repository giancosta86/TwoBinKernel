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

import java.io.Writer

import info.gianlucacosta.helios.io.DecoratorWriter
import info.gianlucacosta.twobinpack.core.Solution

/**
  * Writer providing a dedicated method for writing solutions to CSV
  *
  * @param targetWriter
  */
class SolutionCsvWriter(targetWriter: Writer) extends DecoratorWriter(targetWriter) {
  def writeSolution(solution: Solution): Unit = {
    targetWriter.write(solution.id.toString)
    targetWriter.write("\r\n")

    targetWriter.write(solution.problem.id.toString)
    targetWriter.write("\r\n")

    targetWriter.write(solution.solverOption.getOrElse(""))
    targetWriter.write("\r\n")

    solution.blocks.foreach(block => {
      targetWriter.write(block.dimension.width.toString)
      targetWriter.write(",")
      targetWriter.write(block.dimension.height.toString)
      targetWriter.write(",")
      targetWriter.write(block.anchor.left.toString)
      targetWriter.write(",")
      targetWriter.write(block.anchor.top.toString)
      targetWriter.write("\r\n")
    })

    targetWriter.write("\r\n")
  }
}
