object Elaborate extends App {

  val firtoolOptions = Array(
    "-O=release",
    "--disable-all-randomization",
    "--split-verilog",
    "-strip-debug-info",
    "-strip-fir-debug-info",
    "--add-mux-pragmas",
    "--emit-separate-always-blocks",
    "--lowering-options=explicitBitcast,disallowLocalVariables,disallowPortDeclSharing,locationInfoStyle=none",
    "--disable-aggressive-merge-connections",
    "--verilog"
  )

  circt.stage.ChiselStage.emitSystemVerilogFile(new ifu.ifuTop(), args, firtoolOptions)
}
