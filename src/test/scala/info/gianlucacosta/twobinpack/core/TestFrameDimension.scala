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

import info.gianlucacosta.twobinpack.test.SimpleTestData.{HorizontalFrameDimension, SquareFrameDimension, VerticalFrameDimension}

class TestFrameDimension extends QuantizedDimension2DTestBase[FrameDimension] {
  override protected val horizontalDimension: FrameDimension =
    HorizontalFrameDimension

  override protected val squareDimension: FrameDimension =
    SquareFrameDimension

  override protected val verticalDimension: FrameDimension =
    VerticalFrameDimension

  override protected def createDimension(width: Int, height: Int): FrameDimension =
    FrameDimension(
      width,
      height
    )

  "A frame dimension" should "NOT have width == 0" in {
    intercept[IllegalArgumentException] {
      FrameDimension(
        0,
        9
      )
    }
  }


  "A frame dimension" should "NOT have height == 0" in {
    intercept[IllegalArgumentException] {
      FrameDimension(
        17,
        0
      )
    }
  }
}
