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

package info.gianlucacosta.twobinpack.core

import info.gianlucacosta.twobinpack.test.SimpleTestData.{ProblemA, ProblemB}
import org.scalatest.{FlatSpec, Matchers}

class TestProblem extends FlatSpec with Matchers {
  "Problems" should "be sorted by name" in {
    require(ProblemA.name == "ProblemA")
    require(ProblemB.name == "ProblemB")

    val problemE =
      ProblemA.copy(name = "ProblemE")

    val problemC =
      ProblemA.copy(name = "ProblemC")

    val problemD =
      ProblemA.copy(name = "ProblemD")

    ProblemA should be < ProblemB
    ProblemB should be < problemC
    problemC should be < problemD
    problemD should be < problemE
  }


  "Time limit in seconds" should "be correct if time limit in minutes is defined" in {
    require(ProblemA.timeLimitInMinutesOption.isDefined)

    ProblemA.timeLimitInSecondsOption should be(
      Some(ProblemA.timeLimitInMinutesOption.get * 60)
    )
  }


  "Time limit in seconds" should "be None if time limit in minutes is None" in {
    require(ProblemB.timeLimitInMinutesOption.isEmpty)

    ProblemB.timeLimitInSecondsOption should be(None)
  }
}
