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

class TestQuantizedSegment extends FlatSpec with Matchers {
  private val maxTestIterations =
    500


  private val maxSubsegmentSize =
    9


  "Partitioning" should "keep the original segment length" in {
    runPartitionTests((initialSegment, partitionSegments) => {
      val partitionLength =
        partitionSegments
          .map(_.length)
          .sum

      partitionLength should be(initialSegment.length)
    })
  }


  "Partitioning" should "create non-empty subsegments" in {
    runPartitionTests((initialSegment, partitionSegments) => {
      partitionSegments.foreach(segment => {
        segment.length should be >= 1
      })
    })
  }


  "Partitioning" should "create subsegments not longer than their allowed size" in {
    runPartitionTests((initialSegment, partitionSegments) => {
      partitionSegments.foreach(segment => {
        segment.length should be <= maxSubsegmentSize
      })
    })
  }


  private def runPartitionTests(callback: (QuantizedSegment, List[QuantizedSegment]) => Unit): Unit = {
    val initialSegment =
      QuantizedSegment(59)


    Range.inclusive(1, maxTestIterations).foreach { _ =>
      val partitionSegments =
        initialSegment.createRandomPartition(maxSubsegmentSize)


      callback(
        initialSegment,
        partitionSegments
      )
    }
  }
}
