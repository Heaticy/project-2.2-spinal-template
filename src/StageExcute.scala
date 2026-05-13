package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class StageExcute() extends Component {
  val io = new Bundle {
    // Register D/E
    val regDE = in(RegisterDE())
    // Pc target (for branch/jump)
    val pcTargetE = out(Bits(addrWidth bits))
    // Pc source (for branch/jump)
    val pcSrcE = out(PcSrc())
    // Address of rd (for writeback to Decode)
    val rdE = out(Bits(rfWidth bits))
    // Data of rd (for writeback to Decode)
    val rdDataE = out(Bits(dataWidth bits))
    // Regfile write enable (for writeback to Decode)
    val regWriteE = out(Bool())
    // Dmem interface
    val dmem = master(PortDmem())
  }

  // Shared wires between areas
  val aluResult = Bits(dataWidth bits)
  val memResult = Bits(dataWidth bits)

  val areaE = new Area {
    val alu = Alu().setName("alu_u")
    val bu = BranchUnit().setName("bu_u")

    // TODO
  }

  val areaM = new Area {
    // TODO
  }

  val areaW = new Area {
    // TODO
  }

  // TODO
}

object StageExcuteSim extends App {
  Config.sim.compile(StageExcute()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object StageExcuteVerilog extends App {
  Config.spinal.generateVerilog(StageExcute())
}

object StageExcuteVhdl extends App {
  Config.spinal.generateVhdl(StageExcute())
}
