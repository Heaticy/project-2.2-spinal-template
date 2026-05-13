package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

class SimRegConfig(base: Long, val regs: Int = 4) extends MemoryConfig(base, regs * byteCount, true, true) {
  def halt = base + 0x0
  def stdo = base + 0x4
  def stdi = base + 0x8
}

case class SimulationReg() extends Component {
  val io = new Bundle {
    // Port dmem
    val dmem = slave(PortDmem())
  }

  // Simulation regs
  val m = Vec(Reg(Bits(dataWidth bits)).init(0), cfg.sreg.words.toInt).simPublic()
  val halt = cfg.sreg.maddr(cfg.sreg.halt)
  val stdo = cfg.sreg.maddr(cfg.sreg.stdo)
  val stdi = cfg.sreg.maddr(cfg.sreg.stdi)

  // Port dmem operation
  io.dmem.operate(m, cfg.sreg)
}

object SimulationRegSim extends App {
  Config.sim.compile(SimulationReg()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object SimulationRegVerilog extends App {
  Config.spinal.generateVerilog(SimulationReg())
}

object SimulationRegVhdl extends App {
  Config.spinal.generateVhdl(SimulationReg())
}
