package exu

import chisel3._
import chisel3.util._
import config._
import xcoreBundle._

class Decoder extends XModule {
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val uop  = Output(new Uop)
  })

  io.uop.lsrc1 := io.inst(19,15)
  io.uop.lsrc2 := io.inst(24,20)
  io.uop.ldst  := io.inst(11,7)

}
