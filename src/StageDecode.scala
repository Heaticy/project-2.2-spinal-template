package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class RegisterDE(asReg: Boolean = false) extends Bundle {
  // Data
  val pc = Bits(addrWidth bits)
  val pcPlus4 = Bits(addrWidth bits)
  val rs1Data = Bits(dataWidth bits)
  val rs2Data = Bits(dataWidth bits)
  val immExt = Bits(dataWidth bits)
  val rd = Bits(rfWidth bits)
  // Control
  val regWrite = Bool()
  val resultSrc = ResultSrc()
  val memWrite = Bool()
  val memOp = MemOp()
  val branchOp = BranchOp()
  val aluOp = AluOp()
  val intSrcA = IntSrcA()
  val intSrcB = IntSrcB()
  val pcAddSrcA = PcAddSrcA()

  if (asReg) {
    pc.setAsReg().init(cfg.rom.base)
    pcPlus4.setAsReg().init(cfg.rom.base)
    rs1Data.setAsReg().init(0)
    rs2Data.setAsReg().init(0)
    immExt.setAsReg().init(0)
    rd.setAsReg().init(0)
    regWrite.setAsReg().init(False)
    resultSrc.setAsReg().init(ResultSrc.alu)
    memWrite.setAsReg().init(False)
    memOp.setAsReg().init(MemOp.word)
    branchOp.setAsReg().init(BranchOp.pc4)
    aluOp.setAsReg().init(AluOp.add)
    intSrcA.setAsReg().init(IntSrcA.rs1)
    intSrcB.setAsReg().init(IntSrcB.rs2)
    pcAddSrcA.setAsReg().init(PcAddSrcA.pc)
  }
}

case class StageDecode() extends Component {
  val io = new Bundle {
    // Register D/E
    val regDE = out(RegisterDE(asReg = true))
    // Register F/D
    val regFD = in(RegisterFD())
    // Address of rd (from EX stage, for writeback)
    val rdE = in(Bits(rfWidth bits))
    // Data of rd (from EX stage, for writeback)
    val rdDataE = in(Bits(dataWidth bits))
    // Regfile write enable (from EX stage, for writeback)
    val regWriteE = in(Bool())
    // Flush register D/E
    val flushDE = in(Bool())
  }

  // ControlUnit
  val cu = ControlUnit().setName("cu_u")
  // RegisterFile
  val rf = RegisterFile().setName("rf_u")
  // ImmExtend
  val ext = ImmExtend().setName("ext_u")

  // TODO
}

object StageDecodeSim extends App {
  Config.sim.compile(StageDecode()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object StageDecodeVerilog extends App {
  Config.spinal.generateVerilog(StageDecode())
}

object StageDecodeVhdl extends App {
  Config.spinal.generateVhdl(StageDecode())
}
