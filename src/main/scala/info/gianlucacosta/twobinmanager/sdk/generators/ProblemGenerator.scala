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

package info.gianlucacosta.twobinmanager.sdk.generators

import javafx.stage.Stage

import info.gianlucacosta.twobinpack.io.repositories.ProblemRepository

/**
  * Problem generator
  */
trait ProblemGenerator {
  /**
    * Creates one or more problems, saving them to a ProblemRepository.
    *
    * <b>Please, note</b>: this method runs <strong>on the GUI thread</strong> -
    * therefore, computationally-intensive operations should be carried out in a suitable
    * background thread
    *
    * @param previousStage     The previous stage. Very useful if the generator's GUI is
    *                          implemented via a StackedStage
    * @param problemRepository The underlying problem repository.
    *                          Most probably, one should only use its <i>add()</i> method here
    */
  def generate(previousStage: Stage, problemRepository: ProblemRepository): Unit

  /**
    * The generator's name
    *
    * @return
    */
  def name: String

  override def toString: String =
    name
}
