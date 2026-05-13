package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class PortImem(dummyMaster: Boolean = false, dummySlave: Boolean = false) extends Bundle with IMasterSlave {
  // Address to access
  val addr = Bits(addrWidth bits)
  // Data read from slave to master
  val dataRd = Bits(dataWidth bits)
  // Is select
  val select = Bool()

  // Set io direction
  def asMaster(): Unit = {
    in(dataRd)
    out(addr, select)
  }

  // Generate dummy master
  if (dummyMaster) {
    dataRd := 0
  }

  // Generate dummy slave
  if (dummySlave) {
    addr := 0
    select := False
  }

  // Operation of imem
  def operate(mrd: (UInt) => Bits, mdef: MemoryConfig) {
    // Local address on memory
    val maddr = mdef.maddr(addr)

    // If memory allow read
    dataRd := 0
    if (mdef.allowRd) {
      when(mdef.enableRd(addr)) { dataRd := mrd(maddr) }
    }
  }

  // Operation of imem for Mem
  def operate(m: Mem[Bits], mdef: MemoryConfig) {
    def mrd(a: UInt): Bits = { m.readAsync(a) }
    operate(mrd _, mdef)
  }

  // Operation of imem for Reg
  def operate(m: Vec[Bits], mdef: MemoryConfig) {
    def mrd(a: UInt): Bits = { m(a) }
    operate(mrd _, mdef)
  }
}

case class BusImem() extends Component {
  val io = new Bundle {
    // Port for risc-v core
    val piMaster = slave(PortImem())
    // Port for rom
    val piRom = master(PortImem())
  }

  // Get address
  val addr = io.piMaster.addr

  // Conncect each port with dummy slave and master as default
  io.piMaster <> PortImem(dummyMaster = true)
  io.piRom <> PortImem(dummySlave = true)
  // Mux slave port to master
  when(cfg.rom.mux(addr)) { io.piRom <> io.piMaster }
}

object BusImemSim extends App {
  Config.sim.compile(BusImem()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object BusImemVerilog extends App {
  Config.spinal.generateVerilog(BusImem())
}

object BusImemVhdl extends App {
  Config.spinal.generateVhdl(BusImem())
}
