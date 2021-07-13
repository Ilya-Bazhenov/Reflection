package data;

public class SimpleData {
    private int intData;
    private byte byteData;

    private Object objectData;
    public String stringData;

    public SimpleData() { }

    public SimpleData(int intData, byte byteData, Object objectData, String stringData) {
        this.intData = intData;
        this.byteData = byteData;
        this.objectData = objectData;
        this.stringData = stringData;
    }

    public int getIntData() {
        return intData;
    }

    public void setIntData(int intData) {
        this.intData = intData;
    }

    public byte getByteData() {
        return byteData;
    }

    public void setByteData(byte byteData) {
        this.byteData = byteData;
    }

    public Object getObjectData() {
        return objectData;
    }

    public void setObjectData(Object objectData) {
        this.objectData = objectData;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SimpleData other = (SimpleData) object;
        return this.intData == other.getIntData() && this.byteData == other.getByteData() &&
               this.stringData == other.stringData && this.objectData.equals(other.getObjectData());
    }
}
