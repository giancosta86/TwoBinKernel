package info.gianlucacosta.helios

import java.time.Duration

import info.gianlucacosta.helios.time.DurationExtensions

import scala.language.implicitConversions

object Includes {
  implicit def toDurationExtensions(duration: Duration): DurationExtensions =
    DurationExtensions(duration)

  implicit def toDuration(durationExtensions: DurationExtensions): Duration =
    durationExtensions.duration
}
