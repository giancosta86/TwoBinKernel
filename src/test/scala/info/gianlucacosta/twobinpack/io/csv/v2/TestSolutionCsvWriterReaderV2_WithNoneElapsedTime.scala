package info.gianlucacosta.twobinpack.io.csv.v2

import info.gianlucacosta.twobinpack.core.Solution
import info.gianlucacosta.twobinpack.test.SimpleTestData

class TestSolutionCsvWriterReaderV2_WithNoneElapsedTime extends SolutionCsvWriterReaderV2TestBase {
  override protected def createOriginalObject(): Solution =
    SimpleTestData.SolutionA1.copy(elapsedTimeOption = None)
}
