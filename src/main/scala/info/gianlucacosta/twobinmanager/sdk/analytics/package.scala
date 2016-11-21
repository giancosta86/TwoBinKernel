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

package info.gianlucacosta.twobinmanager.sdk

import info.gianlucacosta.twobinpack.core.Solution

import scalafx.scene.chart.Chart

package object analytics {
  /**
    * Generic intermediate cache used to share partial results
    * between different analytics dimensions
    */
  type IntermediateCache =
  Map[String, Any]


  /**
    * Pure function receiving a sequence of solutions
    * and an intermediate cache and returning:
    *
    * <ul>
    * <li>
    * A chart, if its data could be computed, or None
    * </li>
    *
    * <li>
    * The updated version of the intermediate cache
    * </li>
    * </ul>
    */
  type ChartRetriever =
  (Iterable[Solution], IntermediateCache) => (Option[Chart], IntermediateCache)
}
