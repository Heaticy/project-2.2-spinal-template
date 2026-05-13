package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class RegisterFile() extends Component {
  val io = new Bundle {
    // Addr of rs1
    val rs1 = in(Bits(rfWidth bits))
    // Addr of rs2
    val rs2 = in(Bits(rfWidth bits))
    // Addr of rd
    val rd = in(Bits(rfWidth bits))
    // Data of rs1
    val rs1Data = out(Bits(dataWidth bits))
    // Data of rs2
    val rs2Data = out(Bits(dataWidth bits))
    // Data of rd
    val rdData = in(Bits(dataWidth bits))
    // Write register file enable
    val regWrite = in(Bool())
  }

  // Register file
  val regs = Mem(Bits(dataWidth bits), 1 << rfWidth).init(Seq.fill(1 << rfWidth)(B(0, dataWidth bits)))

  // TODO
}

object RegisterFileSim extends App {
  Config.sim.compile(RegisterFile()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object RegisterFileVerilog extends App {
  Config.spinal.generateVerilog(RegisterFile())
}

object RegisterFileVhdl extends App {
  Config.spinal.generateVhdl(RegisterFile())
}
