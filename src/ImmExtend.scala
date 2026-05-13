package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class ImmExtend() extends Component {
  val io = new Bundle {
    // instruction (including immediate)
    val instr = in(Bits(dataWidth bits))
    // immediate type
    val immSrc = in(ImmSrc())
    // immediate extended
    val immExt = out(Bits(dataWidth bits))
  }

  // TODO
}

object ImmExtendSim extends App {
  Config.sim.compile(ImmExtend()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object ImmExtendVerilog extends App {
  Config.spinal.generateVerilog(ImmExtend())
}

object ImmExtendVhdl extends App {
  Config.spinal.generateVhdl(ImmExtend())
}
