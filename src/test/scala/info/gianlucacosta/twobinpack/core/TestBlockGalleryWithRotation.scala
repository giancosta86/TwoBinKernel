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

import info.gianlucacosta.twobinpack.test.SimpleTestData.{HorizontalBlockDimension, SquareBlockDimension, VerticalBlockDimension}
import org.scalatest.{FlatSpec, Matchers}


class TestBlockGalleryWithRotation extends FlatSpec with Matchers {
  private val galleryWithoutRotatedBlocks =
    new BlockGallery(
      BlockPool.create(
        canRotateBlocks = true,

        HorizontalBlockDimension -> 210,

        SquareBlockDimension -> 220
      )
    )


  private val galleryWithRotatedBlocks =
    new BlockGallery(
      BlockPool.create(
        canRotateBlocks = true,

        HorizontalBlockDimension -> 230,

        SquareBlockDimension -> 240,

        VerticalBlockDimension -> 250
      )
    )


  "The gallery without rotated blocks" should "list the rotated blocks as well" in {
    galleryWithoutRotatedBlocks
      .availableBlocks should be(Map(
      HorizontalBlockDimension -> 210,

      SquareBlockDimension -> 220,

      VerticalBlockDimension -> 210
    ))
  }


  "The gallery with rotated blocks" should "sum the quantity of corresponding rotated blocks" in {
    galleryWithRotatedBlocks
      .availableBlocks should be(Map(
      HorizontalBlockDimension -> 480,

      SquareBlockDimension -> 240,

      VerticalBlockDimension -> 480
    ))
  }


  "The gallery without rotated blocks" should "decrease the quantity of both related block dimensions" in {
    galleryWithoutRotatedBlocks.removeBlock(
      HorizontalBlockDimension
    ).availableBlocks should be(Map(
      HorizontalBlockDimension -> 209,

      SquareBlockDimension -> 220,

      VerticalBlockDimension -> 209
    ))
  }


  "The gallery with rotated blocks" should "decrease the quantity of both related block dimensions" in {
    galleryWithRotatedBlocks.removeBlock(
      HorizontalBlockDimension
    ).availableBlocks should be(Map(
      HorizontalBlockDimension -> 479,

      SquareBlockDimension -> 240,

      VerticalBlockDimension -> 479
    ))
  }


  "The gallery without rotated blocks" should "not decrease a quantity below 0" in {
    val galleryWithNoHorizontalBlocks =
      Range(0, 210).foldLeft(galleryWithoutRotatedBlocks) {
        case (latestGallery, _) =>
          latestGallery.removeBlock(HorizontalBlockDimension)
      }

    intercept[IllegalArgumentException] {
      galleryWithNoHorizontalBlocks.removeBlock(
        HorizontalBlockDimension
      )
    }
  }

  "The gallery with rotated blocks" should "not decrease a quantity below 0" in {
    val galleryWithNoHorizontalBlocks =
      Range(0, 480).foldLeft(galleryWithRotatedBlocks) {
        case (latestGallery, _) =>
          latestGallery.removeBlock(HorizontalBlockDimension)
      }

    intercept[IllegalArgumentException] {
      galleryWithNoHorizontalBlocks.removeBlock(
        HorizontalBlockDimension
      )
    }
  }


  "The gallery without rotated blocks" should "increase the quantity of both related block dimensions" in {
    galleryWithoutRotatedBlocks
      .removeBlock(
        HorizontalBlockDimension
      )
      .removeBlock(
        HorizontalBlockDimension
      )
      .addBlock(
        HorizontalBlockDimension
      ).availableBlocks should be(Map(
      HorizontalBlockDimension -> 209,

      SquareBlockDimension -> 220,

      VerticalBlockDimension -> 209
    ))
  }


  "The gallery with rotated blocks" should "increase the quantity of both related block dimensions" in {
    galleryWithRotatedBlocks
      .removeBlock(
        HorizontalBlockDimension
      ).removeBlock(
      HorizontalBlockDimension
    )
      .addBlock(
        HorizontalBlockDimension
      ).availableBlocks should be(Map(
      HorizontalBlockDimension -> 479,

      SquareBlockDimension -> 240,

      VerticalBlockDimension -> 479
    ))
  }


  "The gallery without rotated blocks" should "not increase a quantity above the initial level" in {
    intercept[IllegalArgumentException] {
      galleryWithoutRotatedBlocks.addBlock(
        HorizontalBlockDimension
      )
    }
  }

  "The gallery with rotated blocks" should "not increase a quantity above the initial level" in {
    intercept[IllegalArgumentException] {
      galleryWithRotatedBlocks.addBlock(
        HorizontalBlockDimension
      )
    }
  }


  "The gallery without rotated blocks" should "decrease the quantity of a square block once only" in {
    galleryWithoutRotatedBlocks
      .removeBlock(SquareBlockDimension)
      .availableBlocks should be(Map(
      HorizontalBlockDimension -> 210,

      SquareBlockDimension -> 219,

      VerticalBlockDimension -> 210
    ))
  }


  "The gallery with rotated blocks" should "decrease the quantity of a square block once only" in {
    galleryWithRotatedBlocks
      .removeBlock(SquareBlockDimension)
      .availableBlocks should be(Map(
      HorizontalBlockDimension -> 480,

      SquareBlockDimension -> 239,

      VerticalBlockDimension -> 480
    ))
  }


  "The gallery without rotated blocks" should "increase the quantity of a square block once only" in {
    galleryWithoutRotatedBlocks
      .removeBlock(SquareBlockDimension)
      .removeBlock(SquareBlockDimension)
      .addBlock(SquareBlockDimension)
      .availableBlocks should be(Map(
      HorizontalBlockDimension -> 210,

      SquareBlockDimension -> 219,

      VerticalBlockDimension -> 210
    ))
  }


  "The gallery with rotated blocks" should "increase the quantity of a square block once only" in {
    galleryWithRotatedBlocks
      .removeBlock(SquareBlockDimension)
      .removeBlock(SquareBlockDimension)
      .addBlock(SquareBlockDimension)
      .availableBlocks should be(Map(
      HorizontalBlockDimension -> 480,

      SquareBlockDimension -> 239,

      VerticalBlockDimension -> 480
    ))
  }
}
