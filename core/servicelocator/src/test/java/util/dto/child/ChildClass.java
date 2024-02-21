package util.dto.child;


import util.dto.AnotherGenericInterface;
import util.dto.NotGenericInterface;
import util.dto.parent.ParentClass;

public class ChildClass<CHILD_CLASS_GENERIC_TYPE> extends ParentClass<Integer> implements ChildInterface<Long,CHILD_CLASS_GENERIC_TYPE>, NotGenericInterface, AnotherGenericInterface<Byte> {
}
