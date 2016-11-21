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

package info.gianlucacosta.twobinpack.core

/**
  * Quantized dimension of a Frame
  */
case class FrameDimension(width: Int, height: Int) extends QuantizedDimension2D[FrameDimension] {
  require(
    width > 0,
    "Frame width must be > 0"
  )

  require(
    height > 0,
    "Frame height must be > 0"
  )


  override def createNew(width: Int, height: Int): FrameDimension =
    copy(
      width,
      height
    )
}
