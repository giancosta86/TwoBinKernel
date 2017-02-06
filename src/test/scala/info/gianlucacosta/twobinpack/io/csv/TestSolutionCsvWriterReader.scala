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

package info.gianlucacosta.twobinpack.io.csv

import java.io.{BufferedReader, StringReader, StringWriter, Writer}

import info.gianlucacosta.twobinpack.core.Solution
import info.gianlucacosta.twobinpack.io.WriterReaderTestBase
import info.gianlucacosta.twobinpack.test.SimpleTestData

class TestSolutionCsvWriterReader extends WriterReaderTestBase[Solution, SolutionCsvWriter, SolutionCsvReader] {
  override protected def createOriginalObject(): Solution = {
    SimpleTestData.SolutionA1
  }


  override protected def createWriter(targetWriter: Writer): SolutionCsvWriter =
    new SolutionCsvWriter(targetWriter)


  override protected def exportObject(writer: SolutionCsvWriter, originalObject: Solution): Unit =
    writer.writeSolution(originalObject)


  override protected def createReader(sourceReader: BufferedReader): SolutionCsvReader =
    new SolutionCsvReader(
      sourceReader,
      (problemId, solutionId) => SimpleTestData.ProblemA
    )


  override protected def importObject(reader: SolutionCsvReader): Solution =
    reader.readSolution().get


  "Reading twice from a reader holding two solutions" should "return both solutions" in {
    val problem =
      SimpleTestData.ProblemA


    val solution1 =
      SimpleTestData.SolutionA1

    val solution2 =
      SimpleTestData.SolutionA2

    val stringWriter =
      new StringWriter

    val solutionWriter =
      new SolutionCsvWriter(stringWriter)


    solutionWriter.writeSolution(solution1)
    solutionWriter.writeSolution(solution2)

    solutionWriter.close()


    val solutionReader =
      new SolutionCsvReader(
        new BufferedReader(
          new StringReader(
            stringWriter.toString
          )
        ),

        (_, _) => problem
      )


    val firstRetrievedSolution =
      solutionReader.readSolution()

    firstRetrievedSolution should be(Some(solution1))


    val secondRetrievedSolution =
      solutionReader.readSolution()

    secondRetrievedSolution should be(Some(solution2))
  }


  "Reading twice from a reader holding just one solution" should "return None the second time" in {
    val problem =
      SimpleTestData.ProblemB

    val solution =
      SimpleTestData.SolutionB1

    val stringWriter =
      new StringWriter

    val solutionWriter =
      new SolutionCsvWriter(stringWriter)


    solutionWriter.writeSolution(solution)

    solutionWriter.close()


    val solutionReader =
      new SolutionCsvReader(
        new BufferedReader(
          new StringReader(
            stringWriter.toString
          )
        ),

        (_, _) => problem
      )

    val firstRetrievedSolution =
      solutionReader.readSolution()

    firstRetrievedSolution should be(Some(solution))


    val secondRetrievedSolution =
      solutionReader.readSolution()

    secondRetrievedSolution should be(None)
  }
}
