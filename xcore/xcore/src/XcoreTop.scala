package xcore

import chisel3._
import chisel3.util._
import config._
import ifu._
import exu._

class XcoreTop extends XModule {

  val ifuTop = Module(new IfuTop)

  val decoderVec = Seq.fill(IFU_WIDTH)(Module(new Decoder))

  for (i <- 0 until IFU_WIDTH) {
    decoderVec(i).io.inst := ifuTop.io.instVec(i)
  }

}