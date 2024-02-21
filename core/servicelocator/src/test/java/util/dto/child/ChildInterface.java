package util.dto.child;

import util.dto.parent.ParentInterface;

public interface ChildInterface<CHILD_INTERFACE_GENERIC_TYPE1,CHILD_INTERFACE_GENERIC_TYPE2>
        extends ParentInterface<String, CHILD_INTERFACE_GENERIC_TYPE1> {
}
