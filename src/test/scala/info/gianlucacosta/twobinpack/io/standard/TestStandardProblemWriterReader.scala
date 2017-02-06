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

import java.io.{BufferedReader, Writer}

import info.gianlucacosta.twobinpack.core.StandardProblem
import info.gianlucacosta.twobinpack.io.WriterReaderTestBase
import info.gianlucacosta.twobinpack.test.SimpleTestData

class TestStandardProblemWriterReader extends WriterReaderTestBase[StandardProblem, StandardProblemWriter, StandardProblemReader] {
  override protected def createOriginalObject(): StandardProblem =
    new StandardProblem(
      SimpleTestData.ProblemA
    )

  override protected def createWriter(targetWriter: Writer): StandardProblemWriter =
    new StandardProblemWriter(targetWriter)

  override protected def exportObject(writer: StandardProblemWriter, originalObject: StandardProblem): Unit =
    writer.writeStandardProblem(originalObject)

  override protected def createReader(sourceReader: BufferedReader): StandardProblemReader =
    new StandardProblemReader(sourceReader)

  override protected def importObject(reader: StandardProblemReader): StandardProblem =
    reader.readStandardProblem()
}
