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

package info.gianlucacosta.helios.time

import java.time.Duration

case class DurationExtensions private(duration: Duration) extends Ordered[DurationExtensions] {
  def +(that: DurationExtensions): DurationExtensions =
    DurationExtensions(duration.plus(that.duration))


  def -(that: DurationExtensions): DurationExtensions =
    DurationExtensions(duration.minus(that.duration))


  override def compare(that: DurationExtensions): Int =
    duration.compareTo(that.duration)


  lazy val digitalFormat: String = {
    val hours =
      duration.toHours

    val minutes =
      duration.toMinutes % 60

    val seconds =
      duration.getSeconds % 60


    if (hours > 0)
      f"${hours}%02d:${minutes}%02d:${seconds}%02d"
    else
      f"${minutes}%02d:${seconds}%02d"
  }
}
