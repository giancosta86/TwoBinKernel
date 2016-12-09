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

import java.util.UUID

object Problem {
  val MaxTimeLimitInMinutes =
    60 * 24

  val MinResolution =
    1

  val MaxResolution =
    100

  val SuggestedResolution =
    30


  /**
    * Provides a suggested name for a problem given a few of its parameters
    *
    * @param frameTemplate            The frame template
    * @param timeLimitInMinutesOption The time limit in minutes
    * @return
    */
  def getSuggestedName(frameTemplate: FrameTemplate, timeLimitInMinutesOption: Option[Int]): String = {
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
  * @param frameTemplate            The template underlying the problem's frame
  * @param timeLimitInMinutesOption The time limit, in minutes. Cannot exceed 1 day. If None, no time limit will be set
  * @param name                     The problem name. Must not be empty
  * @param id                       The unique ID
  */
case class Problem(
                    frameTemplate: FrameTemplate,
                    timeLimitInMinutesOption: Option[Int],
                    name: String,
                    id: UUID = UUID.randomUUID()
                  ) extends Ordered[Problem] {
  timeLimitInMinutesOption.foreach(timeLimitInMinutes => {
    require(
      1 <= timeLimitInMinutes && timeLimitInMinutes <= Problem.MaxTimeLimitInMinutes,
      s"The time limit in minute, when present, must be in the range [1..${Problem.MaxTimeLimitInMinutes}]"
    )
  })

  require(
    name.nonEmpty,
    "The problem name cannot be empty"
  )


  @transient
  lazy val timeLimitInSecondsOption: Option[Int] =
    timeLimitInMinutesOption.map(_ * 60)


  override def compare(that: Problem): Int =
    name.compareTo(that.name)


  override def toString: String =
    name
}
