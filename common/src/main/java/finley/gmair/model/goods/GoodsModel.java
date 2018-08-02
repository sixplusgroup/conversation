package finley.gmair.model.goods;

import finley.gmair.model.Entity;
import finley.gmair.model.machine.ModelVolume;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/24
 */
public class GoodsModel extends Entity {
    private String modelId;

    private String goodsId;

    private String modelCode;

    private String modelName;

    private ModelVolume volume;

    public GoodsModel() {
        super();
    }

    public GoodsModel(String goodsId, String modelCode, String modelName) {
        this();
        this.goodsId = goodsId;
        this.modelCode = modelCode;
        this.modelName = modelName;
    }

    public GoodsModel(String goodsId, String modelCode, String modelName, int minVolume, int maxVolume) {
        this(goodsId, modelCode, modelName);
        this.volume = new ModelVolume(minVolume, maxVolume);
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public ModelVolume getVolume() {
        return volume;
    }

    public void setVolume(ModelVolume volume) {
        this.volume = volume;
    }
}