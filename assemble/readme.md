# Assemble Module
## Introduction
* This module aims to collect necessary information, especially images of the boxes to make sure all required components are in it.
* It is a component to guarantee the boxing procedure and provide an approach for the staff to post check each box.
* For each box, a barcode with two copies will be allocated, one ticked inside while the other outside.
## Functions
* Barcode generation. The system should generate a batch of barcode automatically for a given number.
* Bind image with a barcode. Worker in the factory should be able to upload a photo once he/she scan the barcode from his/her mobile. System will record it and provide access to the resource later.
* Barcode information obtain. The system should provide a list of binding information in step 2, worker can check the image to see whether all the components are available. If not, he/she should mark the record for further processing.
## Data Structure
1. Barcode
* Columns:
code_id | code_value | block_flag | create_time
2. Snapshot
* Columns:
snapshot_id | code_value | snapshot_path | block_flag | create_time
3. CheckRecord
* Columns:
record_id | snapshot_id | record_status | block_flag | create_time

## Controller
1.BarcodeController ("/assemble/barcode")
*       //根据数量创建条形码,最少1条,最多1000条
        @PostMapping("/batch/create")
        public ResultData createBatch(int number);
        
*       //按照条形码值查询条形码
        @GetMapping("/fetch")
        public ResultData fetchBarcode(String codeValue);
        
2.SnapshotController
*       //工人上传一张图片时触发
        @RequestMapping(method = RequestMethod.POST, value = "/pic/upload")
        public ResultData uploadPic(MultipartHttpServletRequest request);
*       //工人提交图片对应url和条形码时触发,创建snapshot表单
        @RequestMapping(method = RequestMethod.POST, value = "/create")
        public ResultData createSnapshot(String codeValue, String snapshotPath);
*       //显示snapshot list
        @RequestMapping(method = RequestMethod.GET, value = "/fetch")
        public ResultData fetchSnapshot(String startTime, String endTime, String codeValue);
        
3.CheckRecordController ("/assemble/checkrecord")
*       //创建check record
        @PostMapping("/create") 
        public ResultData createCheckRecord(String snapshotId, boolean recordStatus);

*       //根据checkStatus查询check record
        @GetMapping("/fetch/bystatus")
        public ResultData fetchCheckRecordByStatus(boolean recordStatus);
