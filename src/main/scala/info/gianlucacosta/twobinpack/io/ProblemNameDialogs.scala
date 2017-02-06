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

package info.gianlucacosta.twobinpack.io

import info.gianlucacosta.helios.fx.dialogs.{Alerts, InputDialogs}
import info.gianlucacosta.twobinpack.core.Problem
import info.gianlucacosta.twobinpack.io.repositories.ProblemRepository

import scala.annotation.tailrec

/**
  * Utility class providing dialogs asking for problem names
  *
  * @param problemRepository
  */
class ProblemNameDialogs(problemRepository: ProblemRepository) {
  /**
    * Asks for a new problem name, ensuring that the name is not empty and
    * that a problem having such name is not already in the db.
    *
    * @param header      The dialog's header
    * @param defaultName The default name, when the dialog is shown
    * @return A valid new name, or None if the user canceled the dialog
    */
  @tailrec
  final def askForNewProblemName(header: String, defaultName: String): Option[String] = {
    val nameInputOption =
      InputDialogs.askForString(
        "Problem name:",
        defaultName,
        header = header
      )

    nameInputOption match {
      case Some(nameInput) =>
        val newName =
          nameInput.trim

        if (newName.isEmpty) {
          Alerts.showWarning("The problem name cannot be empty")
          askForNewProblemName(header, defaultName)
        } else {
          val conflictingProblemOption =
            problemRepository.findByName(newName)

          if (conflictingProblemOption.nonEmpty) {
            Alerts.showWarning(s"A problem named '${newName}' already exists")
            askForNewProblemName(header, defaultName)
          } else {
            Some(newName)
          }
        }

      case None =>
        None
    }
  }

  /**
    * Asks for a new name for a problem, ensuring that the name is not empty and
    * that a different problem having such name is not already in the db - but
    * the user can successfully input the name of the problem itself.
    *
    * @param problem The problem whose name should be changed
    * @param header  The dialog's header
    * @return A valid new name, or None if the user canceled the dialog
    */
  @tailrec
  final def askForNewProblemName(problem: Problem, header: String): Option[String] = {
    val nameInputOption =
      InputDialogs.askForString(
        "Problem name:",
        problem.name,
        header = header
      )

    nameInputOption match {
      case Some(nameInput) =>
        val newName =
          nameInput.trim

        if (newName.isEmpty) {
          Alerts.showWarning("The problem name cannot be empty")
          askForNewProblemName(problem, header)
        } else {
          val conflictingProblemOption =
            problemRepository.findByName(newName)

          if (conflictingProblemOption.exists(_ != problem)) {
            Alerts.showWarning(s"Another problem named '${newName}' already exists")
            askForNewProblemName(problem, header)
          } else {
            Some(newName)
          }
        }

      case None =>
        None
    }
  }


  /**
    * Shows the existing problem names, asking to choose one
    *
    * @param header The dialog's header
    * @return The selected problem name - or None if the user canceled the dialog
    */
  def askForExistingProblemName(header: String): Option[String] = {
    val problemNames =
      problemRepository.findAllNamesSorted().toList

    if (problemNames.nonEmpty)
      InputDialogs.askForItem(
        "Choose a problem:",
        problemNames,
        header = header
      )
    else {
      showNoProblemsAvailableWarning()
      None
    }
  }


  /**
    * Asks for one or more existing problem names - requesting them one by one.
    *
    * Every time a problem is asked, the problems selected until then are no more shown;
    * furthermore:
    *
    * <ul>
    * <li>
    * If the user cancels the first dialog, None is returned
    * </li>
    *
    * <li>
    * If the user cancels the following dialogs, the previously selected problems are returned
    * </li>
    * </ul>
    *
    * @param header The dialog's header
    * @return The List of selected problems, or None if the user canceled the first dialog
    */
  def askForExistingProblemNames(header: String): Option[List[String]] = {
    val problemNamesPool =
      problemRepository.findAllNamesSorted().toList

    askForExistingProblemNames(
      List(),
      1,
      problemNamesPool,
      header
    )
  }


  @tailrec
  private def askForExistingProblemNames(
                                          cumulatedSelectedNames: List[String],
                                          problemOrdinal: Int,
                                          problemNamesPool: List[String],
                                          header: String
                                        ): Option[List[String]] = {
    if (problemNamesPool.isEmpty) {
      if (problemOrdinal == 1) {
        showNoProblemsAvailableWarning()
        None
      } else {
        Some(cumulatedSelectedNames.reverse)
      }
    } else {
      val selectedNameOption =
        InputDialogs.askForItem(
          s"Choose the ${problemOrdinal}° problem:",
          problemNamesPool,
          header = header
        )

      selectedNameOption match {
        case Some(selectedName) =>
          val newProblemNamesPool: List[String] = {
            val (left, right) =
              problemNamesPool.partition(_ < selectedName)

            left ++ right.tail
          }

          askForExistingProblemNames(
            selectedName :: cumulatedSelectedNames,
            problemOrdinal + 1,
            newProblemNamesPool,
            header
          )

        case None =>
          if (problemOrdinal == 1)
            None
          else
            Some(cumulatedSelectedNames.reverse)
      }
    }
  }


  def showNoProblemsAvailableWarning(): Unit = {
    Alerts.showWarning("There are no problems available.")
  }
}
