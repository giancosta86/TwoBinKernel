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

import javafx.scene.Parent
import javafx.stage.Stage

import info.gianlucacosta.helios.fx.dialogs.InputDialogs
import info.gianlucacosta.helios.fx.scene.fxml.FxmlScene
import info.gianlucacosta.helios.fx.stage.StackedStage
import info.gianlucacosta.twobinpack.io.repositories.ProblemRepository

import scalafx.Includes._
import scalafx.stage.WindowEvent

/**
  * Generic stage for visually setting up a problem generator
  *
  * @param controllerClass   The class of the controller underlying the scene
  * @param previousStage     The previous window - which is internally used as this is a StackedStage
  * @param problemRepository The problem repository
  * @param stageTitle        The stage title
  * @tparam TController The type of the underlying controller
  * @tparam TRootNode   The type of the root node in the FXML. It usually belong to the javafx.** package tree
  */
abstract class GeneratorFxmlStage[TController <: GeneratorFxmlController, TRootNode <: Parent](
                                                                                                controllerClass: Class[TController],
                                                                                                val previousStage: Stage,
                                                                                                problemRepository: ProblemRepository,
                                                                                                stageTitle: String
                                                                                              ) extends StackedStage {
  title =
    stageTitle

  maximized =
    true


  scene =
    new FxmlScene[TController, TRootNode](controllerClass) {
      override protected def preInitialize(): Unit = {
        controller.problemRepository =
          problemRepository

        controller.stage =
          GeneratorFxmlStage.this

        controller.scene =
          this
      }
    }


  handleEvent(WindowEvent.WindowCloseRequest) {
    (event: WindowEvent) => {
      if (!canClose) {
        event.consume()
      }
    }
  }


  private def canClose: Boolean =
    InputDialogs.askYesNoCancel(
      "Return to the main screen without saving?"
    ).contains(true)
}
