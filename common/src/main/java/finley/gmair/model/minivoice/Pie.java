package finley.gmair.model.minivoice;

/**
 * 饼图数据
 */
public class Pie {

    String name;

    int value;

    public Pie() {
    }

    public Pie(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
