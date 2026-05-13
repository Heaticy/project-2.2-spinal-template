package Qin110

import spinal.core._
import spinal.core.sim._

object Constant {
  val dataWidth = 32
  val addrWidth = 32
  val rfWidth = 5
  val byteWidth = 8
  val byteCount = dataWidth / byteWidth
  val byteShift = log2Up(byteCount)

  val opcodeWidth = 7
  val funct3Width = 3
  val funct6Width = 6
  val funct7Width = 7

  def rangeWord(i: Int = 0) = { i * 4 * byteWidth until (i + 1) * 4 * byteWidth }
  def rangeHalf(i: Int = 0) = { i * 2 * byteWidth until (i + 1) * 2 * byteWidth }
  def rangeByte(i: Int = 0) = { i * 1 * byteWidth until (i + 1) * 1 * byteWidth }
}
