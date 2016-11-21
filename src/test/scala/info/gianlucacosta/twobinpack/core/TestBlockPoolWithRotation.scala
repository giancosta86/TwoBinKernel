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

import info.gianlucacosta.twobinpack.test.SimpleTestData.{HorizontalBlockDimension, SquareBlockDimension}


class TestBlockPoolWithRotation extends BlockPoolTestBase(canRotateBlocks = true) {
  "Creating an empty block pool" should "be allowed" in {
    BlockPool.create(
      canRotateBlocks = true
    )
  }


  "Creating a block pool with a quantity = 0" should "fail" in {
    intercept[IllegalArgumentException] {
      BlockPool.create(
        canRotateBlocks = true,

        HorizontalBlockDimension -> 5,

        SquareBlockDimension -> 0
      )
    }
  }


  "Creating a block pool with a quantity < 0" should "fail" in {
    intercept[IllegalArgumentException] {
      BlockPool.create(
        canRotateBlocks = true,

        HorizontalBlockDimension -> 5,

        SquareBlockDimension -> -6
      )
    }
  }


  "Blocks" should "include only horizontal draft dimensions, but with summed quantities" in {
    TestBlockPool.blocks should be(
      Map(
        HorizontalBlockDimension -> 22,
        SquareBlockDimension -> 13
      )
    )
  }


  "Block dimensions" should "include only horizontal dimensions" in {
    TestBlockPool.blockDimensions should be(
      Set(
        HorizontalBlockDimension,
        SquareBlockDimension
      )
    )
  }


  "Total blocks count" should "count all the draft blocks" in {
    TestBlockPool.totalBlockCount should be(
      TestDraftBlocks.map(_._2).sum
    )
  }
}
