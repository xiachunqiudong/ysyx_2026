package ifu

import chisel3._
import chisel3.util._
import config._

class ifuTop extends XModule {
  val io = IO(new Bundle{
    val instValidVec = Output(Vec(IFU_WIDTH, Bool()))
    val inst         = Output(Vec(IFU_WIDTH, UInt(32.W)))
  })

  for (i <- 0  until IFU_WIDTH) {
    io.instValidVec(i) := true.B
    io.inst(i) := i.U
  }

}

