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

package info.gianlucacosta.twobinpack.io

import java.io._

import org.scalatest.{FlatSpec, Matchers}

abstract class WriterReaderTestBase[TObject, TWriter <: Writer, TReader <: Reader] extends FlatSpec with Matchers {
  "Exporting to a Writer and importing from a Reader" should "return the original object" in {
    val originalObject =
      createOriginalObject()

    val restoredObject =
      saveAndRestore(originalObject)

    restoredObject should be(originalObject)
  }


  "Exporting and importing twice" should "return the original object" in {
    val originalObject =
      createOriginalObject()

    val restoredObject =
      saveAndRestore(originalObject)

    val secondLevelRestoredObject =
      saveAndRestore(restoredObject)


    secondLevelRestoredObject should be(originalObject)
  }


  private def saveAndRestore(originalObject: TObject): TObject = {
    val stringWriter =
      new StringWriter()

    val writer =
      createWriter(stringWriter)

    exportObject(writer, originalObject)


    val sourceString =
      stringWriter.toString

    println("-----------------")
    println(sourceString)
    println("-----------------")


    val stringReader =
      new StringReader(sourceString)

    val reader =
      createReader(
        new BufferedReader(
          stringReader
        )
      )


    importObject(reader)
  }


  protected def createOriginalObject(): TObject

  protected def createWriter(targetWriter: Writer): TWriter

  protected def exportObject(writer: TWriter, originalObject: TObject)

  protected def createReader(sourceReader: BufferedReader): TReader

  protected def importObject(reader: TReader): TObject
}
