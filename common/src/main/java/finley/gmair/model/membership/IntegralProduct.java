package finley.gmair.model.membership;

import finley.gmair.model.Entity;

/**
 * @ClassName IntegralProduct
 * @Description 产品表
 * @Author Joby
 * @Date 2021/7/16 16:37
 */
public class IntegralProduct extends Entity {
    private String productID;
    private String productName;
    public IntegralProduct(){

    }
    public IntegralProduct(String productID){
        this.productID = productID;
    }

    public IntegralProduct(String productID, String productName) {
        this.productID = productID;
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
