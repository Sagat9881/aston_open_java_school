package util;

import org.junit.jupiter.api.Test;
import ru.apzakharov.utils.ReflectionUtils;
import util.dto.AnotherGenericInterface;
import util.dto.NoParentClass;
import util.dto.NotGenericInterface;
import util.dto.child.ChildClass;
import util.dto.child.ChildInterface;
import util.dto.parent.ParentClass;
import util.dto.parent.ParentClassInterface;
import util.dto.parent.ParentInterface;

import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionUtilsTest {

    @Test
    void getGenericParameterClass() {
        ChildClass<Integer> cInstance = new ChildClass();
        Class genericParameterChildInterface =  ReflectionUtils.getGenericParameterClass(cInstance.getClass(), ChildInterface.class, 0);
        Class genericParameterParentClass =
                ReflectionUtils.getGenericParameterClass(cInstance.getClass(), ParentClass.class, 0);
        Class genericParameterParentInterface =
                ReflectionUtils.getGenericParameterClass(cInstance.getClass(), ParentInterface.class, 1);
        Class genericParameterParentClassInterface =
                ReflectionUtils.getGenericParameterClass(cInstance.getClass(), ParentClassInterface.class, 1);
        Class anotherGenericParameterInterface =
                ReflectionUtils.getGenericParameterClass(cInstance.getClass(), AnotherGenericInterface.class, 0);

        assertEquals(anotherGenericParameterInterface,Byte.class);
        assertEquals(genericParameterChildInterface,Long.class);
        assertEquals(genericParameterParentInterface,Long.class);
        assertEquals(genericParameterParentClass,Integer.class);
        assertEquals(genericParameterParentClassInterface, Short.class);

        assertThrows(IllegalArgumentException.class,()->ReflectionUtils.getGenericParameterClass(cInstance.getClass(), NoParentClass.class,0));
        assertThrows(IllegalArgumentException.class,()->ReflectionUtils.getGenericParameterClass(cInstance.getClass(), NotGenericInterface.class,0));
        assertThrows(IllegalStateException.class,()-> ReflectionUtils.getGenericParameterClass(cInstance.getClass(), ChildInterface.class, 1));
        assertThrows(IllegalStateException.class,()->  ReflectionUtils.getGenericParameterClass(cInstance.getClass(), ParentClassInterface.class, 0));

    }

    @Test
    void getParameterTypeDeclarationIndex() {
    }

    private static Type getGenericInterfaceType(Class<?> cClass) {
        return Arrays.stream(cClass.getGenericInterfaces())
                .findFirst().orElse(null);
    }

    private static Class<?> getInterface(Class<?> cClass) {
        return Arrays.stream(cClass.getInterfaces())
                .findFirst().orElse(null);
    }
}