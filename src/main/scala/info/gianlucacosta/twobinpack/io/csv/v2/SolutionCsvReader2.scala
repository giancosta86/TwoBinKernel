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

package info.gianlucacosta.twobinpack.io.csv.v2

import java.io.BufferedReader
import java.time.Duration
import java.util.UUID

import info.gianlucacosta.helios.io.DecoratorReader
import info.gianlucacosta.twobinpack.core._
import info.gianlucacosta.twobinpack.io.MissingProblemException
import info.gianlucacosta.twobinpack.io.repositories.ProblemRepository

/**
  * Reader providing a dedicated method for reading solutions from CSV - according to version 2 of the document format
  *
  * @param sourceReader     The source reader
  * @param problemRetriever A function (problemId, solutionId) => Problem,
  *                         used to retrieve the Problem object when
  *                         instantiating the Solution
  */
class SolutionCsvReader2(
                          sourceReader: BufferedReader,
                          problemRetriever: (UUID, UUID) => Problem
                        ) extends DecoratorReader(sourceReader) {

  /**
    * Simplified constructor, employing a ProblemRepository to retrieve problems
    *
    * @param sourceReader
    * @param problemRepository
    * @return
    */
  def this(
            sourceReader: BufferedReader,
            problemRepository: ProblemRepository
          ) = this(
    sourceReader,
    (problemId, solutionId) =>
      problemRepository.findById(problemId).getOrElse(
        throw new MissingProblemException(
          s"Solution '${solutionId}' is related to the problem having id '${problemId}', which is not in the repository"
        )
      )
  )

  def readSolution(): Option[Solution] = {
    val uuidLine: String =
      sourceReader.readLine()

    if (uuidLine != null) {
      val solutionId =
        UUID.fromString(
          uuidLine.trim
        )

      val problemId =
        UUID.fromString(
          sourceReader.readLine().trim
        )


      val problem: Problem =
        problemRetriever(problemId, solutionId)


      val solverOptionLine =
        sourceReader.readLine().trim

      val solverOption =
        if (solverOptionLine.nonEmpty)
          Some(solverOptionLine)
        else
          None


      val elapsedTimeOptionLine =
        sourceReader.readLine().trim

      val elapsedTimeOption =
        if (elapsedTimeOptionLine.nonEmpty)
          Some(
            Duration.ofSeconds(
              elapsedTimeOptionLine.toInt
            )
          )
        else
          None


      val blocks =
        parseBlocks()


      Some(
        Solution(
          problem,
          solverOption,
          elapsedTimeOption,
          blocks,
          solutionId
        )
      )
    } else
      None
  }


  /**
    * Reads a line
    *
    * @return The line, or null on EOF
    */
  def readLine(): String =
    sourceReader.readLine()


  private def parseBlocks(): Set[AnchoredBlock] =
    parseLineBlock(
      trimmedLine => {
        val blockParameters =
          trimmedLine.split(',')
            .map(_.toInt)

        AnchoredBlock(
          BlockDimension(
            blockParameters(0),
            blockParameters(1)
          ),

          QuantizedPoint2D(
            blockParameters(2),
            blockParameters(3)
          )
        )
      }
    ).toSet
}
