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

package info.gianlucacosta.twobinpack.io.csv.v2

import java.io.{BufferedReader, Writer}

import info.gianlucacosta.twobinpack.core.Solution
import info.gianlucacosta.twobinpack.io.WriterReaderTestBase
import info.gianlucacosta.twobinpack.test.SimpleTestData

abstract class SolutionCsvWriterReaderV2TestBase extends WriterReaderTestBase[Solution, SolutionCsvWriter2, SolutionCsvReader2] {
  override protected def createWriter(targetWriter: Writer): SolutionCsvWriter2 =
    new SolutionCsvWriter2(targetWriter)


  override protected def exportObject(writer: SolutionCsvWriter2, originalObject: Solution): Unit =
    writer.writeSolution(originalObject)


  override protected def createReader(sourceReader: BufferedReader): SolutionCsvReader2 =
    new SolutionCsvReader2(
      sourceReader,
      (problemId, solutionId) => SimpleTestData.ProblemA
    )


  override protected def importObject(reader: SolutionCsvReader2): Solution =
    reader.readSolution().get
}
