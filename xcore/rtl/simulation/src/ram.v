module ram #(
  parameter DW = 128, // 16Byte
  parameter AW = 16
) (
  input  wire          clk,
  input  wire [AW-1:0] addr,
  input  wire          ren,
  input  wire          wen,
  input  wire [DW-1:0] wdata,
  output wire [DW-1:0] rdata
);

  parameter DEPTH = 1 << AW;

  reg [DW-1:0] dataArray [DEPTH-1:0];
  reg [DW-1:0] rdata_Q;

// read
  always @(posedge clk ) begin
    if (ren) begin
      rdata_Q[DW-1:0] <= dataArray[addr];
    end
  end

// write
  always @(posedge clk ) begin
    if (wen) begin
      dataArray[addr] <= wdata[DW-1:0];
    end
  end

endmodule
