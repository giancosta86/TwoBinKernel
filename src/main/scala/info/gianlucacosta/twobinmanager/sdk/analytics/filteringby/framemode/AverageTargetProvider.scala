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

package info.gianlucacosta.twobinmanager.sdk.analytics.filteringby.framemode

import info.gianlucacosta.twobinmanager.sdk.analytics._
import info.gianlucacosta.twobinpack.core.{FrameMode, Solution}

import scalafx.scene.chart.{Chart, XYChart}

/**
  * Analytics provider whose dimensions compute <em>the average target</em> for the global solutions set
  * and for the solutions of every frame mode.
  *
  * To determine the average target, the solutions of every set (global or frame-mode-specific)
  * are grouped according to a property, whose name is expressed by <em>groupingPropertyName</em>;
  * the actual grouping is performed via <em>groupingFunction</em>.
  *
  * Solutions having undefined target are NOT included in the average's denominator.
  *
  * @param groupingPropertyName The name of the property; shown in the dimension label and available when creating the chart
  * @param groupingFunction     The function that actually groups solutions in every subset
  * @tparam TGrouping The type of the grouping property
  */
abstract class AverageTargetProvider[TGrouping](
                                                 protected val groupingPropertyName: String,
                                                 groupingFunction: (Solution) => TGrouping
                                               ) extends FrameModeFilteringProvider(
  s"Average target by ${groupingPropertyName.toLowerCase}"
) {
  override protected def getGlobalResult(allSolutions: Iterable[Solution], intermediateCache: IntermediateCache): (Option[Chart], IntermediateCache) =
    (
      setupAverageChart(allSolutions),
      intermediateCache
    )

  override protected def getResultForFrameMode(allSolutions: Iterable[Solution], frameMode: FrameMode, frameModeSolutions: Iterable[Solution], intermediateCache: IntermediateCache): (Option[Chart], IntermediateCache) =
    (
      setupAverageChart(frameModeSolutions),
      intermediateCache
    )


  /**
    * Computes the average target and prepares the chart
    *
    * @param solutions A solution pool
    * @return The chart, if it's possible to determine at least an average value
    */
  private def setupAverageChart(solutions: Iterable[Solution]): Option[Chart] = {
    val averageMap: Map[TGrouping, Double] =
      solutions
        .groupBy(groupingFunction)
        .mapValues(groupSolutions => {
          val (targetSum, solutionsCount) =
            groupSolutions.foldLeft((0.0, 0)) {
              case ((cumulatedTargetSum, cumulatedSolutionsCount), solution) =>
                (
                  cumulatedTargetSum +
                    solution.target.getOrElse(0),

                  cumulatedSolutionsCount + (
                    if (solution.target.nonEmpty)
                      1
                    else
                      0
                    )
                )
            }

          if (solutionsCount > 0)
            Some(targetSum / solutionsCount)
          else
            None
        })
        .filter {
          case (_, averageTargetOption) =>
            averageTargetOption.nonEmpty
        }.map {
        case (groupingValue, averageTargetOption) =>
          groupingValue -> averageTargetOption.get
      }

    if (averageMap.nonEmpty) {
      val averageSeries =
        new XYChart.Series[TGrouping, Number] {
          name =
            "Average"

          data = averageMap
            .toList
            .map {
              case (groupingValue, averageTarget) =>
                XYChart.Data[TGrouping, Number](groupingValue, averageTarget)
            }
        }


      val chart =
        createChart(averageMap, averageSeries)

      Some(
        chart
      )
    } else
      None
  }


  /**
    * Creates the actual chart when a non-empty average map is available
    *
    * @param averageMap    The map of (Grouping-property value) --> (Target average)
    * @param averageSeries The data series related to averageMap
    * @return Any subclass of XYChart
    */
  protected def createChart(
                             averageMap: Map[TGrouping, Double],
                             averageSeries: XYChart.Series[TGrouping, Number]
                           ): XYChart[TGrouping, Number]
}
