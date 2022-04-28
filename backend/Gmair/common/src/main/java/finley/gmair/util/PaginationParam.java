

package finley.gmair.util;


import java.util.List;

public class PaginationParam<T> {

    /**
     * 查询数据列表
     */
    private List<T> records;
    /**
     * 总数
     */
    private long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;

    /**
     * false 默认顺序, true 反序
     */
    private Boolean createTimeSort = false;

    public Boolean getCreateTimeSort() {
        return createTimeSort;
    }

    public void setCreateTimeSort(Boolean createTimeSort) {
        this.createTimeSort = createTimeSort;
    }

    public List<T> getRecords() {
        return this.records;
    }

    public PaginationParam<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public long getTotal() {
        return this.total;
    }

    public PaginationParam<T> setTotal(long total) {
        this.total = total;
        return this;
    }


    public long getSize() {
        return this.size;
    }

    public PaginationParam<T> setSize(long size) {
        this.size = size;
        return this;
    }

    public long getCurrent() {
        return this.current;
    }

    public PaginationParam<T> setCurrent(long current) {
        this.current = current;
        return this;
    }
}
