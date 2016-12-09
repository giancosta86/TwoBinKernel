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

package info.gianlucacosta.twobinpack.io.csv.v2

import info.gianlucacosta.twobinpack.core.Solution
import info.gianlucacosta.twobinpack.test.SimpleTestData

class TestSolutionCsvWriterReaderV2_WithNoneElapsedTime extends SolutionCsvWriterReaderV2TestBase {
  override protected def createOriginalObject(): Solution =
    SimpleTestData.SolutionA1.copy(elapsedTimeOption = None)
}
