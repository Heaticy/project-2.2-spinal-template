package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.lib.io.TriStateArray
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

object MainConfig {
  // scalafmt: { align.preset = more }
  val rom  = new MemoryConfig(0x00000000L, 0x00008000L, allowRd = true, allowWr = false)
  val ram  = new MemoryConfig(0x20000000L, 0x00008000L, allowRd = true, allowWr = true)
  val sreg = new SimRegConfig(0x60000000L)

  val cycleMax = 0x10000
  // scalafmt: { align.preset = some }
}

class MemoryConfig(val base: Long, val size: Long, val allowRd: Boolean, val allowWr: Boolean) {
  def words: Long = { size / byteCount }
  def in(addr: Long): Boolean = { addr >= base && addr < base + size }

  def mux(addr: Bits): Bool = { addr.asUInt >= base && addr.asUInt < base + size }
  def maddr(addr: Bits): UInt = { ((addr.asUInt - base) >> byteShift).resize(log2Up(words) bits) }
  def mtail(addr: Bits): UInt = { ((addr.asUInt - base)(0 until byteShift)) }

  def enableRd(addr: Bits): Bool = Bool(allowRd)
  def enableWr(addr: Bits): Bool = Bool(allowWr)
}
