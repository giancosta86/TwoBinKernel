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

import java.time.Duration
import javafx.fxml.FXML
import javafx.stage.Stage

import info.gianlucacosta.helios.fx.dialogs.Alerts
import info.gianlucacosta.twobinpack.core.{FrameTemplate, Problem}
import info.gianlucacosta.twobinpack.io.ProblemNameDialogs
import info.gianlucacosta.twobinpack.io.repositories.ProblemRepository

import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType


/**
  * Generic FXML-based controller for problem generators.
  *
  * The related FXML file must include:
  *
  * <ul>
  * <li>
  * an ActionEvent named <i>saveProblem</i>, which is automatically handled by the controller itself
  * </li>
  *
  * <li>
  * a <i>maxTimeField</i> TextField control
  * </li>
  * </ul>
  *
  * After the problem is saved, the related stage gets hidden - therefore, it should probably be a StackedStage.
  */
abstract class GeneratorFxmlController {
  var problemRepository: ProblemRepository = _
  var stage: Stage = _
  var scene: Scene = _

  protected lazy val problemNameDialogs: ProblemNameDialogs =
    new ProblemNameDialogs(problemRepository)


  @FXML
  def saveProblem(): Unit = {
    try {
      val frameTemplate =
        createFrameTemplate()

      val timeLimitInMinutesValue =
        maxTimeField.getText.toInt

      val defaultProblemName =
        Problem.getSuggestedName(
          frameTemplate
        )


      val problemNameOption =
        problemNameDialogs.askForNewProblemName(
          "Generate problem...",
          defaultProblemName
        )


      val timeLimitOption: Option[Duration] =
        if (timeLimitInMinutesValue > 0)
          Some(
            Duration.ofMinutes(
              timeLimitInMinutesValue
            )
          )
        else
          None

      problemNameOption.foreach(problemName => {
        val problem =
          Problem(
            frameTemplate,
            timeLimitOption,
            problemName
          )

        problemRepository.add(problem)

        Alerts.showInfo(s"Problem '${problemName}' saved successfully")

        stage.hide()
      })
    } catch {
      case ex: Exception =>
        Alerts.showException(ex, alertType = AlertType.Warning)
    }
  }


  /**
    * Creates the frame template for the problem being generated
    *
    * @return A frame template, whose parameters are probably gathered from UI controls
    */
  protected def createFrameTemplate(): FrameTemplate

  @FXML
  var maxTimeField: javafx.scene.control.TextField = _
}
