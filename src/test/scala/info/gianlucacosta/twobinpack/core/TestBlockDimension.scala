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

import info.gianlucacosta.twobinpack.test.SimpleTestData.{HorizontalBlockDimension, SquareBlockDimension, VerticalBlockDimension}

class TestBlockDimension extends QuantizedDimension2DTestBase[BlockDimension] {
  override protected val horizontalDimension: BlockDimension =
    HorizontalBlockDimension

  override protected val squareDimension: BlockDimension =
    SquareBlockDimension

  override protected val verticalDimension: BlockDimension =
    VerticalBlockDimension


  override protected def createDimension(width: Int, height: Int): BlockDimension =
    BlockDimension(
      width,
      height
    )


  "A block dimension" should "NOT have width == 0" in {
    intercept[IllegalArgumentException] {
      BlockDimension(
        0,
        9
      )
    }
  }


  "A block dimension" should "NOT have height == 0" in {
    intercept[IllegalArgumentException] {
      BlockDimension(
        17,
        0
      )
    }
  }


  "Block dimensions" should "be sorted" in {
    val smallerHorizontalBlockDimension =
      HorizontalBlockDimension.copy(width = HorizontalBlockDimension.width - 1)

    val biggerVerticalBlockDimension =
      VerticalBlockDimension.copy(height = VerticalBlockDimension.height + 1)

    val biggestSquareBlockDimension =
      BlockDimension(VerticalBlockDimension.height * 10, VerticalBlockDimension.height * 10)

    smallerHorizontalBlockDimension should be < HorizontalBlockDimension

    HorizontalBlockDimension should be < SquareBlockDimension

    SquareBlockDimension should be < VerticalBlockDimension

    VerticalBlockDimension should be < biggerVerticalBlockDimension

    biggerVerticalBlockDimension should be < biggestSquareBlockDimension
  }
}
