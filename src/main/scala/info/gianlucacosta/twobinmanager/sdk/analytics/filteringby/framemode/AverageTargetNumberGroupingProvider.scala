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

package info.gianlucacosta.twobinmanager.sdk.analytics.filteringby.framemode

import info.gianlucacosta.twobinpack.core.Solution

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}

/**
  * Provider filtering by frame mode and computing the average target after grouping by a Number property.
  *
  * It also automatically creates a LineChart.
  *
  * @param groupingPropertyName The name of the property; shown in the dimension label and available when creating the chart
  * @param groupingFunction     The function that actually groups solutions in every subset
  */
abstract class AverageTargetNumberGroupingProvider(
                                                    groupingPropertyName: String,
                                                    groupingFunction: (Solution) => Number
                                                  ) extends AverageTargetProvider[Number](groupingPropertyName, groupingFunction) {
  override protected def createChart(averageMap: Map[Number, Double], averageSeries: Series[Number, Number]): XYChart[Number, Number] = {
    val xAxis =
      new NumberAxis {
        label =
          groupingPropertyName
      }

    val yAxis =
      new NumberAxis {
        label =
          "Target"
      }

    LineChart(
      xAxis,
      yAxis,
      ObservableBuffer(averageSeries)
    )
  }
}
