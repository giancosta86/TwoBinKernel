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

package info.gianlucacosta.twobinpack.io.bpp

import java.io.{BufferedReader, InputStreamReader, StringWriter}
import java.nio.file.{Files, Paths}
import java.util.UUID

import info.gianlucacosta.twobinpack.core._
import org.scalatest.{FlatSpec, Matchers}

class TestBppSolutionWriter extends FlatSpec with Matchers {
  val problem: Problem = {
    val problemStream =
      getClass.getResourceAsStream(s"testProblem.bpp")


    val problemReader =
      new BppProblemReader(
        new BufferedReader(
          new InputStreamReader(
            problemStream
          )
        )
      )

    try {
      problemReader.readBppProblem(
        1,
        None,
        "BPP problem",
        UUID.randomUUID()
      )
    } finally {
      problemReader.close()
    }
  }


  val solutionBlocks = Set(
    AnchoredBlock(
      BlockDimension(
        1,
        10
      ),

      QuantizedPoint2D(
        0,
        9
      )
    ),

    AnchoredBlock(
      BlockDimension(
        1,
        8
      ),

      QuantizedPoint2D(
        2,
        8
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        5
      ),

      QuantizedPoint2D(
        3,
        4
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        1
      ),

      QuantizedPoint2D(
        3,
        5
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        2
      ),

      QuantizedPoint2D(
        3,
        8
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        4
      ),

      QuantizedPoint2D(
        5,
        5
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        3
      ),

      QuantizedPoint2D(
        5,
        8
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        6
      ),

      QuantizedPoint2D(
        6,
        5
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        9
      ),

      QuantizedPoint2D(
        7,
        9
      )
    ),


    AnchoredBlock(
      BlockDimension(
        1,
        7
      ),

      QuantizedPoint2D(
        8,
        8
      )
    )
  )


  val solution =
    new Solution(
      problem,
      None,
      None,
      solutionBlocks
    )


  "A solution" should "be correctly written to text, including empty bins" in {
    val actualSolutionText = {
      val stringWriter =
        new StringWriter

      val solutionWriter =
        new BppSolutionWriter(stringWriter)


      try {
        solutionWriter.writeSolution(solution)
      } finally {
        solutionWriter.close()
      }

      stringWriter.toString
    }


    val expectedSolutionText = {
      val expectedSolutionFile =
        getClass.getResource("testSolution.bpps").getFile

      new String(
        Files.readAllBytes(
          Paths.get(expectedSolutionFile)
        )
      )
    }

    actualSolutionText should be(expectedSolutionText)
  }
}
