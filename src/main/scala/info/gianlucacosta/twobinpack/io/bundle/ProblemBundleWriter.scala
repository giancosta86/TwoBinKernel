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

package info.gianlucacosta.twobinpack.io.bundle

import java.io.Writer

import com.thoughtworks.xstream.io.xml.PrettyPrintWriter
import info.gianlucacosta.helios.io.DecoratorWriter
import info.gianlucacosta.twobinpack.core.ProblemBundle
import info.gianlucacosta.twobinpack.io.ModelXStream


/**
  * Writer providing a dedicated method for writing problem bundles as pretty-printed XML
  *
  * @param targetWriter
  */
class ProblemBundleWriter(targetWriter: Writer) extends DecoratorWriter(targetWriter) {
  def writeProblemBundle(problemBundle: ProblemBundle): Unit = {
    val prettyPrintWriter =
      new PrettyPrintWriter(this)

    ModelXStream.marshal(problemBundle, prettyPrintWriter)
  }
}
