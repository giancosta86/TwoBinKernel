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

/**
  * A user-friendly source of blocks.
  *
  * Unlike BlockPool, if <i>rotation</i> is enabled the gallery provides <strong>both</both>
  * the <i>horizontal</i> and <i>vertical</i> copy of each rectangular block.
  *
  * @param blockPool                  The source block pool
  * @param currentPoolBlockQuantities The quantities (even 0) of the pool-provided blocks currently
  *                                   in the gallery.
  *                                   For a user-friendly, rotation-compatible representation,
  *                                   use <i>availableBlocks</i> instead
  */
case class BlockGallery private(
                                 blockPool: BlockPool,
                                 currentPoolBlockQuantities: Map[BlockDimension, Int]
                               ) {

  def this(blockPool: BlockPool) =
    this(
      blockPool,
      blockPool.blocks
    )


  /**
    * Removes one block from the gallery.
    *
    * Throws an exception when trying to remove a block which is not available.
    *
    * @param dimension The block to remove
    * @return The updated block gallery
    */
  def removeBlock(dimension: BlockDimension): BlockGallery =
    updateQuantity(dimension, -1)


  /**
    * Adds one block to the gallery.
    *
    * Throws and exception when exceeding the block quantity provided by the
    * underlying block pool.
    *
    * @param dimension The block to add
    * @return The updated block gallery
    */
  def addBlock(dimension: BlockDimension): BlockGallery =
    updateQuantity(dimension, +1)


  private def updateQuantity(blockDimension: BlockDimension, quantityDelta: Int): BlockGallery = {
    val poolBlockDimension =
      if (blockPool.canRotateBlocks && !blockDimension.isHorizontal)
        blockDimension.rotate()
      else
        blockDimension


    val quantity =
      currentPoolBlockQuantities(poolBlockDimension)

    val poolQuantity =
      blockPool.blocks(poolBlockDimension)


    val newQuantity =
      quantity + quantityDelta


    require(
      newQuantity >= 0,
      s"The quantity of Block ${blockDimension} cannot be ${newQuantity} < 0"
    )

    require(
      newQuantity <= poolQuantity,
      s"The quantity of Block ${blockDimension} cannot be ${newQuantity} > ${poolQuantity}"
    )


    val newPoolBlockQuantities =
      currentPoolBlockQuantities.updated(
        poolBlockDimension,
        newQuantity
      )


    copy(
      currentPoolBlockQuantities = newPoolBlockQuantities
    )
  }

  /**
    * A map containing a user-friendly view of the available blocks (therefore excluding
    * blocks whose current quantity in the gallery is 0): if
    * rotation is not enabled, it contains a subset of the blocks in the block pool;
    * otherwise, it provides both a horizontal and vertical copy of each
    * rectangular block in the pool.
    */
  @transient
  lazy val availableBlocks: Map[BlockDimension, Int] = {
    val availablePoolBlocks =
      currentPoolBlockQuantities.filter {
        case (dimension, quantity) =>
          quantity > 0
      }

    if (blockPool.canRotateBlocks) {
      val availableVerticalBlocks =
        availablePoolBlocks.map {
          case (dimension, quantity) =>
            dimension.rotate() -> quantity
        }

      availablePoolBlocks ++ availableVerticalBlocks
    } else
      availablePoolBlocks
  }
}
