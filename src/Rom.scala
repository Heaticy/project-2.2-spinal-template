package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class Rom(romInit: Array[BigInt] = Array.fill(cfg.rom.words.toInt)(0)) extends Component {
  val io = new Bundle {
    // Port dmem
    val dmem = slave(PortDmem())
    // Port imem
    val imem = slave(PortImem())
  }

  // Rom
  val m = Mem(Bits(dataWidth bits), cfg.rom.words).init(romInit.map(B(_)).padTo(cfg.rom.words.toInt, B(0)).toSeq).simPublic()

  // Port dmem operation
  io.dmem.operate(m, cfg.rom)
  // Port imem operation
  io.imem.operate(m, cfg.rom)
}

object RomSim extends App {
  Config.sim.compile(Rom()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object RomVerilog extends App {
  Config.spinal.generateVerilog(Rom())
}

object RomVhdl extends App {
  Config.spinal.generateVhdl(Rom())
}
