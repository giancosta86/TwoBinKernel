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


object AnchoredBlock {
  /**
    * Creates a block given its top-left point in lieu of its anchor; this is especially useful in a standard
    * rendering coordinate system
    *
    * @param dimension    The block dimension
    * @param topLeftPoint The top-left point. The coordinate system is the standard, 0-based,
    *                     having left-right and top-bottom axes
    * @return The anchored block
    */
  def createFromTopLeft(
                         dimension: BlockDimension,
                         topLeftPoint: QuantizedPoint2D): AnchoredBlock = {
    val anchor =
      QuantizedPoint2D(
        topLeftPoint.left,
        topLeftPoint.top + dimension.height - 1
      )

    AnchoredBlock(
      dimension,
      anchor
    )
  }
}


case class AnchoredBlock(
                          dimension: BlockDimension,
                          anchor: QuantizedPoint2D
                        ) {
  require(
    anchor.left >= 0,
    "Anchor left must be >= 0"
  )

  require(
    anchor.top >= 0,
    "Anchor top must be >= 0"
  )


  @transient
  lazy val top: Int =
    anchor.top - dimension.height + 1

  @transient
  lazy val left =
    anchor.left

  @transient
  lazy val bottom =
    anchor.top

  @transient
  lazy val right =
    anchor.left + dimension.width - 1

  /**
    * Default algorithm to determine if 2 rectangles overlap
    *
    * @param other The other block
    * @return True if the blocks overlap - totally or partially
    */
  def overlaps(other: AnchoredBlock): Boolean =
    (anchor.left < other.anchor.left + other.dimension.width) &&
      (anchor.left + dimension.width > other.anchor.left) &&
      (top < other.top + other.dimension.height) &&
      (top + dimension.height > other.top)


  /**
    * Tests whether the block contains the given quantized point
    *
    * @param point The quantized point
    * @return True if the point is within the block
    */
  def containsPoint(point: QuantizedPoint2D): Boolean =
    (left <= point.left && point.left <= right) &&
      (top <= point.top && point.top <= bottom)
}
