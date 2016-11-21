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

import info.gianlucacosta.twobinpack.rendering.frame.Frame

import scalafx.Includes._
import scalafx.scene.canvas.GraphicsContext

/**
  * Horizontal axis
  *
  * @param referenceFrame The reference frame
  */
private class HorizontalAxis(referenceFrame: Frame) extends AxisBase(referenceFrame) {
  width <==
    referenceFrame.quantizedWidth * referenceFrame.resolution

  height <==
    referenceFrame.resolution


  protected override def drawLabels(gc: GraphicsContext): Unit = {
    Range(
      0,
      referenceFrame.quantizedWidth()
    ).foreach(leftIndex => {
      val leftOrdinal =
        leftIndex + 1

      val resolution =
        referenceFrame.resolution()

      graphicsContext2D.font =
        labelFont()

      gc.fillText(
        leftOrdinal.toString,
        leftIndex * resolution + resolution / 2,
        height() / 2
      )
    })
  }
}