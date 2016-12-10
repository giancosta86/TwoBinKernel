package info.gianlucacosta.helios.time

import java.time.Duration

case class DurationExtensions private(duration: Duration) extends Ordered[DurationExtensions] {
  def +(that: DurationExtensions): DurationExtensions =
    DurationExtensions(duration.plus(that.duration))

  def -(that: DurationExtensions): DurationExtensions =
    DurationExtensions(duration.minus(that.duration))

  override def compare(that: DurationExtensions): Int =
    duration.compareTo(that.duration)
}
