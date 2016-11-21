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

import scala.annotation.tailrec
import scala.util.Random

/**
  * A segment whose length is a multiple of a basic "quantum" unit
  *
  * @param length The quantized length of the segment
  */
case class QuantizedSegment(length: Int) {
  require(
    length >= 0,
    "The segment length must be >= 0"
  )

  /**
    * Splits this segment into a random list of subsegments, each having random length <= maxSubsegmentLength
    *
    * @param maxSubsegmentLength The maximum length of every subsegment
    * @return The list of random subsegments
    */
  def createRandomPartition(maxSubsegmentLength: Int): List[QuantizedSegment] = {
    require(
      maxSubsegmentLength > 0,
      "Subsegments must have maximum length > 0"
    )

    createRandomPartition(maxSubsegmentLength, 0, Nil)
  }

  @tailrec
  private def createRandomPartition(
                                     maxSubsegmentLength: Int,
                                     cumulatedSubsegmentsLength: Int,
                                     cumulatedSubsegments: List[QuantizedSegment]): List[QuantizedSegment] = {
    if (cumulatedSubsegmentsLength == length)
      cumulatedSubsegments //As segments are random, we do not need to reverse here
    else {
      val availableLength =
        length - cumulatedSubsegmentsLength

      val currentMaxLength: Int =
        math.min(
          maxSubsegmentLength,
          availableLength
        )

      val currentLength: Int =
        Random.nextInt(currentMaxLength) + 1

      val currentSegment =
        QuantizedSegment(currentLength)


      createRandomPartition(
        maxSubsegmentLength,
        cumulatedSubsegmentsLength + currentLength,
        currentSegment :: cumulatedSubsegments
      )
    }
  }
}
