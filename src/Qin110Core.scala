package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class Qin110Core() extends Component {
  val io = new Bundle {
    // Imem interface
    val imem = master(PortImem())
    // Dmem interface
    val dmem = master(PortDmem())
  }

  // Fetch
  val stageF = StageFetch()
  // Decode
  val stageD = StageDecode()
  // Excute
  val stageE = StageExcute()

  // Hazard
  val hazard = Hazard().setName("hazard_u")

  // Connect memory interface
  io.imem <> stageF.io.imem
  io.dmem <> stageE.io.dmem

  // TODO: connect all inter-stage signals
}

object Qin110CoreSim extends App {
  Config.sim.compile(Qin110Core()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object Qin110CoreVerilog extends App {
  Config.spinal.generateVerilog(Qin110Core())
}

object Qin110CoreVhdl extends App {
  Config.spinal.generateVhdl(Qin110Core())
}
