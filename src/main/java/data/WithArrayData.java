package data;

public class WithArrayData extends SimpleData {
    private String[] stringArray;

    public WithArrayData() { }

    public WithArrayData(int intData, byte byteData, Object objectData, String stringData, String[] stringArray) {
        super(intData, byteData, objectData, stringData);
        this.stringArray = stringArray;
    }

    public String[] getStringArray() {
        return stringArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }
}
