package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.lib.io.TriStateArray
import Qin110.Constant._
import Qin110.{MainConfig => cfg}
import Qin110.Tools.loadProgram

case class Qin110Top(romInit: Array[BigInt] = Array.fill(cfg.rom.words.toInt)(0), ramInit: Array[BigInt] = Array.fill(cfg.ram.words.toInt)(0)) extends Component {
  val io = new Bundle {}

  // Qin110 core
  val core = Qin110Core()

  // Dmem bus
  val dbus = BusDmem()
  // Imem bus
  val ibus = BusImem()

  // Rom
  val rom = Rom(romInit).setName("rom_m")
  // Ram
  val ram = Ram(ramInit).setName("ram_u")
  // Simulation Reg
  val msreg = SimulationReg().setName("msreg_u")

  // Connect mem port
  core.io.imem <> ibus.io.piMaster
  core.io.dmem <> dbus.io.pdMaster
  // Imem
  rom.io.imem <> ibus.io.piRom
  // Dmem
  rom.io.dmem <> dbus.io.pdRom
  ram.io.dmem <> dbus.io.pdRam
  msreg.io.dmem <> dbus.io.pdSreg

  // Set word for simulation
  def setWord(m: Mem[Bits], base: Long, addr: Long, data: BigInt): Unit = {
    m.setBigInt((addr - base) >> byteShift, data)
  }

  def setWord(m: Vec[Bits], base: Long, addr: Long, data: BigInt): Unit = {
    m.toArray.apply(((addr - base) >> byteShift).toInt) #= data
  }

  // Get word for simulation
  def getWord(m: Mem[Bits], base: Long, addr: Long): BigInt = {
    m.getBigInt((addr - base) >> byteShift)
  }

  def getWord(m: Vec[Bits], base: Long, addr: Long): BigInt = {
    m.toArray.apply(((addr - base) >> byteShift).toInt).toBigInt
  }

  // Mem write interface for simulation
  def memWr(addr: Long, data: BigInt): Unit = {
    addr match {
      case a if cfg.rom.in(a)  => setWord(rom.m, cfg.rom.base, addr, data)
      case a if cfg.ram.in(a)  => setWord(ram.m, cfg.ram.base, addr, data)
      case a if cfg.sreg.in(a) => setWord(msreg.m, cfg.sreg.base, addr, data)
    }
  }

  // Mem read interface for simulation
  def memRd(addr: Long): BigInt = {
    addr match {
      case a if cfg.rom.in(a)  => getWord(rom.m, cfg.rom.base, addr)
      case a if cfg.ram.in(a)  => getWord(ram.m, cfg.ram.base, addr)
      case a if cfg.sreg.in(a) => getWord(msreg.m, cfg.sreg.base, addr)
      case _                   => BigInt(0)
    }
  }
}

object Qin110TopSim extends App {
  // Simulation
  val key = args(0)
  Config.sim.compile(Qin110Top(romInit = loadProgram(key))).doSim { dut =>
    // Get test object by key match
    val test: Test = key match {
      case "TestRV32IFull" => new TestRV32IFull(dut.memWr, dut.memRd)
      case "TestRV32IPart" => new TestRV32IPart(dut.memWr, dut.memRd)
      case other           => new Test(other, dut.memWr, dut.memRd)
    }

    // Load program and data
    test.prSim()

    // Set clock
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

    // Print stdio start
    println("---------------- STDIO ----------------")

    // Start running
    for (cycles <- 0 until cfg.cycleMax) {
      // Detect if halt is set
      if (dut.memRd(cfg.sreg.halt) != 0) {
        // Print stdio end
        print("\n")
        println("---------------- STDIO ----------------")
        // Show result
        test.poSim(cycles)
        // Stop running
        simSuccess()
      }

      // Put char
      if (dut.memRd(cfg.sreg.stdo) != 0) {
        print(dut.memRd(cfg.sreg.stdo).toChar)
        dut.memWr(cfg.sreg.stdo, 0)
      }

      // Next cycle
      dut.clockDomain.waitRisingEdge()
    }

    // Print stdio end
    print("\n")
    println("---------------- STDIO ----------------")
    // Stop running
    simSuccess()
  }
}

object Qin110TopVerilog extends App {
  Config.spinal.generateVerilog(Qin110Top(romInit = loadProgram(args(0))))
}

object Qin110TopVhdl extends App {
  Config.spinal.generateVhdl(Qin110Top(romInit = loadProgram(args(0))))
}
