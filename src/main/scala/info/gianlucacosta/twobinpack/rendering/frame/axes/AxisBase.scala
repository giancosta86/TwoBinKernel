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

package info.gianlucacosta.twobinpack.rendering.frame.axes

import javafx.beans.property.SimpleObjectProperty

import info.gianlucacosta.twobinpack.rendering.frame.Frame

import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.VPos
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, TextAlignment}

/**
  * Base class for frame axes
  *
  * @param referenceFrame The reference frame
  */
private abstract class AxisBase(referenceFrame: Frame) extends Canvas {
  graphicsContext2D.textAlign =
    TextAlignment.Center

  graphicsContext2D.textBaseline =
    VPos.Center

  graphicsContext2D.fill =
    Color.Gray


  width.onChange {
    render()
  }

  height.onChange {
    render()
  }


  protected val labelFont =
    new SimpleObjectProperty[Font]

  labelFont <==
    Bindings.createObjectBinding(
      () => {
        Font(
          "Arial",
          referenceFrame.resolution() / 3
        )
      },

      referenceFrame.resolution
    )


  private def render(): Unit = {
    graphicsContext2D.clearRect(
      0,
      0,
      width(),
      height()
    )

    drawLabels(graphicsContext2D)
  }


  protected def drawLabels(gc: GraphicsContext): Unit
}
