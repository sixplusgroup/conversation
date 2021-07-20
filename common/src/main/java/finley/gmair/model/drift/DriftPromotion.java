package finley.gmair.model.drift;

import finley.gmair.model.Entity;

/**
 * @ClassName: DriftPromotion
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/16 3:02 PM
 */
public class DriftPromotion extends Entity {
    private String promotionId;

    private String activityId;

    private double price;

    private String description;

    public DriftPromotion() {
        super();
    }

    public DriftPromotion(String activityId, double price) {
        this();
        this.activityId = activityId;
        this.price = price;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
