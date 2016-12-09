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
