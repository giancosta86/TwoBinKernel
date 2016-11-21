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


object BlockDimension {
  val Max: BlockDimension =
    BlockDimension(
      Int.MaxValue,
      Int.MaxValue
    )
}


/**
  * Dimension of a block
  */
case class BlockDimension(
                           width: Int,
                           height: Int
                         ) extends QuantizedDimension2D[BlockDimension] with Ordered[BlockDimension] {

  require(
    width > 0,
    "Block width must be > 0"
  )

  require(
    height > 0,
    "Block height must be > 0"
  )


  override def createNew(width: Int, height: Int): BlockDimension =
    copy(width, height)

  /**
    * To optimize space, block dimensions are sorted first by height, then by width
    *
    * @param that The other dimension
    * @return The comparison result
    */
  override def compare(that: BlockDimension): Int = {
    val heightComparison =
      height.compare(that.height)

    if (heightComparison != 0)
      heightComparison
    else
      width.compare(that.width)
  }
}
