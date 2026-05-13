package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class Ram(ramInit: Array[BigInt] = Array.fill(cfg.ram.words.toInt)(0)) extends Component {
  val io = new Bundle {
    // Port dmem
    val dmem = slave(PortDmem())
  }

  // Ram
  val m = Mem(Bits(dataWidth bits), cfg.ram.words).init(ramInit.map(B(_)).padTo(cfg.ram.words.toInt, B(0)).toSeq).simPublic()

  // Port dmem operation
  io.dmem.operate(m, cfg.ram)
}

object RamSim extends App {
  Config.sim.compile(Ram()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object RamVerilog extends App {
  Config.spinal.generateVerilog(Ram())
}

object RamVhdl extends App {
  Config.spinal.generateVhdl(Ram())
}
