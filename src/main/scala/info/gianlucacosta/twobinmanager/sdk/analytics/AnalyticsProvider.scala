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

package info.gianlucacosta.twobinmanager.sdk.analytics

/**
  * Analytics plugin, providing a map of <em>analytics dimensions</em>:
  * an analytics dimension includes a descriptive string and the related
  * function creating a chart
  */
trait AnalyticsProvider {
  /**
    * This method must return a <b>Map[String, ChartRetriever]</b>, where:
    *
    * <ul>
    * <li>
    * <b>String</b> represents a description of the analytics dimension - a label that will
    * be shown and that the user can choose
    * </li>
    *
    * <li>
    * <b>ChartRetriever</b> is a function that is lazily invoked whenever
    * the corresponding label is chosen for the first time; the value is then
    * cached
    * </li>
    * </ul>
    *
    * <b>Please note:</b> you should <strong>not</strong> assume being on the FX thread:
    * it is definitely suggested to employ utilities such as <i>Platform.runLater</i>.
    *
    * @return A map (even empty, but never null) of Label string -> ChartRetriever
    */
  def analyticsDimensions: Map[String, ChartRetriever]
}
