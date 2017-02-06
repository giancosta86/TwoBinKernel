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

package info.gianlucacosta.twobinpack.core

/**
  * Dimension whose width and height are multiples of a basic "quantum" unit
  */
trait QuantizedDimension2D[T <: QuantizedDimension2D[T]] {
  this: T =>
  def width: Int

  def height: Int

  def createNew(width: Int, height: Int): T

  @transient
  lazy val isSquare: Boolean =
    width == height

  @transient
  lazy val area: Int =
    width * height


  @transient
  lazy val isHorizontal: Boolean =
    height <= width


  def rotate(): T =
    createNew(
      height,
      width
    )

  override def toString: String =
    s"${width} x ${height}"
}
