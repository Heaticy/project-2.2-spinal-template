package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class Hazard() extends Component {
  val io = new Bundle {
    // Pc source (Stage E)
    val pcSrcE = in(PcSrc())
    // Flush register F/D
    val flushFD = out(Bool())
    // Flush register D/E
    val flushDE = out(Bool())
  }

  // TODO
}

object HazardSim extends App {
  Config.sim.compile(Hazard()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object HazardVerilog extends App {
  Config.spinal.generateVerilog(Hazard())
}

object HazardVhdl extends App {
  Config.spinal.generateVhdl(Hazard())
}
