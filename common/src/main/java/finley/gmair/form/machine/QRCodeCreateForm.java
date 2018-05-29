package finley.gmair.form.machine;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/29
 */
public class QRCodeCreateForm {
    private String goodsId;

    private String modelId;

    private String batchValue;

    private int num;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getBatchValue() {
        return batchValue;
    }

    public void setBatchValue(String batchValue) {
        this.batchValue = batchValue;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}