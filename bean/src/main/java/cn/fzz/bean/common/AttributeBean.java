package cn.fzz.bean.common;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
public class AttributeBean {
    private String type;
    private String name;
    private Object value;

    public AttributeBean() {
    }

    public AttributeBean(String type, String name, Object value) {

        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
