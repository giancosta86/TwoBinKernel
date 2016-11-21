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

package info.gianlucacosta.twobinmanager.sdk.importers

import java.io.File

import info.gianlucacosta.twobinmanager.sdk.io.OutputWriter
import info.gianlucacosta.twobinpack.core.Problem
import info.gianlucacosta.twobinpack.io.repositories.{ProblemRepository, SolutionRepository}

/**
  * Generic importer simplifying the import of one or more problems
  */
abstract class ProblemImporter extends Importer {
  /**
    * Extracts problems from the given file.
    *
    * Catching exceptions is not required, as it's already done by the framework.
    *
    * @param file         The source file
    * @param outputWriter The writer used for logging
    * @return A set of problems
    */
  protected def readProblems(file: File, outputWriter: OutputWriter): Set[Problem]

  override def importFile(
                           file: File,
                           outputWriter: OutputWriter,
                           problemRepository: ProblemRepository,
                           solutionRepository: SolutionRepository
                         ): Unit = {
    try {
      val problems: Set[Problem] =
        readProblems(file, outputWriter)

      problems.foreach(problem => {
        outputWriter.printSubHeader(s"Problem: ${problem.name}")

        try {
          val existingProblemOption =
            problemRepository.findByName(problem.name)

          existingProblemOption match {
            case Some(existingProblem) =>
              if (existingProblem == problem) {
                outputWriter.printOutcome("The problem is already in the DB")
              } else {
                outputWriter.printOutcome(s"A different problem named '${problem.name}' is in the DB")
              }

            case None =>
              problemRepository.add(problem)
              outputWriter.printOutcome("Problem successfully imported")
          }
        } catch {
          case ex: Exception =>
            outputWriter.printException(ex)
        }

        outputWriter.println()
      })
    } catch {
      case ex: Exception =>
        outputWriter.printException(ex)
        outputWriter.println()
    }
  }
}
