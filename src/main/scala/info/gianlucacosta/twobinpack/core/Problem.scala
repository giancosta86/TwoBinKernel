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

import java.time.Duration
import java.util.UUID

import info.gianlucacosta.helios.Includes._

object Problem {
  val MaxTimeLimit =
    Duration.ofDays(1)

  val MinResolution =
    1

  val MaxResolution =
    100

  val SuggestedResolution =
    30


  /**
    * Provides a suggested name for a problem given a few of its parameters
    *
    * @param frameTemplate The frame template
    * @return The suggested name
    */
  def getSuggestedName(frameTemplate: FrameTemplate): String = {
    val rotationString =
      if (frameTemplate.blockPool.canRotateBlocks)
        "R"
      else
        "NR"


    s"${frameTemplate.frameMode.name(0)} ${frameTemplate.initialDimension}, ${frameTemplate.blockPool.blockDimensions.size} D, ${frameTemplate.blockPool.totalBlockCount} B, ${rotationString}"
  }
}

/**
  * Two-dimensional packing problem.
  *
  * Problems are ordered by name.
  *
  * @param frameTemplate   The template underlying the problem's frame
  * @param timeLimitOption The optional time limit. Cannot exceed 1 day. If None, no time limit will be set
  * @param name            The problem name. Must not be empty
  * @param id              The unique ID
  */
case class Problem(
                    frameTemplate: FrameTemplate,
                    timeLimitOption: Option[Duration],
                    name: String,
                    id: UUID = UUID.randomUUID()
                  ) extends Ordered[Problem] {
  timeLimitOption.foreach(timeLimit => {
    require(
      timeLimit > Duration.ZERO,
      "The time limit must be a positive duration"
    )

    require(
      timeLimit <= Problem.MaxTimeLimit,
      "The time limit cannot exceed its maximum value"
    )
  })


  require(
    name.nonEmpty,
    "The problem name cannot be empty"
  )


  override def compare(that: Problem): Int =
    name.compareTo(that.name)


  override def toString: String =
    name
}
