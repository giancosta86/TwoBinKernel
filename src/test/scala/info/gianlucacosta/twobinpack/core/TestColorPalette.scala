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

import info.gianlucacosta.twobinpack.test.SimpleTestData.{AllBlockDimensions, HorizontalBlockDimension, UniqueBlockDimensions, VerticalBlockDimension}
import org.scalatest.{FlatSpec, Matchers}

import scalafx.scene.paint.Color

class TestColorPalette extends FlatSpec with Matchers {
  private val colorPool =
    List(
      Color.Red,
      Color.Green,
      Color.Blue
    )


  "The returned color" should "be the same for a dimension and its rotated version, if rotation is enabled" in {
    val colorPalette =
      ColorPalette(
        BlockPool.create(
          canRotateBlocks = true,
          AllBlockDimensions
            .map(_ -> Int.MaxValue)
            .toMap
        ),
        colorPool
      )

    colorPalette.getColor(HorizontalBlockDimension) should be(colorPalette.getColor(VerticalBlockDimension))
  }


  "The returned color" should "NOT be the same for a dimension and its rotated version, if rotation is not enabled" in {
    val colorPalette =
      ColorPalette(
        BlockPool.create(
          canRotateBlocks = false,
          AllBlockDimensions
            .map(_ -> Int.MaxValue)
            .toMap
        ),
        colorPool
      )

    colorPalette.getColor(HorizontalBlockDimension) should not be colorPalette.getColor(VerticalBlockDimension)
  }


  "Requesting the color of a rotated dimension" should "fail is rotation is not enabled" in {
    val colorPalette =
      ColorPalette(
        BlockPool.create(
          canRotateBlocks = false,
          UniqueBlockDimensions
            .map(_ -> Int.MaxValue)
            .toMap
        ),
        colorPool
      )

    intercept[NoSuchElementException] {
      colorPalette.getColor(VerticalBlockDimension)
    }
  }
}
