package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class ControlUnit() extends Component {
  val io = new Bundle {
    // opcode
    val opcode = in(Bits(opcodeWidth bits))
    // funct3
    val funct3 = in(Bits(funct3Width bits))
    // funct7
    val funct7 = in(Bits(funct7Width bits))
    // resultSrc (-> Stage W, ResultIntMux)
    val resultSrc = out(ResultSrc())
    // regWrite  (-> Stage D, RegfileInt)
    val regWrite = out(Bool())
    // memWrite  (-> Stage M, Dmem)
    val memWrite = out(Bool())
    // memOp     (-> Stage M, Dmem)
    val memOp = out(MemOp())
    // branchOp  (-> Stage E, BranchUnit)
    val branchOp = out(BranchOp())
    // aluOp     (-> Stage E, Alu)
    val aluOp = out(AluOp())
    // intSrcA   (-> Stage E, IntSrcAMux)
    val intSrcA = out(IntSrcA())
    // intSrcB   (-> Stage E, IntSrcBMux)
    val intSrcB = out(IntSrcB())
    // pcAddSrcA (-> Stage E, PcAddSrcAMux)
    val pcAddSrcA = out(PcAddSrcA())
    // immSrc    (-> Stage D, ImmExtend)
    val immSrc = out(ImmSrc())
  }

  // TODO
}

object ControlUnitSim extends App {
  Config.sim.compile(ControlUnit()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object ControlUnitVerilog extends App {
  Config.spinal.generateVerilog(ControlUnit())
}

object ControlUnitVhdl extends App {
  Config.spinal.generateVhdl(ControlUnit())
}
