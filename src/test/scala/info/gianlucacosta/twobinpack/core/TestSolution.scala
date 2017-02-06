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
import info.gianlucacosta.twobinpack.test.SimpleTestData
import org.scalatest.{FlatSpec, Matchers}

class TestSolution extends FlatSpec with Matchers {
  "A solution" should "NOT include rotated blocks when rotation is disabled" in {
    val problem =
      SimpleTestData.ProblemB

    val blockDimension =
      BlockDimension(
        8,
        1
      )

    val rotatedDimension =
      blockDimension.rotate()

    require(!problem.frameTemplate.blockPool.canRotateBlocks)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))
    require(!problem.frameTemplate.blockPool.blockDimensions.contains(rotatedDimension))

    intercept[NoSuchElementException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            rotatedDimension,
            QuantizedPoint2D(
              1,
              9
            )
          )
        )
      )
    }
  }


  "A solution" should "NOT include blocks not in the gallery" in {
    val problem =
      SimpleTestData.ProblemB

    val blockDimension =
      BlockDimension(
        6,
        7
      )

    require(!problem.frameTemplate.blockPool.canRotateBlocks)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(!problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[NoSuchElementException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              1,
              9
            )
          )
        )
      )
    }
  }


  "A solution" should "accept rotated blocks when rotation is enabled" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    val rotatedDimension =
      blockDimension.rotate()

    require(problem.frameTemplate.blockPool.canRotateBlocks)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))
    require(!problem.frameTemplate.blockPool.blockDimensions.contains(rotatedDimension))

    Solution(
      problem,
      None,
      None,
      Set(
        AnchoredBlock(
          rotatedDimension,
          QuantizedPoint2D(
            1,
            9
          )
        )
      )
    )
  }


  "A solution" should "consider only one block in a set of totally overlapping blocks" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    Solution(
      problem,
      None,
      None,
      Set(
        AnchoredBlock(
          blockDimension,
          QuantizedPoint2D(
            6,
            5
          )
        ),

        AnchoredBlock(
          blockDimension,
          QuantizedPoint2D(
            6,
            5
          )
        )
      )
    )
  }


  "A solution" should "NOT accept partially overlapping blocks" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              6,
              5
            )
          ),

          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              7,
              7
            )
          )
        )
      )
    }
  }


  "A solution" should "NOT allow adding more blocks than the quantity provided by the problem" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))
    require(problem.frameTemplate.blockPool.blocks(blockDimension) == 2)

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              0,
              5
            )
          ),

          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              6,
              5
            )
          ),

          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              0,
              9
            )
          )
        )
      )
    }
  }


  "A Knapsack solution" should "NOT accept blocks whose anchor is after the frame's bottom edge" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        1,
        1
      )

    require(problem.frameTemplate.frameMode == FrameMode.Knapsack)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height == 15)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              1,
              15
            )
          )
        )
      )
    }
  }


  "A Strip solution" should "NOT accept blocks whose anchor is after the frame's bottom edge" in {
    val problem =
      SimpleTestData.ProblemB

    val blockDimension =
      BlockDimension(
        1,
        1
      )

    require(problem.frameTemplate.frameMode == FrameMode.Strip)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height == 12)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              1,
              12
            )
          )
        )
      )
    }
  }


  "A Knapsack solution" should "NOT accept blocks overflowing the frame's top edge" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    require(problem.frameTemplate.frameMode == FrameMode.Knapsack)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              1,
              1
            )
          )
        )
      )
    }
  }


  "A Strip solution" should "NOT accept blocks overflowing the frame's top edge" in {
    val problem =
      SimpleTestData.ProblemA
        .copy(frameTemplate = SimpleTestData.ProblemA.frameTemplate.copy(
          frameMode = FrameMode.Strip
        ))

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    require(problem.frameTemplate.frameMode == FrameMode.Strip)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              1,
              1
            )
          )
        )
      )
    }
  }


  "A Knapsack solution" should "NOT accept blocks overflowing the frame's right edge" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    require(problem.frameTemplate.frameMode == FrameMode.Knapsack)
    require(problem.frameTemplate.initialDimension.width == 20)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              18,
              5
            )
          )
        )
      )
    }
  }


  "A Strip solution" should "accept blocks overflowing the frame's right edge" in {
    val problem =
      SimpleTestData.ProblemB

    val blockDimension =
      BlockDimension(
        8,
        1
      )

    require(problem.frameTemplate.frameMode == FrameMode.Strip)
    require(problem.frameTemplate.initialDimension.width == 15)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    Solution(
      problem,
      None,
      None,
      Set(
        AnchoredBlock(
          blockDimension,
          QuantizedPoint2D(
            12,
            5
          )
        )
      )
    )
  }


  "A Knapsack solution" should "NOT accept blocks whose anchor is after the frame's right edge" in {
    val problem =
      SimpleTestData.ProblemA

    val blockDimension =
      BlockDimension(
        4,
        3
      )

    require(problem.frameTemplate.frameMode == FrameMode.Knapsack)
    require(problem.frameTemplate.initialDimension.width == 20)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            blockDimension,
            QuantizedPoint2D(
              30,
              5
            )
          )
        )
      )
    }
  }


  "A Strip solution" should "accept blocks whose anchor is after the frame's right edge" in {
    val problem =
      SimpleTestData.ProblemB

    val blockDimension =
      BlockDimension(
        8,
        1
      )

    require(problem.frameTemplate.frameMode == FrameMode.Strip)
    require(problem.frameTemplate.initialDimension.width == 15)
    require(problem.frameTemplate.initialDimension.height >= 10)
    require(problem.frameTemplate.blockPool.blockDimensions.contains(blockDimension))

    Solution(
      problem,
      None,
      None,
      Set(
        AnchoredBlock(
          blockDimension,
          QuantizedPoint2D(
            30,
            5
          )
        )
      )
    )
  }


  "An empty Knapsack solution" should "have a target equal to the initial frame area" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.frameTemplate.frameMode == FrameMode.Knapsack)

    val solution =
      Solution(
        problem,
        None,
        None,
        Set()
      )

    solution.target should be(Some(problem.frameTemplate.initialDimension.area))
  }


  "An empty Strip solution" should "have undefined target" in {
    val problem =
      SimpleTestData.ProblemB

    require(problem.frameTemplate.frameMode == FrameMode.Strip)
    require(problem.frameTemplate.blockPool.totalBlockCount > 0)

    val solution =
      Solution(
        problem,
        None,
        None,
        Set()
      )

    solution.target should be(None)
  }


  "A Knapsack solution" should "have target equal to the area not covered by blocks" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.frameTemplate.frameMode == FrameMode.Knapsack)
    require(problem.frameTemplate.initialDimension.width >= 10)
    require(problem.frameTemplate.initialDimension.height >= 10)

    val solution =
      Solution(
        problem,
        None,
        None,
        Set(
          AnchoredBlock(
            BlockDimension(
              4,
              3
            ),

            QuantizedPoint2D(
              5,
              5
            )
          ),

          AnchoredBlock(
            BlockDimension(
              1,
              1
            ),

            QuantizedPoint2D(
              0,
              0
            )
          ),

          AnchoredBlock(
            BlockDimension(
              1,
              1
            ),

            QuantizedPoint2D(
              2,
              2
            )
          )
        )
      )

    solution.target should be(
      Some(
        problem.frameTemplate.initialDimension.area - (4 * 3 + 1 + 1)
      )
    )
  }


  "A Strip solution" should "still have undefined target if not all the blocks have been anchored" in {
    val problem =
      SimpleTestData.ProblemB


    require(problem.frameTemplate.frameMode == FrameMode.Strip)
    require(problem.frameTemplate.initialDimension.height >= 10)


    val blocks =
      Set(
        AnchoredBlock(
          BlockDimension(
            1,
            1
          ),

          QuantizedPoint2D(
            2,
            2
          )
        ),

        AnchoredBlock(
          BlockDimension(
            8,
            1
          ),

          QuantizedPoint2D(
            3,
            3
          )
        ),

        AnchoredBlock(
          BlockDimension(
            8,
            1
          ),

          QuantizedPoint2D(
            4,
            4
          )
        ),

        AnchoredBlock(
          BlockDimension(
            8,
            1
          ),

          QuantizedPoint2D(
            10,
            1
          )
        )
      )

    val solution =
      Solution(
        problem,
        None,
        None,
        blocks
      )

    require(blocks.size == problem.frameTemplate.blockPool.totalBlockCount - 1)
    solution.target should be(None)
  }


  "A Strip solution" should "have target equal to the actual length of required ribbon" in {
    val problem =
      SimpleTestData.ProblemB


    require(problem.frameTemplate.frameMode == FrameMode.Strip)
    require(problem.frameTemplate.initialDimension.height >= 10)


    val blocks =
      Set(
        AnchoredBlock(
          BlockDimension(
            1,
            1
          ),

          QuantizedPoint2D(
            0,
            0
          )
        ),

        AnchoredBlock(
          BlockDimension(
            1,
            1
          ),

          QuantizedPoint2D(
            2,
            2
          )
        ),

        AnchoredBlock(
          BlockDimension(
            8,
            1
          ),

          QuantizedPoint2D(
            3,
            3
          )
        ),

        AnchoredBlock(
          BlockDimension(
            8,
            1
          ),

          QuantizedPoint2D(
            4,
            4
          )
        ),

        AnchoredBlock(
          BlockDimension(
            8,
            1
          ),

          QuantizedPoint2D(
            10,
            1
          )
        )
      )

    val solution =
      Solution(
        problem,
        None,
        None,
        blocks
      )


    require(blocks.size == problem.frameTemplate.blockPool.totalBlockCount)
    require(blocks.map(_.right).max == 17)
    solution.target should be(Some(18))
  }


  "A solution having elapsed time equal to 0" should "be valid" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.timeLimitOption.nonEmpty)

    Solution(
      problem,
      None,
      Some(Duration.ZERO),
      Set()
    )
  }


  "A solution having negative elapsed time" should "NOT be valid" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.timeLimitOption.nonEmpty)

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        Some(Duration.ofMinutes(-2)),
        Set()
      )
    }
  }


  "A solution having no elapsed time and problem with time limit defined" should "be valid" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.timeLimitOption.nonEmpty)

    Solution(
      problem,
      None,
      None,
      Set()
    )
  }


  "A solution having no elapsed time and problem with no time limit" should "be valid" in {
    val problem =
      SimpleTestData.ProblemB

    require(problem.timeLimitOption.isEmpty)

    Solution(
      problem,
      None,
      None,
      Set()
    )
  }


  "A solution having elapsed time defined and problem with no time limit" should "be valid" in {
    val problem =
      SimpleTestData.ProblemB

    require(problem.timeLimitOption.isEmpty)

    Solution(
      problem,
      None,
      Some(Duration.ofSeconds(42)),
      Set()
    )
  }


  "A solution having elapsed time greater than the problem's time limit" should "NOT be valid" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.timeLimitOption.nonEmpty)

    intercept[IllegalArgumentException] {
      Solution(
        problem,
        None,
        Some(problem.timeLimitOption.get + Duration.ofSeconds(1)),
        Set()
      )
    }
  }


  "A solution having elapsed time less than the problem's time limit" should "be valid" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.timeLimitOption.nonEmpty)
    require(problem.timeLimitOption.get.getSeconds > 2)

    Solution(
      problem,
      None,
      Some(problem.timeLimitOption.get - Duration.ofSeconds(1)),
      Set()
    )
  }


  "A solution having elapsed time equal to the problem's time limit" should "be valid" in {
    val problem =
      SimpleTestData.ProblemA

    require(problem.timeLimitOption.nonEmpty)
    require(problem.timeLimitOption.get > Duration.ZERO)

    Solution(
      problem,
      None,
      Some(problem.timeLimitOption.get),
      Set()
    )
  }
}
