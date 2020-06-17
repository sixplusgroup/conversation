package finley.gmair.model.drift;

import finley.gmair.model.Entity;

/**
 * @ClassName: Thumbnail
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/14 10:10 AM
 */
public class Thumbnail extends Entity {
    private String thumbnailId;

    private String thumbnailPath;

    private int thumbnailIndex;

    public Thumbnail() {
        super();
    }

    public Thumbnail(String thumbnailId, String thumbnailPath, int thumbnailIndex) {
        this();
        this.thumbnailId = thumbnailId;
        this.thumbnailPath = thumbnailPath;
        this.thumbnailIndex = thumbnailIndex;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getThumbnailIndex() {
        return thumbnailIndex;
    }

    public void setThumbnailIndex(int thumbnailIndex) {
        this.thumbnailIndex = thumbnailIndex;
    }
}
