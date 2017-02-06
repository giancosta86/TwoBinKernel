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
import info.gianlucacosta.twobinpack.io.repositories.{ProblemRepository, SolutionRepository}

/**
  * Generic file-import process - in particular, it can be used to import
  * external problem/solution formats.
  *
  * <b>Please, note</b>: importers do <strong>NOT</strong> run on the GUI thread,
  * therefore requesting user input via GUI elements (e.g., dialogs) requires tools such as
  * Platform.runLater
  */
trait Importer {
  /**
    * Tests whether the importer supports a file
    *
    * @param file The file that should be imported
    * @return true if the importer can import the file - which will cause importFile() to be
    *         invoked by the framework
    */
  def canImport(file: File): Boolean

  /**
    * Actually imports a file
    *
    * @param file               The file to import
    * @param outputWriter       A dedicated log writer
    * @param problemRepository  A problem repository
    * @param solutionRepository A solution repository
    */
  def importFile(
                  file: File,
                  outputWriter: OutputWriter,
                  problemRepository: ProblemRepository,
                  solutionRepository: SolutionRepository
                ): Unit
}
