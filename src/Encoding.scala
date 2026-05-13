package Qin110

import spinal.core._
import spinal.core.sim._
import spinal.lib._
import Qin110.Constant._
import Qin110.{MainConfig => cfg}

// scalafmt: { align.preset = more }

object OpCode {
  def op     = B("0110011")
  def opimm  = B("0010011")
  def load   = B("0000011")
  def store  = B("0100011")
  def branch = B("1100011")
  def jal    = B("1101111")
  def jalr   = B("1100111")
  def lui    = B("0110111")
  def auipc  = B("0010111")
}

object F3Alu {
  def add  = B("000")
  def sll  = B("001")
  def srl  = B("101")
  def xor  = B("100")
  def or   = B("110")
  def and  = B("111")
  def slt  = B("010")
  def sltu = B("011")
}

object F3Mem {
  def byte  = B("000")
  def half  = B("001")
  def word  = B("010")
  def byteu = B("100")
  def halfu = B("101")
}

object F3Branch {
  def beq  = B("000")
  def bne  = B("001")
  def blt  = B("100")
  def bge  = B("101")
  def bltu = B("110")
  def bgeu = B("111")
}

object F7 {
  def alup = B("0000000")
  def alun = B("0100000")
}

object BranchOp extends SpinalEnum {
  val pc4  = newElement("pc4_")
  val jump = newElement("jump_")
  val beq  = newElement("beq_")
  val bne  = newElement("bne_")
  val blt  = newElement("blt_")
  val bge  = newElement("bge_")
}

object MemOp extends SpinalEnum {
  val byte  = newElement("byte_")
  val half  = newElement("half_")
  val word  = newElement("word_")
  val byteu = newElement("byteu_")
  val halfu = newElement("halfu_")
}

object AluOp extends SpinalEnum {
  val add  = newElement("add_")
  val sub  = newElement("sub_")
  val sll  = newElement("sll_")
  val srl  = newElement("srl_")
  val sra  = newElement("sra_")
  val xor  = newElement("xor_")
  val or   = newElement("or_")
  val and  = newElement("and_")
  val slt  = newElement("slt_")
  val sltu = newElement("sltu_")
  val srca = newElement("srca_")
  val srcb = newElement("srcb_")
}

object IntSrcA extends SpinalEnum {
  val rs1 = newElement("rs1_")
  val pc  = newElement("pc_")
}

object IntSrcB extends SpinalEnum {
  val rs2 = newElement("rs2_")
  val imm = newElement("imm_")
}

object PcAddSrcA extends SpinalEnum {
  val rs1 = newElement("rs1_")
  val pc  = newElement("pc_")
}

object ImmSrc extends SpinalEnum {
  val i1 = newElement("i1_")
  val i2 = newElement("i2_")
  val s  = newElement("s_")
  val b  = newElement("b_")
  val j  = newElement("j_")
  val u  = newElement("u_")
}

object ResultSrc extends SpinalEnum {
  val alu = newElement("alu_")
  val mem = newElement("mem_")
  val pc4 = newElement("pc4_")
}

object PcSrc extends SpinalEnum {
  val pc4      = newElement("pc4_")
  val pctarget = newElement("pctarget_")
}

object Inst extends SpinalEnum {
  val invaild = newElement("invaild_")
  val add     = newElement("add_")
  val sub     = newElement("sub_")
  val sll     = newElement("sll_")
  val srl     = newElement("srl_")
  val sra     = newElement("sra_")
  val xor     = newElement("xor_")
  val or      = newElement("or_")
  val and     = newElement("and_")
  val slt     = newElement("slt_")
  val sltu    = newElement("sltu_")
  val addi    = newElement("addi_")
  val slli    = newElement("slli_")
  val srli    = newElement("srli_")
  val srai    = newElement("srai_")
  val xori    = newElement("xori_")
  val ori     = newElement("ori_")
  val andi    = newElement("andi_")
  val slti    = newElement("slti_")
  val sltui   = newElement("sltui_")
  val jalr    = newElement("jalr_")
  val lb      = newElement("lb_")
  val lh      = newElement("lh_")
  val lw      = newElement("lw_")
  val lbu     = newElement("lbu_")
  val lhu     = newElement("lhu_")
  val sb      = newElement("sb_")
  val sh      = newElement("sh_")
  val sw      = newElement("sw_")
  val beq     = newElement("beq_")
  val bne     = newElement("bne_")
  val blt     = newElement("blt_")
  val bge     = newElement("bge_")
  val bltu    = newElement("bltu_")
  val bgeu    = newElement("bgeu_")
  val jal     = newElement("jal_")
  val lui     = newElement("lui_")
  val auipc   = newElement("auipc_")
}
