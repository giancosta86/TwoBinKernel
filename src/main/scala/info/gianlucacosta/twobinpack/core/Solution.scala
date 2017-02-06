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

import java.time.Duration
import java.util.UUID

import info.gianlucacosta.helios.Includes._


object Solution {
  /**
    * Suitably formats the target
    *
    * @param target The target
    * @return A user-friendly string representation of the target
    */
  def formatTarget(target: Option[Int]): String = {
    target match {
      case Some(value) =>
        value.toString
      case None =>
        "(not defined)"
    }
  }
}


/**
  * Solution to a two-dimensional packing problem.
  *
  * On construction, it ensures its block actually define a valid solution to the given problem.
  *
  * @param problem           The problem instance
  * @param solverOption      The optional nickname of the solver
  * @param elapsedTimeOption The time required to reach this solution
  * @param blocks            The blocks composing the solution
  * @param id                The solution's unique identifier
  */
case class Solution(
                     problem: Problem,
                     solverOption: Option[String],
                     elapsedTimeOption: Option[Duration],
                     blocks: Set[AnchoredBlock],
                     id: UUID = UUID.randomUUID()
                   ) extends Ordered[Solution] {
  elapsedTimeOption.foreach(elapsedTime => {
    require(
      elapsedTime >= Duration.ZERO,
      "The elapsed time cannot be negative"
    )

    problem.timeLimitOption.foreach(problemTimeLimit =>
      require(
        elapsedTime <= problemTimeLimit,
        "The elapsedTime cannot be exceed the problem's time limit"
      )
    )
  })


  blocks.foreach(leftBlock =>
    blocks.foreach(rightBlock =>
      require(
        (leftBlock == rightBlock) || !leftBlock.overlaps(rightBlock),
        s"Two blocks overlap in the solution!\n--${leftBlock}\n--${rightBlock}"
      )
    )
  )


  {
    val solutionBlockQuantities: Map[BlockDimension, Int] =
      blocks
        .toList
        .map(block =>
          block.dimension
        )
        .groupBy(dimension =>
          dimension
        )
        .map {
          case (blockDimension, groupedBlocks) =>
            blockDimension -> groupedBlocks.size
        }

    solutionBlockQuantities.foreach {
      case (blockDimension, quantity) =>
        require(
          quantity <= problem.frameTemplate.blockGallery.availableBlocks(blockDimension),
          "A solution cannot have more blocks than allowed by the problem"
        )
    }


    blocks.foreach(block => {
      problem.frameTemplate.frameMode match {
        case FrameMode.Knapsack =>
          require(
            block.right < problem.frameTemplate.initialDimension.width,
            "In a Knapsack solution, every block cannot bypass the right edge of the frame"
          )

        case FrameMode.Strip =>
        //Just do nothing
      }


      require(
        block.top >= 0,
        "The top of a block cannot bypass the top edge of the frame"
      )

      require(
        block.bottom < problem.frameTemplate.initialDimension.height,
        "The bottom of a block cannot bypass the bottom edge of the frame"
      )
    })
  }


  @transient
  lazy val target: Option[Int] = {
    val frameTemplate =
      problem.frameTemplate

    frameTemplate.frameMode match {
      case FrameMode.Knapsack =>
        val coveredArea =
          blocks
            .toList
            .map(_.dimension.area)
            .sum

        val waste =
          frameTemplate.initialDimension.area - coveredArea

        Some(waste)

      case FrameMode.Strip =>
        if (blocks.size == frameTemplate.blockPool.totalBlockCount)
          Some(
            blocks
              .map(block => {
                val blockRightOrdinal =
                  block.right + 1

                blockRightOrdinal
              })
              .max
          )
        else
          None
    }
  }


  @transient
  lazy val displayName: String =
    solverOption
      .map(solver =>
        s"${solver} {${id}}"
      )
      .getOrElse(
        s"{${id}}"
      )


  override def compare(that: Solution): Int =
    displayName.compare(that.displayName)


  override def toString: String =
    displayName
}
