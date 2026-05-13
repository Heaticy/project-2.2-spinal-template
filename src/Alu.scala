package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class Alu() extends Component {
  val io = new Bundle {
    // Alu input 1
    val srca = in(Bits(dataWidth bits))
    // Alu input 2
    val srcb = in(Bits(dataWidth bits))
    // Alu operation
    val aluOp = in(AluOp())
    // Alu output
    val result = out(Bits(dataWidth bits))
    // Alu less (-> BranchUnit)
    val less = out(Bool())
    // Alu zero (-> BranchUnit)
    val zero = out(Bool())
  }

  // TODO
}

object AluSim extends App {
  Config.sim.compile(Alu()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object AluVerilog extends App {
  Config.spinal.generateVerilog(Alu())
}

object AluVhdl extends App {
  Config.spinal.generateVhdl(Alu())
}
