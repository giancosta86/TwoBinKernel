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

import java.util.UUID

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.StaxDriver
import com.thoughtworks.xstream.security.{NoTypePermission, NullPermission, PrimitiveTypePermission}
import info.gianlucacosta.twobinpack.core._

/**
  * Dedicated XStream object providing cleaner XML files and strict security checks
  */
private object ModelXStream extends XStream(new StaxDriver) {
  initSecurity()
  initAliases()


  private def initSecurity(): Unit = {
    addPermission(NoTypePermission.NONE)

    addPermission(NullPermission.NULL)
    addPermission(PrimitiveTypePermission.PRIMITIVES)

    allowTypesByWildcard(Array(
      "info.gianlucacosta.twobinpack.**",
      "scala.collection.**",
      "java.time.**"
    ))


    allowTypes(Array[Class[_]](
      Class.forName("scala.Some"),
      None.getClass,

      Class.forName("javafx.scene.paint.Color"),
      Class.forName("scalafx.scene.paint.Color"),

      classOf[UUID]
    ))

    registerConverter(new DurationConverter)
  }


  private def initAliases(): Unit = {
    alias("bundle", classOf[ProblemBundle])

    alias("problem", classOf[Problem])

    alias("frameDimension", classOf[FrameDimension])
    alias("blockDimension", classOf[BlockDimension])

    alias("point", classOf[QuantizedPoint2D])


    alias("some", Class.forName("scala.Some"))
    alias("none", None.getClass)

    alias("color", Class.forName("javafx.scene.paint.Color"))
    alias("fxColor", Class.forName("scalafx.scene.paint.Color"))

    alias("uuid", classOf[UUID])
  }
}
