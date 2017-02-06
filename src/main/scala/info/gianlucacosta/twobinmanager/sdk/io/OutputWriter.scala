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

package info.gianlucacosta.twobinmanager.sdk.io

import java.io.{PrintWriter, Writer}

/**
  * PrintWriter providing useful formatting methods
  *
  * @param targetWriter The decorated writer
  */
class OutputWriter(targetWriter: Writer) extends PrintWriter(targetWriter) {
  /**
    * Prints a header (semantically similar to H1 in HTML)
    *
    * @param header The header text
    */
  def printHeader(header: String): Unit = {
    println("=" * header.length)
    println(header)
    println("=" * header.length)
    println()
  }


  /**
    * Prints a subheader (semantically similar to H2 in HTML)
    *
    * @param subHeader
    */
  def printSubHeader(subHeader: String): Unit = {
    println("-" * subHeader.length)
    println(subHeader)
    println("-" * subHeader.length)
    println()
  }

  /**
    * Prints an outcome string using a dedicated format
    *
    * @param outcome The outcome string
    */
  def printOutcome(outcome: String): Unit = {
    println(s"*** ${outcome} ***")
  }


  /**
    * Prints an exception, also showing its stacktrace
    *
    * @param exception The exception
    */
  def printException(exception: Exception): Unit = {
    printOutcome(s"EXCEPTION - ${exception.getClass.getSimpleName}")

    exception.printStackTrace(this)
  }
}
