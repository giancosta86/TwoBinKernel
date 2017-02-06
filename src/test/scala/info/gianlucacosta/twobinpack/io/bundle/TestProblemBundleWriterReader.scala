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

package info.gianlucacosta.twobinpack.io.bundle

import java.io.{BufferedReader, Writer}

import info.gianlucacosta.twobinpack.core._
import info.gianlucacosta.twobinpack.io.WriterReaderTestBase
import info.gianlucacosta.twobinpack.test.SimpleTestData

class TestProblemBundleWriterReader extends WriterReaderTestBase[ProblemBundle, ProblemBundleWriter, ProblemBundleReader] {
  override protected def createOriginalObject(): ProblemBundle = {
    ProblemBundle(List(
      SimpleTestData.ProblemA,
      SimpleTestData.ProblemB
    ))
  }


  override protected def createWriter(targetWriter: Writer): ProblemBundleWriter =
    new ProblemBundleWriter(targetWriter)


  override protected def exportObject(writer: ProblemBundleWriter, originalObject: ProblemBundle): Unit =
    writer.writeProblemBundle(originalObject)


  override protected def createReader(sourceReader: BufferedReader): ProblemBundleReader =
    new ProblemBundleReader(sourceReader)


  override protected def importObject(reader: ProblemBundleReader): ProblemBundle =
    reader.readProblemBundle()
}
