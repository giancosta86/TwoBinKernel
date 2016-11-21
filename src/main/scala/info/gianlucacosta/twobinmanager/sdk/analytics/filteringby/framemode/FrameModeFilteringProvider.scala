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

import info.gianlucacosta.twobinmanager.sdk.analytics._
import info.gianlucacosta.twobinpack.core.{FrameMode, Solution}

import scalafx.scene.chart.Chart

/**
  * Analytics provider filtering the solutions by frame mode - and creating:
  *
  * <ul>
  * <li>
  * A <b>global</b> dimension, related to <em>all</em> the solutions
  * </li>
  *
  * <li>
  * One dimension <em>for every frame mode</em>
  * </li>
  * </ul>
  *
  * <strong>Side effects</strong>: frame-specific dimensions look for (and create) a key-value pair
  * in the temporary cache: (frame mode) --> (solutions having such frame mode)
  *
  * @param dimensionBasename The base name for the dimension - which will be specialized for every frame mode
  */
abstract class FrameModeFilteringProvider(dimensionBasename: String) extends AnalyticsProvider {

  override def analyticsDimensions: Map[String, ChartRetriever] = {
    val frameModeBasedRetrievers: Map[String, ChartRetriever] =
      FrameMode.All.map(frameMode => {
        val frameModeDimensionName =
          s"${dimensionBasename} - ${frameMode.name}"

        val frameModeChartRetriever: ChartRetriever =
          (allSolutions, intermediateCache) => {
            val frameModeSolutions: Iterable[Solution] =
              intermediateCache.getOrElse(
                frameMode.name,
                allSolutions.filter(_.problem.frameTemplate.frameMode == frameMode)
              ).asInstanceOf[Iterable[Solution]]


            val cacheWithFrameModeItems =
              intermediateCache.updated(
                frameMode.name,
                frameModeSolutions
              )

            getResultForFrameMode(
              allSolutions,
              frameMode,
              frameModeSolutions,
              cacheWithFrameModeItems
            )
          }

        frameModeDimensionName -> frameModeChartRetriever
      }).toMap


    frameModeBasedRetrievers.updated(
      dimensionBasename,
      getGlobalResult
    )
  }

  /**
    * Returns the chart and the new cache <em>for all</em> the solutions
    *
    * @param allSolutions      All the solutions
    * @param intermediateCache The intermediate cache
    * @return The optional chart and the updated intermediate cache
    */
  protected def getGlobalResult(
                                 allSolutions: Iterable[Solution],
                                 intermediateCache: IntermediateCache
                               ): (Option[Chart], IntermediateCache)


  /**
    * Returns the chart and the new cache <em>for all the solutions having a given frame mode</em>
    *
    * @param allSolutions       All the solutions
    * @param frameMode          The current frame mode
    * @param frameModeSolutions The solutions whose problem has the current frame mode
    * @param intermediateCache  The intermediate cache
    * @return The optional chart for the current frame mode and the updated intermediate cache
    */
  protected def getResultForFrameMode(
                                       allSolutions: Iterable[Solution],
                                       frameMode: FrameMode,
                                       frameModeSolutions: Iterable[Solution],
                                       intermediateCache: IntermediateCache
                                     ): (Option[Chart], IntermediateCache)
}
