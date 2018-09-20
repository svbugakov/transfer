package rb.bars.jreport.sql.parametr;

import java.util.List;

public class Parametr {
    private String name;
    private int type;
    private String value;
    private List<DefaultValue> defaultValues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DefaultValue> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(List<DefaultValue> defaultValues) {
        this.defaultValues = defaultValues;
    }
}
