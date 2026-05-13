package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

case class PortDmem(dummyMaster: Boolean = false, dummySlave: Boolean = false) extends Bundle with IMasterSlave {
  // Address to access
  val addr = Bits(addrWidth bits)
  // Data read from slave to master
  val dataRd = Bits(dataWidth bits)
  // Data write from master to slave
  val dataWr = Bits(dataWidth bits)
  // Data write enable
  val enableWr = Bool()
  // Data write mask
  val length = BusLength()
  // Is select
  val select = Bool()

  // Set io direction
  def asMaster(): Unit = {
    in(dataRd)
    out(addr, dataWr, enableWr, length, select)
  }

  // Generate dummy master
  if (dummyMaster) {
    dataRd := 0
  }

  // Generate dummy slave
  if (dummySlave) {
    addr := 0
    dataWr := 0
    enableWr := False
    length := BusLength.word
    select := False
  }

  // Helper function to generate mask
  def maskGen(mtail: UInt, length: SpinalEnumCraft[BusLength.type]): Bits = {
    val mask = B("0000")
    when(length === BusLength.word) {
      // word
      switch(mtail) {
        is(U("00")) { mask := B("1111") }
      }
    }
    when(length === BusLength.half) {
      // half
      switch(mtail) {
        is(U("00")) { mask := B("0011") }
        is(U("10")) { mask := B("1100") }
      }
    }
    when(length === BusLength.byte) {
      // byte
      switch(mtail) {
        is(U("00")) { mask := B("0001") }
        is(U("01")) { mask := B("0010") }
        is(U("10")) { mask := B("0100") }
        is(U("11")) { mask := B("1000") }
      }
    }
    return mask
  }

  // Helper function to handle preprocess of data write
  // Before write, data at lsb should be placed to correct position
  def processWr(mask: Bits, din: Bits): Bits = {
    val dout = B(0, dataWidth bits)
    switch(mask) {
      // word
      is(B("1111")) { dout(rangeWord(0)) := din(rangeWord()) }
      // half
      is(B("0011")) { dout(rangeHalf(0)) := din(rangeHalf()) }
      is(B("1100")) { dout(rangeHalf(1)) := din(rangeHalf()) }
      // byte
      is(B("0001")) { dout(rangeByte(0)) := din(rangeByte()) }
      is(B("0010")) { dout(rangeByte(1)) := din(rangeByte()) }
      is(B("0100")) { dout(rangeByte(2)) := din(rangeByte()) }
      is(B("1000")) { dout(rangeByte(3)) := din(rangeByte()) }
      // otherwise
      default { dout := din }
    }
    return dout
  }

  // Helper function to handle preprocess of data read
  // After read, data should be placed to lsb
  def processRd(mask: Bits, din: Bits): Bits = {
    val dout = B(0, dataWidth bits)
    switch(mask) {
      // word
      is(B("1111")) { dout := din(rangeWord(0)).asBits.resized }
      // half
      is(B("0011")) { dout := din(rangeHalf(0)).asBits.resized }
      is(B("1100")) { dout := din(rangeHalf(1)).asBits.resized }
      // byte
      is(B("0001")) { dout := din(rangeByte(0)).asBits.resized }
      is(B("0010")) { dout := din(rangeByte(1)).asBits.resized }
      is(B("0100")) { dout := din(rangeByte(2)).asBits.resized }
      is(B("1000")) { dout := din(rangeByte(3)).asBits.resized }
      // otherwise
      default { dout := din }
    }
    return dout
  }

  // Operation of dmem
  def operate(mrd: (UInt) => Bits, mwr: (UInt, Bits, Bits) => Unit, mdef: MemoryConfig) {
    // Local address on memory
    val maddr = mdef.maddr(addr)
    val mtail = mdef.mtail(addr)
    val mask = maskGen(mtail, length)

    // If memory allow read
    dataRd := 0
    if (mdef.allowRd) {
      when(mdef.enableRd(addr)) { dataRd := processRd(mask, mrd(maddr)) }
    }

    // If memory allow write
    if (mdef.allowWr) {
      when(mdef.enableWr(addr) && enableWr && select) { mwr(maddr, processWr(mask, dataWr), mask) }
    }
  }

  // Operation of dmem for Mem
  def operate(m: Mem[Bits], mdef: MemoryConfig) {
    def mrd(a: UInt): Bits = { m.readAsync(a) }
    def mwr(a: UInt, d: Bits, mask: Bits): Unit = { m.write(a, d, mask = mask) }
    operate(mrd _, mwr _, mdef)
  }

  // Operation of dmem for Reg
  def operate(m: Vec[Bits], mdef: MemoryConfig) {
    def mrd(a: UInt): Bits = { m(a) }
    def mwr(a: UInt, d: Bits, mask: Bits): Unit = { for (i <- 0 until byteCount) { when(mask(i)) { m(a)(rangeByte(i)) := d(rangeByte(i)) } } }
    operate(mrd _, mwr _, mdef)
  }
}

case class BusDmem() extends Component {
  val io = new Bundle {
    // Port for risc-v core
    val pdMaster = slave(PortDmem())
    // Port for rom
    val pdRom = master(PortDmem())
    // Port for ram
    val pdRam = master(PortDmem())
    // Port for sreg
    val pdSreg = master(PortDmem())
  }

  // Get address
  val addr = io.pdMaster.addr

  // Conncect each port with dummy slave and master as default
  io.pdMaster <> PortDmem(dummyMaster = true)
  io.pdRom <> PortDmem(dummySlave = true)
  io.pdRam <> PortDmem(dummySlave = true)
  io.pdSreg <> PortDmem(dummySlave = true)
  // Mux slave port to master
  when(cfg.rom.mux(addr)) { io.pdRom <> io.pdMaster }
  when(cfg.ram.mux(addr)) { io.pdRam <> io.pdMaster }
  when(cfg.sreg.mux(addr)) { io.pdSreg <> io.pdMaster }
}

object BusLength extends SpinalEnum {
  val byte = newElement("byte_")
  val half = newElement("half_")
  val word = newElement("word_")
}

object BusDmemSim extends App {
  Config.sim.compile(BusDmem()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object BusDmemVerilog extends App {
  Config.spinal.generateVerilog(BusDmem())
}

object BusDmemVhdl extends App {
  Config.spinal.generateVhdl(BusDmem())
}
