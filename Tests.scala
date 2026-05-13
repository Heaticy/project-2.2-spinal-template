package Qin110

import Qin110.Tools.dataWr
import Qin110.Tools.dataRd
import Qin110.Tools.loadProgram
import Qin110.Tools.loadArray
import Qin110.Tools.createLog
import Qin110.Tools.strProgram
import Qin110.Tools.logTitle
import Qin110.Tools.logProgram
import Qin110.Tools.strCheck
import Qin110.Tools.strArray
import Qin110.Tools.eqArray
import Qin110.{MainConfig => cfg}

class Test(name: String, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) {
  val file = createLog(name)
  val program = loadProgram(name)

  def prSim(): Unit = {}

  def poSim(cycles: Int): Unit = {
    logTitle(file, name, cycles)
    logProgram(file, program)
    file.close()
  }
}

class TestInst(name: String, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt, base: Long, step: Int, golden: Array[(String, Int)]) extends Test(name, memWr, memRd) {
  override def poSim(cycles: Int): Unit = {
    val data = dataRd(memRd, golden.length, step, base).map { _.toInt }.toArray
    val result = eqArray(data, golden.map { case (k, v) => v })
    logTitle(file, name, cycles, result)
    logProgram(file, program)
    file.println("GT: " + strArray(golden.map { case (k, v) => v }) + "\n")
    file.println("HW: " + strArray(data) + "\n")
    data.zip(golden).foreach { case (hwval, (key, gtval)) =>
      file.println("# Instruction " + key)
      file.println(strCheck(gtval == hwval))
      file.println("GT: " + gtval)
      file.println("HW: " + hwval)
      file.println()
    }
    file.close()
  }
}

object GoldenRV32IFull {
  val content = Array(
    // scalafmt: { align.preset = more }
    "auipc" -> 32,
    "jalr"  -> 48,
    "jal"   -> 64,
    "sw"    -> -858984705,
    "sh"    -> 61183,
    "sb"    -> 65280,
    "lw"    -> -858984705,
    "lh"    -> -4353,
    "lb"    -> -1,
    "lhu"   -> 61183,
    "lbu"   -> 255,
    "add"   -> 56,
    "sub"   -> 46,
    "sll"   -> 14,
    "srl"   -> 3,
    "sra"   -> -4,
    "xor"   -> 54,
    "or"    -> 55,
    "and"   -> 1,
    "slt"   -> 1,
    "sltu"  -> 1,
    "addi"  -> 41,
    "slli"  -> 28,
    "srli"  -> 1,
    "srai"  -> -2,
    "xori"  -> 6,
    "ori"   -> 7,
    "andi"  -> 1,
    "slti"  -> 0,
    "sltiu" -> 0,
    "beq"   -> 0,
    "bne"   -> 1,
    "blt"   -> 1,
    "bge"   -> 0,
    "bltu"  -> 1,
    "bgeu"  -> 0
    // scalafmt: { align.preset = some }
  )
}

object GoldenRV32IPart {
  val content = Array(
    // scalafmt: { align.preset = more }
    "auipc" -> 32,
    "jalr"  -> 48,
    "jal"   -> 64,
    "sw"    -> -858984705,
    "sb"    -> 65280,
    "lw"    -> -858984705,
    "lb"    -> -1,
    "add"   -> 56,
    "sub"   -> 46,
    "and"   -> 1,
    "slt"   -> 1,
    "addi"  -> 41,
    "andi"  -> 1,
    "slti"  -> 0,
    "beq"   -> 0,
    "blt"   -> 1
    // scalafmt: { align.preset = some }
  )
}
class TestRV32IFull(memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestInst("TestRV32IFull", memWr, memRd, base = cfg.ram.base, step = 1, golden = GoldenRV32IFull.content)

class TestRV32IPart(memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestInst("TestRV32IPart", memWr, memRd, base = cfg.ram.base, step = 1, golden = GoldenRV32IPart.content)
