import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import data.*;
import java.util.*;

class MainTest extends Main {
    private static SimpleData simpleData1;
    private static SimpleData simpleData2;

    private static WithArrayData arrayData1;

    @BeforeAll
    static void initialFieldsBeforeAllTests() {
        simpleData1 = new SimpleData(1, (byte) 1, "ObjectInSimpleData1", "StringInSimpleData1");
        simpleData2 = new SimpleData(2, (byte) 2, simpleData1, "StringInSimpleData2");

        String[] strArray = {"strInArr1", "strInArr2", "strInArr3"};
        arrayData1 = new WithArrayData(11, (byte) 11, simpleData2, "StringInArrayData1", strArray);
    }

    @Test
    void getFieldsNamesTest() {
        String expectedString = """
                ----[ data.WithCollectionData ]-----
                listString as java.util.List
                mapStringSimpleData as java.util.Map
                -------[ data.WithArrayData ]-------
                stringArray as java.lang.String[]
                --------[ data.SimpleData ]---------
                intData as int
                byteData as byte
                objectData as java.lang.Object
                stringData as java.lang.String
                """;

        String actualString = Main.getFieldsNames(new WithCollectionData());

        assertEquals(expectedString, actualString);
    }

    @Test
    void getMethodsNamesTest() {
        String expectedString = """
                ----[ data.WithCollectionData ]-----
                setMapStringSimpleData return void
                getMapStringSimpleData return java.util.Map
                getListString return java.util.List
                setListString return void
                -------[ data.WithArrayData ]-------
                getStringArray return java.lang.String[]
                setStringArray return void
                --------[ data.SimpleData ]---------
                setObjectData return void
                setIntData return void
                getByteData return byte
                getObjectData return java.lang.Object
                getIntData return int
                setByteData return void
                """;
        List<String> expectedList = new ArrayList<>(List.of(expectedString.split("\n")));

        String actualString = Main.getMethodsNames(new WithCollectionData());
        List<String> actualList = new ArrayList<>(List.of(actualString.split("\n")));

        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    void getFieldsValuesWithArrayDataTest() {
        String expectedString = """
                :data.WithArrayData =\s
                {
                	intData:int = 11;
                	byteData:byte = 11;
                	objectData:data.SimpleData =\s
                	{
                		intData:int = 2;
                		byteData:byte = 2;
                		objectData:data.SimpleData =\s
                		{
                			intData:int = 1;
                			byteData:byte = 1;
                			objectData:java.lang.Object = ObjectInSimpleData1;
                			stringData:java.lang.String = StringInSimpleData1;
                		};
                		stringData:java.lang.String = StringInSimpleData2;
                	};
                	stringData:java.lang.String = StringInArrayData1;
                	stringArray:java.lang.String[] =\s
                	strInArr1,
                	strInArr2,
                	strInArr3;
                };
                """;

        String actualString = Main.getFieldsValues(arrayData1, 0);

        assertEquals(expectedString, actualString);
    }

    @Test
    void copySimpleDataTest() {
        SimpleData copySimpleData = new SimpleData();

        copy(simpleData2, copySimpleData);

        assertEquals(simpleData2, copySimpleData);
        assertNotSame(simpleData2.getObjectData(), copySimpleData.getObjectData());
    }

    @Test
    void copyWithArrayDataTest() {
        WithArrayData copyArrayData = new WithArrayData();

        copy(arrayData1, copyArrayData);

        assertArrayEquals(arrayData1.getStringArray(), copyArrayData.getStringArray());
        assertNotSame(arrayData1.getStringArray(), copyArrayData.getStringArray());
    }
}