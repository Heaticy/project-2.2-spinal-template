package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class RegisterFD(asReg: Boolean = false) extends Bundle {
  // Data
  val pc = Bits(addrWidth bits)
  val instr = Bits(dataWidth bits)

  if (asReg) {
    pc.setAsReg().init(cfg.rom.base)
    instr.setAsReg().init(0)
  }
}

case class StageFetch() extends Component {
  val io = new Bundle {
    // Register F/D
    val regFD = out(RegisterFD(asReg = true))
    // Imem interface
    val imem = master(PortImem())
    // Pc next select
    val pcSrcE = in(PcSrc())
    // Pc target (if jump)
    val pcTargetE = in(Bits(addrWidth bits))
    // Flush register F/D
    val flushFD = in(Bool())
  }

  // Pc register
  val pcReg = Reg(Bits(addrWidth bits)).simPublic() init (cfg.rom.base)

  // TODO
}

object StageFetchSim extends App {
  Config.sim.compile(StageFetch()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object StageFetchVerilog extends App {
  Config.spinal.generateVerilog(StageFetch())
}

object StageFetchVhdl extends App {
  Config.spinal.generateVhdl(StageFetch())
}
