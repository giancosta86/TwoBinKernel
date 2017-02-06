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

package info.gianlucacosta.twobinmanager.sdk.importers

import java.io.File

import info.gianlucacosta.twobinmanager.sdk.io.OutputWriter
import info.gianlucacosta.twobinpack.core.Solution
import info.gianlucacosta.twobinpack.io.repositories.{ProblemRepository, SolutionRepository}

/**
  * Generic importer simplifying the import of one or more solutions
  */
abstract class SolutionImporter extends Importer {
  /**
    * Extracts solutions from the given file.
    *
    * Catching exceptions is not required, as it's already done by the framework.
    *
    * @param file              The source file
    * @param outputWriter      The writer used for logging
    * @param problemRepository The ProblemRepository required to retrieve problems and connect them to the solution
    * @return
    */
  protected def readSolutions(
                               file: File,
                               outputWriter: OutputWriter,
                               problemRepository: ProblemRepository
                             ): Set[Solution]


  override def importFile(
                           file: File,
                           outputWriter: OutputWriter,
                           problemRepository: ProblemRepository,
                           solutionRepository: SolutionRepository
                         ): Unit = {
    try {
      val solutions: Set[Solution] =
        readSolutions(file, outputWriter, problemRepository)

      solutions.foreach(solution => {
        try {
          outputWriter.printSubHeader(
            s"Solution '${solution}' for problem '${solution.problem.name}'(${solution.problem.id})"
          )


          problemRepository.findById(solution.problem.id) match {
            case Some(_) =>
              val existingSolutionOption =
                solutionRepository.findById(solution.id)

              existingSolutionOption match {
                case Some(existingSolution) =>
                  if (existingSolution == solution) {
                    outputWriter.printOutcome("The solution is already in the DB")
                  } else {
                    outputWriter.printOutcome(s"A different solution having id '${solution.id}' is in the DB")
                  }

                case None =>
                  solutionRepository.add(solution)
                  outputWriter.printOutcome("Solution successfully imported")
              }

            case None =>
              outputWriter.printOutcome(s"The problem '${solution.problem.id}' is not in the DB")
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
