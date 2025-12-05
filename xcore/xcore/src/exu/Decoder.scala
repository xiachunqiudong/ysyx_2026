package exu

import chisel3._
import chisel3.util._
import config._
import xcoreBundle._

trait DecoderTrait {
  def X = BitPat("b?")
  def N = BitPat("b0")
  def Y = BitPat("b1")
}

// object RVI_instTable extends DecoderTrait{

//   def ADD                = BitPat("b0000000??????????000?????0110011")
//   def SUB                = BitPat("b0000001??????????000?????0110011")

//   val table: Array[(BitPat, List[BitPat])] = Array(ADD -> List(Y, N),
//                                                    SUB -> List(Y, N))

// }

class Decoder extends XModule with DecoderTrait{
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val uop  = Output(new Uop)
  })

  io.uop.lsrc1 := io.inst(19,15)
  io.uop.lsrc2 := io.inst(24,20)
  io.uop.ldst  := io.inst(11,7)

  // val inst_code = ListLookup(io.inst, List(Y, N), RVI_instTable.table)

}
