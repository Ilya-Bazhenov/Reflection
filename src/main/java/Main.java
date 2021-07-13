import data.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import java.util.*;

public class Main {
    private static final int outputWidth = 32;

    public static void main(String[] args) {


    }

    protected static void copy(Object originObject, Object copyObject) {
        Class<?> originObjectClass = originObject.getClass();
        Class<?> copyObjectClass = copyObject.getClass();

        if (originObjectClass == copyObjectClass) {
            List<Field> copyObjectFields = getAllObjectFields(copyObject);
            int curIndexForCopyObjectFields = 0;

            List<Field> originObjectFields = getAllObjectFields(originObject);

            for (Field originObjectField: originObjectFields) {
                try {
                    originObjectField.setAccessible(true);

                    Field copyObjectField = copyObjectFields.get(curIndexForCopyObjectFields++);
                    copyObjectField.setAccessible(true);

                    Object value = originObjectField.get(originObject);
                    Class<?> valueClass = (value != null) ? value.getClass() : null;

                    if (valueClass == null || originObjectField.getType().isPrimitive() || valueClass.getName().matches("java\\.lang\\.[A-Z][a-z]*")) {
                        copyObjectField.set(copyObject, value);
                    } else if (originObjectField.getType().isArray()) {
                        Object[] stringArray = (Object[]) originObjectField.get(originObject);
                        copyObjectField.set(copyObject, Arrays.copyOf(stringArray, stringArray.length));
                    } else {
                        copyObjectField.set(copyObject, copyOf(value));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static Object copyOf(Object originObject) {
        Object copyObject = null;

        try {
            Constructor<?> constructor = originObject.getClass().getConstructor();
            copyObject = constructor.newInstance();

            copy(originObject, copyObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return copyObject;
    }

    private static List<Field> getAllObjectFields(Object object) {
        List<Field> curFields = new ArrayList<>();

        Class<?> superclass = object.getClass().getSuperclass();
        if (!(superclass == null || superclass.getName().equals("java.lang.Object"))) {
            try {
                curFields.addAll(getAllObjectFields(superclass.newInstance()));
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        curFields.addAll(List.of(object.getClass().getDeclaredFields()));
        return curFields;
    }

    protected static String getFieldsValues(Object object, int countTab) {
        StringBuilder strBuild = new StringBuilder();

        strBuild.append(String.format(":%s = \n%s{\n", object.getClass().getName(), "\t".repeat(countTab)));

        List<Field> fields = getAllObjectFields(object);
        for (Field field: fields) {
            field.setAccessible(true);

            try {
                String indent = "\t".repeat(countTab + 1);

                Object value = field.get(object);
                Class<?> valueClass = (value != null) ? value.getClass() : null;

                if (valueClass == null || field.getType().isPrimitive() || valueClass.getName().equals("java.lang.String")) {
                    strBuild.append(String.format("%s%s:%s = %s;\n", indent, field.getName(), field.getType().getCanonicalName(), value));
                } else if (field.getType().isArray()) {
                    strBuild.append(String.format("%s%s:%s = \n", indent, field.getName(), field.getType().getCanonicalName()));

                    Object[] stringArray = (Object[]) field.get(object);
                    if (stringArray.length != 0) {
                        for (int i = 0; i < stringArray.length - 1; i++){
                            strBuild.append(String.format("%s%s,\n", indent, stringArray[i]));
                        }
                        strBuild.append(String.format("%s%s;\n", indent, stringArray[stringArray.length - 1]));
                    }
                } else {
                    strBuild.append(String.format("%s%s%s", indent, field.getName(), getFieldsValues(value, countTab + 1)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        strBuild.append(String.format("%s};\n", "\t".repeat(countTab)));

        return strBuild.toString();
    }

    private static void printFieldsValues(Object object) {
        System.out.print(getFieldsValues(object, 0));
    }

    private static String getFormatClassName(String className) {
        StringBuilder formatBefore = new StringBuilder();
        int countBeforeClassName = (outputWidth - className.length()) / 2;
        for (int i = 0; i < countBeforeClassName; i++)
            formatBefore.append('-');
        String formatAfter = formatBefore.toString() + ((outputWidth - className.length()) % 2 == 0 ? "" : "-");

        return String.format("%s[ %s ]%s\n", formatBefore.toString(), className, formatAfter);
    }

    protected static String getFieldsNames(Object object) {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(getFormatClassName(object.getClass().getName()));

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field: fields)
            strBuild.append(String.format("%s as %s\n", field.getName(), field.getType().getCanonicalName()));

        Class<?> superclass = object.getClass().getSuperclass();
        if (!(superclass == null || superclass.getName().equals("java.lang.Object"))) {
            try {
                strBuild.append(getFieldsNames(superclass.newInstance()));
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        return strBuild.toString();
    }

    private static void printFieldsNames(Object object) {
        System.out.print(getFieldsNames(object));
    }

    protected static String getMethodsNames(Object object) {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(getFormatClassName(object.getClass().getName()));

        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method: methods)
            strBuild.append(String.format("%s return %s\n", method.getName(), method.getReturnType().getCanonicalName()));

        Class<?> superclass = object.getClass().getSuperclass();
        if (!(superclass == null || superclass.getName().equals("java.lang.Object"))) {
            try {
                strBuild.append(getMethodsNames(superclass.newInstance()));
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        return strBuild.toString();
    }

    private static void printMethodsNames(Object object) {
        System.out.print(getMethodsNames(object));
    }
}
