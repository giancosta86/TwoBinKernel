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

package info.gianlucacosta.twobinpack.core

import java.time.Duration

import info.gianlucacosta.helios.Includes._
import info.gianlucacosta.twobinpack.test.SimpleTestData.{BppProblem, ProblemA, ProblemB}
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


  "A problem having negative time limit" should "NOT be valid" in {
    intercept[IllegalArgumentException] {
      ProblemA.copy(
        timeLimitOption =
          Some(Duration.ofMinutes(-2))
      )
    }
  }


  "A problem having time limit equal to zero" should "NOT be valid" in {
    intercept[IllegalArgumentException] {
      ProblemA.copy(
        timeLimitOption =
          Some(Duration.ZERO)
      )
    }
  }


  "A problem having time limit exceeding the maximum value" should "NOT be valid" in {
    intercept[IllegalArgumentException] {
      ProblemA.copy(
        timeLimitOption =
          Some(Problem.MaxTimeLimit + Duration.ofSeconds(1))
      )
    }
  }


  "A problem having positive time limit" should "be valid" in {
    require(ProblemB.timeLimitOption.isEmpty)

    ProblemB.copy(
      timeLimitOption =
        Some(Duration.ofMinutes(5))
    )
  }


  "A problem with at least one block having width > 1" should "NOT be BPP" in {
    val problem =
      ProblemA

    require(problem.frameTemplate.blockPool.blockDimensions.exists(_.width > 1))

    problem.isBinPacking should be(false)
  }


  "The test BPP problem" should "be detected as BPP" in {
    val problem =
      BppProblem

    problem.isBinPacking should be(true)
  }


  "A problem in Knapsack mode" should "NOT be BPP" in {
    val newFrameTemplate =
      BppProblem.frameTemplate.copy(
        frameMode = FrameMode.Knapsack
      )


    val problem =
      BppProblem.copy(
        frameTemplate =
          newFrameTemplate
      )


    problem.isBinPacking should be(false)
  }


  "A problem with rotation enabled" should "NOT be BPP" in {
    val newBlockProol =
      BppProblem.frameTemplate.blockPool.copy(
        canRotateBlocks = true
      )

    val newFrameTemplate =
      BppProblem.frameTemplate.copy(
        blockPool = newBlockProol
      )


    val problem =
      BppProblem.copy(
        frameTemplate = newFrameTemplate
      )

    require(problem.frameTemplate.blockPool.canRotateBlocks)

    problem.isBinPacking should be(false)
  }


  "A problem with initial width > 1" should "still be BPP" in {
    val newFrameTemplate =
      BppProblem.frameTemplate.copy(
        initialDimension =
          FrameDimension(
            20,
            15
          )
      )


    val problem =
      BppProblem.copy(
        frameTemplate = newFrameTemplate
      )

    problem.isBinPacking should be(true)
  }
}
