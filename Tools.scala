package Qin110

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.io.PrintWriter
import java.io.File
import scala.util.Random

object Tools {
  def programPath(name: String): Path = {
    val buildBin = Paths.get(name, "build", s"${name}.bin")
    val prebuiltBin = Paths.get("prebuilt", s"${name}.bin")

    if (Files.exists(buildBin)) buildBin
    else if (Files.exists(prebuiltBin)) prebuiltBin
    else {
      throw new java.io.FileNotFoundException(
        s"Missing program binary for ${name}: expected ${buildBin} or ${prebuiltBin}"
      )
    }
  }

  def loadProgram(name: String): Array[BigInt] = {
    val bytes = Files.readAllBytes(programPath(name))
    val words = bytes.grouped(4).map { g => BigInt(g.reverse.padTo(4, 0x00.toByte)) & 0xffffffffL }.toArray
    words
  }

  def loadArray(arr: Array[Int]): Array[BigInt] = {
    val words = arr.map { e => BigInt(e) & 0xffffffffL }.toArray
    words
  }

  def dataWr(memWr: (Long, BigInt) => Unit, words: Array[BigInt], base: Long): Unit = {
    words.zipWithIndex.foreach { case (w, i) => memWr(i * 4 + base, w) }
  }

  def dataRd(memRd: (Long) => BigInt, size: Int, step: Int, base: Long): Array[BigInt] = {
    (0 until size).map { case (i) => memRd(i * 4 * step + base) }.toArray
  }

  def createLog(name: String): PrintWriter = {
    new PrintWriter(new File("build/" + name + ".txt"))
  }

  def logTitle(file: PrintWriter, name: String, cycles: Int, result: Boolean = true): Unit = {
    println("@@@@@@@@@@@@@@@@@@")
    println("# " + name + ": " + strCheck(result))
    println("# cycles = " + cycles.toString)
    println("@@@@@@@@@@@@@@@@@@")
    file.println("@@@@@@@@@@@@@@@@@@")
    file.println("# " + name + ": " + strCheck(result))
    file.println("# cycles = " + cycles.toString)
    file.println("@@@@@@@@@@@@@@@@@@")
    file.println("")
  }

  def logProgram(file: PrintWriter, program: Array[BigInt]): Unit = {
    file.println("PROGRAM LENGTH = " + program.length.toString + "\n" + strProgram(program))
    file.println("")
  }

  def logText(file: PrintWriter, prefix: String, str: String): Unit = {
    file.println(prefix + ": " + str)
    file.println("")
  }

  def strProgram(program: Array[BigInt]) = {
    program.map { w => f"${w}%08X" }.mkString("\n")
  }

  def strArray(arr: Array[Int]): String = {
    "[" + arr.mkString(", ") + "]"
  }

  def strCheck(result: Boolean): String = {
    if (result) "PASS" else "FAIL"
  }

  def eqArray(arr1: Array[Int], arr2: Array[Int]): Boolean = {
    arr1.zip(arr2).map { case (e1, e2) => e1 == e2 }.reduce(_ && _)
  }
}
