package ifu

import chisel3._
import chisel3.util._

class InstFifoEntry extends Module {
  val io = IO(new Bundle {
    val wen     = Input(Bool())
    val inst_In = Input(UInt(32.W))
    val inst_Q  = Output(UInt(32.W))
  })

  val instReg = RegEnable(io.inst_In, io.wen)

  io.inst_Q := instReg

}

class InstFifo (EntryNum: Int) extends Module {
  val io = IO(new Bundle {
    val push    = Input(Bool())
    // val pop     = Input(Bool())
    val inst_In = Input(UInt(32.W))
    val inst_Q  = Output(UInt(32.W))
  })

  val entryArray = Seq.fill(EntryNum)(Module(new InstFifoEntry))

// Write
  val writePtrDcd_In = Wire(UInt(EntryNum.W))
  val writePtrDcd_Q  = RegInit(0.U(EntryNum.W))

  writePtrDcd_In := Cat(writePtrDcd_Q(EntryNum-2,0), writePtrDcd_Q(EntryNum-1))

  when (io.push) {
    writePtrDcd_Q := writePtrDcd_In
  }

// Read

  for (e <- 0 until EntryNum) {
    entryArray(e).io.wen     := io.push & (writePtrDcd_Q(e))
    entryArray(e).io.inst_In := io.inst_In
  }

  io.inst_Q := entryArray(0).io.inst_Q

}

class InstQueue (EntryNum: Int, BankNum: Int, ReadPotr: Int, WritePort: Int) extends Module {
  val io = IO(new Bundle{
    val fetchValidVec = Input(Vec(WritePort, Bool()))
    val fetchInstVec  = Input(Vec(WritePort, UInt(32.W)))
  })

  def bitRotateLeft(data: UInt, shiftNum: UInt): UInt = {
    val W = data.getWidth
    val shiftCandidates = (0 until W).map { k =>
      if (k == 0) data
      else Cat(data((W-1-k), 0), data((W-1), (W-k)))
    }
    Mux1H(UIntToOH(shiftNum), shiftCandidates)
  }

  val bankPtrWidth = log2Ceil(BankNum)

  val EntryNumPerBank = EntryNum / BankNum

  val InstFifoVec = Seq.fill(BankNum)(Module(new InstFifo(EntryNum=EntryNumPerBank)))

  val bankWritePtr_Q = RegInit(0.U(bankPtrWidth.W))

  for (bank <- 0 until BankNum) {
    InstFifoVec(bank).io.push    := true.B
    InstFifoVec(bank).io.inst_In :=  io.fetchInstVec(0)
  }

  
  val fetchValidVecShift = bitRotateLeft(io.fetchValidVec.asUInt, bankWritePtr_Q)
  dontTouch(fetchValidVecShift)
}