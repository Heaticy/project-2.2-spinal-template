package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class BranchUnit() extends Component {
  val io = new Bundle {
    // Alu less
    val less = in(Bool())
    // Alu zero
    val zero = in(Bool())
    // Branch type
    val branchOp = in(BranchOp())
    // Pc next select
    val pcSrc = out(PcSrc())
  }

  // TODO
}

object BranchUnitSim extends App {
  Config.sim.compile(BranchUnit()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object BranchUnitVerilog extends App {
  Config.spinal.generateVerilog(BranchUnit())
}

object BranchUnitVhdl extends App {
  Config.spinal.generateVhdl(BranchUnit())
}
