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

package info.gianlucacosta.twobinpack

import javafx.beans.property.SimpleStringProperty

import info.gianlucacosta.helios.fx.util.FxEngine
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scalafx.Includes._
import scalafx.scene.control.Label

class TestScalaFX extends FlatSpec with Matchers with BeforeAndAfterEach {
  override protected def beforeEach(): Unit = {
    FxEngine.initialize()
  }


  "A ScalaFX binding" should "replace previous bindings" in {
    val firstSourceLabel =
      new Label("Alpha")

    val secondSourceLabel =
      new Label("Beta")

    val targetLabel =
      new Label


    targetLabel.text <==
      firstSourceLabel.text

    targetLabel.text() should be("Alpha")

    firstSourceLabel.text =
      "Gamma"

    targetLabel.text() should be("Gamma")


    targetLabel.text <==
      secondSourceLabel.text

    targetLabel.text() should be("Beta")


    secondSourceLabel.text =
      "Delta"

    targetLabel.text() should be("Delta")


    firstSourceLabel.text =
      "Epsilon"

    //The target label should now be affected only by the latest binding source
    targetLabel.text() should be("Delta")
  }



  "Parameterless onChange on a property" should "be triggered only when the property value changes" in {
    val testProperty =
      new SimpleStringProperty("Alpha")

    var counter =
      0

    testProperty.onChange {
      counter +=
        1
    }


    testProperty() =
      "Beta"

    counter should be(1)


    testProperty() =
      "Gamma"

    counter should be(2)


    testProperty() =
      "Gamma"

    counter should be(2)
  }


  "Full onChange on a property" should "be triggered only when the property value changes" in {
    val testProperty =
      new SimpleStringProperty("Alpha")

    var counter =
      0


    testProperty.onChange { (_, _, _) =>
      counter +=
        1
    }


    testProperty() =
      "Beta"

    counter should be(1)


    testProperty() =
      "Gamma"

    counter should be(2)


    testProperty() =
      "Gamma"

    counter should be(2)
  }
}
