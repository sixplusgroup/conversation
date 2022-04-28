package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class PictureMd5 extends Entity {

    private String imageId;

    private String picturePath;

    private String md5;

    public PictureMd5(){
        super();
    }

    public PictureMd5(String picturePath, String md5) {
        this();
        this.picturePath = picturePath;
        this.md5 = md5;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }


    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
