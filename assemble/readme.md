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