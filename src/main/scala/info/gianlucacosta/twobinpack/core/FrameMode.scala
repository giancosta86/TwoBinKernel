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

object FrameMode {
  val Knapsack =
    FrameMode("Knapsack")

  val Strip =
    FrameMode("Strip")


  val All =
    List(
      Knapsack,
      Strip
    )


  def getByName(name: String): FrameMode =
    All.find(_.name == name).get
}


/**
  * A frame mode - that is, how the frame should react to user input
  *
  * @param name
  */
case class FrameMode private(name: String) {
  override def toString: String =
    name
}
