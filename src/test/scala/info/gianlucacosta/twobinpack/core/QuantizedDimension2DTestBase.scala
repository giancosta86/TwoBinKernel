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

abstract class QuantizedDimension2DTestBase[T <: QuantizedDimension2D[T]] extends FlatSpec with Matchers {
  protected val horizontalDimension: T
  protected val squareDimension: T
  protected val verticalDimension: T

  protected def createDimension(width: Int, height: Int): T

  "isSquare" should "be true for a square dimension" in {
    squareDimension.isSquare should be(true)
  }

  "isSquare" should "be false for a rectangular dimension" in {
    horizontalDimension.isSquare should be(false)
  }


  "rotation" should "work for a rectangular dimension" in {
    horizontalDimension.rotate() should be(
      createDimension(
        horizontalDimension.height,
        horizontalDimension.width
      )
    )
  }

  "rotation" should "leave a square dimension unaltered" in {
    squareDimension.rotate() should be(squareDimension)
  }


  "area" should "be correct" in {
    createDimension(
      13,
      7
    ).area should be(91)
  }


  "isHorizontal" should "be true for a horizontal dimension" in {
    horizontalDimension.isHorizontal should be(true)
  }

  "isHorizontal" should "be false for a vertical dimension" in {
    verticalDimension.isHorizontal should be(false)
  }

  "isHorizontal" should "be true for a square dimension" in {
    squareDimension.isHorizontal should be(true)
  }
}
