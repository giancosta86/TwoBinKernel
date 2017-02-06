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

import org.scalatest.{FlatSpec, Matchers}


class TestAnchoredBlock extends FlatSpec with Matchers {
  private val coordinatesTestBlock =
    AnchoredBlock(
      BlockDimension(
        3,
        5
      ),

      QuantizedPoint2D(
        7,
        6
      )
    )


  "Top" should "be within the block" in {
    coordinatesTestBlock.top should be(2)
  }


  "Right" should "be within the block" in {
    coordinatesTestBlock.right should be(9)
  }


  "Bottom" should "be within the block" in {
    coordinatesTestBlock.bottom should be(6)
  }


  "Left" should "be within the block" in {
    coordinatesTestBlock.left should be(7)
  }


  "The anchor left" should "NOT be negative" in {
    intercept[IllegalArgumentException] {
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),

        QuantizedPoint2D(
          -2,
          6
        )
      )
    }
  }


  "The anchor top" should "NOT be negative" in {
    intercept[IllegalArgumentException] {
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),

        QuantizedPoint2D(
          7,
          -4
        )
      )
    }
  }


  "A block" should "overlap itself" in {
    var block =
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),
        QuantizedPoint2D(
          10,
          10
        )
      )

    block.overlaps(block) should be(true)
  }


  "Two detached blocks" should "not overlap" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),
        QuantizedPoint2D(
          10,
          10
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),
        QuantizedPoint2D(
          20,
          20
        )
      )


    blockA.overlaps(blockB) should be(false)
    blockB.overlaps(blockA) should be(false)
  }

  "Horizontally adjacent blocks" should "not overlap" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          2,
          1
        ),
        QuantizedPoint2D(
          0,
          9
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),
        QuantizedPoint2D(
          2,
          9
        )
      )


    blockA.overlaps(blockB) should be(false)
    blockB.overlaps(blockA) should be(false)
  }


  "Vertically adjacent blocks" should "not overlap" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          1,
          2
        ),
        QuantizedPoint2D(
          9,
          1
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          1,
          3
        ),
        QuantizedPoint2D(
          9,
          4
        )
      )


    blockA.overlaps(blockB) should be(false)
    blockB.overlaps(blockA) should be(false)
  }


  "Partially overlapping blocks" should "be identified" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          5,
          4
        ),
        QuantizedPoint2D(
          11,
          11
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          8,
          2
        ),
        QuantizedPoint2D(
          14,
          12
        )
      )


    blockA.overlaps(blockB) should be(true)
    blockB.overlaps(blockA) should be(true)
  }


  "Blocks one inside the other" should "overlap" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          5,
          4
        ),
        QuantizedPoint2D(
          11,
          11
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          3,
          2
        ),
        QuantizedPoint2D(
          12,
          10
        )
      )


    blockA.overlaps(blockB) should be(true)
    blockB.overlaps(blockA) should be(true)
  }


  "Crossed blocks" should "overlap" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          5,
          1
        ),
        QuantizedPoint2D(
          10,
          5
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          1,
          5
        ),
        QuantizedPoint2D(
          12,
          7
        )
      )


    blockA.overlaps(blockB) should be(true)
    blockB.overlaps(blockA) should be(true)
  }


  "Blocks sharing the same left and overlapping" should "be identified" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          5,
          5
        ),
        QuantizedPoint2D(
          13,
          13
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          5,
          5
        ),
        QuantizedPoint2D(
          13,
          14
        )
      )


    blockA.overlaps(blockB) should be(true)
    blockB.overlaps(blockA) should be(true)
  }


  "Blocks sharing the same top and overlapping" should "be identified" in {
    var blockA =
      AnchoredBlock(
        BlockDimension(
          5,
          5
        ),
        QuantizedPoint2D(
          13,
          13
        )
      )

    var blockB =
      AnchoredBlock(
        BlockDimension(
          5,
          5
        ),
        QuantizedPoint2D(
          14,
          13
        )
      )


    blockA.overlaps(blockB) should be(true)
    blockB.overlaps(blockA) should be(true)
  }


  "Creation from top left angle" should "work" in {
    val block =
      AnchoredBlock.createFromTopLeft(
        BlockDimension(
          13,
          4
        ),

        QuantizedPoint2D(
          8,
          2
        )
      )

    block should be(AnchoredBlock(
      BlockDimension(
        13,
        4
      ),

      QuantizedPoint2D(
        8,
        5
      )
    ))
  }


  "A 1x1 block" should "contain its anchor" in {
    val blockAnchor =
      QuantizedPoint2D(
        6,
        19
      )

    val block =
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),

        blockAnchor
      )

    block.containsPoint(blockAnchor) should be(true)
  }


  "A 1x1 block" should "NOT contain external points" in {
    val blockAnchor =
      QuantizedPoint2D(
        6,
        19
      )

    val block =
      AnchoredBlock(
        BlockDimension(
          1,
          1
        ),

        blockAnchor
      )

    block.containsPoint(
      blockAnchor.copy(
        left =
          blockAnchor.left + 1
      )
    ) should be(false)
  }


  "A rectangular block" should "contain its anchor" in {
    val blockAnchor =
      QuantizedPoint2D(
        6,
        19
      )

    val block =
      AnchoredBlock(
        BlockDimension(
          8,
          7
        ),

        blockAnchor
      )

    block.containsPoint(
      blockAnchor
    ) should be(true)
  }


  "A rectangular block" should "contain an edge point" in {
    val blockAnchor =
      QuantizedPoint2D(
        6,
        19
      )

    val block =
      AnchoredBlock(
        BlockDimension(
          8,
          7
        ),

        blockAnchor
      )

    block.containsPoint(
      blockAnchor.copy(
        left =
          blockAnchor.left + 3
      )
    ) should be(true)
  }


  "A rectangular block" should "contain an internal point" in {
    val blockAnchor =
      QuantizedPoint2D(
        6,
        19
      )

    val block =
      AnchoredBlock(
        BlockDimension(
          8,
          7
        ),

        blockAnchor
      )

    block.containsPoint(
      blockAnchor.copy(
        left =
          blockAnchor.left + 3,

        top =
          blockAnchor.top - 4
      )
    ) should be(true)
  }


  "A rectangular block" should "NOT contain an external point" in {
    val blockAnchor =
      QuantizedPoint2D(
        6,
        19
      )

    val block =
      AnchoredBlock(
        BlockDimension(
          8,
          7
        ),

        blockAnchor
      )

    block.containsPoint(
      blockAnchor.copy(
        left =
          blockAnchor.left - 3
      )
    ) should be(false)
  }
}
