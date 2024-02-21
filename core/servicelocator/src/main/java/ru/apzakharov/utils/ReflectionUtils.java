package ru.apzakharov.utils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

public class ReflectionUtils {

    /**
     * Для некоторого класса определяет каким классом был параметризован один из его предков с generic-параметрами.
     *
     * @param actualClass    анализируемый класс
     * @param genericClass   класс, для которого определяется значение параметра
     * @param parameterIndex номер параметра
     * @return класс, являющийся параметром с индексом parameterIndex в genericClass
     */
    public static Class getGenericParameterClass(final Class actualClass, final Class genericClass, final int parameterIndex) {
        // Нам нужно найти класс, для которого непосредственным родителем будет genericClass.
        // Мы будем подниматься по иерархии, пока не найдем интересующий нас класс.
        // В процессе поднятия мы будем сохранять в genericClassesStack все параметризированные классы
        // они нам понадобятся при спуске вниз.

        final LinkedList<ParameterizedType> genericClassesStack = getGenericParameterStack(genericClass, actualClass);

        final Type resultType = getResult(parameterIndex, genericClassesStack, actualClass, genericClass);

        final Class resultClass = (Class) getRawType(resultType);

        return resultClass;
    }
    private static Type getResult(int parameterIndex, LinkedList<ParameterizedType> genericClassesStack, Class actualClass, Class genericClass) {
        if (genericClassesStack.isEmpty()) {
            // Мы спустились до самого низа, Но не нашли среди родителей actualClass (классов и интерфейсов) указанный GenericClass
            throw new IllegalArgumentException("actual class " + actualClass.getSimpleName() +
                    " does not inherit or implement the specified type " + genericClass.getSimpleName() +
                    " or specified type actually is not parameterized");
        }

        // Нужный класс найден. Теперь мы можем узнать, какими типами он параметризован.
        Type result = genericClassesStack.pop().getActualTypeArguments()[parameterIndex];
        // Анализируем тип значения переменной, в которой должен быть
        while (result instanceof TypeVariable && !genericClassesStack.isEmpty()) {
            // Похоже наш параметр задан где-то ниже по иерархии, спускаемся вниз.
            // Получаем индекс параметра в том классе, в котором он задан.
            int actualArgumentIndex = getParameterTypeDeclarationIndex((TypeVariable) result);
            // Берем соответствующий класс, содержащий метаинформацию о нашем параметре.
            ParameterizedType type = genericClassesStack.pop();
            // Получаем информацию о значении параметра.
            result = type.getActualTypeArguments()[actualArgumentIndex];
        }

        if (result instanceof TypeVariable) {
            // Мы спустились до самого низа, но даже там нужный параметр не имеет явного задания.
            // Следовательно из-за "Type erasure" узнать класс для параметра невозможно.
            throw new IllegalStateException("Unable to resolve type variable " + result + "."
                    + " Try to replace instances of parametrized class with its non-parameterized subtype.");
        }

        if (result instanceof ParameterizedType) {
            // Сам параметр оказался параметризованным.
            result = ((ParameterizedType) result).getRawType();
        }

        if (result == null) {
            throw new IllegalStateException("Unable to determine actual parameter type for "
                    + actualClass.getName() + ".");
        }

        if (!(result instanceof Class)) {
            // Похоже, что параметр - массив или что-то еще, что не является классом.
            throw new IllegalStateException("Actual parameter type for " + actualClass.getName() + " is not a Class.");
        }
        return result;
    }

    private static LinkedList<ParameterizedType> getGenericParameterStack(Class genericRawType, Class clazz) {
        if (!genericRawType.isInterface()) {
            if (!genericRawType.isAssignableFrom(clazz.getSuperclass())) {
                throw new IllegalArgumentException("Class " + genericRawType.getName() + " is not a superclass of "
                        + clazz.getName() + ".");
            }
        }

        LinkedList<ParameterizedType> parametrizedTypeStack = new LinkedList<>();

        while (true) {
            LinkedList<ParameterizedType> parametrizedInterfaceStack =
                    getParametrizedInterfaceStack(parametrizedTypeStack, genericRawType, clazz);
            if (!parametrizedInterfaceStack.isEmpty()) {
                return parametrizedInterfaceStack;
            }
            if (genericRawType.equals(clazz.getSuperclass())) {
                pushType(parametrizedTypeStack, clazz.getGenericSuperclass());
            } else {
                pushType(parametrizedTypeStack, clazz);
            }

            if (!checkStopIteration(genericRawType, clazz)) {
                clazz = clazz.getSuperclass();
            } else {
                break;
            }
        }
        return parametrizedTypeStack;
    }

    private static boolean checkStopIteration(Class genericType, Class clazz) {
        Class rawType = (Class) getRawType(clazz);
        Class rawGenericType = (Class) getRawType(genericType);
        boolean isNoInterfaces = clazz.getInterfaces().length == 0;
        //Если который мы ищем искомый класс интерфейс, но у текущего интерфейса нет
        //То значит мы в рекурсии искали интерфейс и больше некуда
        boolean isInterface = genericType.isInterface();
        if (isInterface && isNoInterfaces) {
            return true;
        }
        //В любом случае(итеративно и рекурсивно) при обнаружении необходимого типа
        //стоит прекратить поиск
        if (rawGenericType.equals(rawType.getSuperclass())) {
            return true;
        }
        //Если искомый класс не интерфейс(или уже бы вышли из метода)
        //то осталось одно место, куда можно двигаться - вверх классам
        //а если класса нет или родитель Object - значит мы уже на самом верху
        if (clazz.getGenericSuperclass() == null || clazz.getGenericSuperclass().equals(Object.class)) {
            return true;
        }
        return false;
    }

    private static LinkedList<ParameterizedType> getParametrizedInterfaceStack(LinkedList<ParameterizedType> genericClassesStack,
                                                                               Type genericInterface, Type clazz) {
        final Type classType = pushType(genericClassesStack, clazz);

        Class rawType = (Class) getRawType(classType);

        Class rawGenericType = (Class) getRawType(genericInterface);


        final Type[] genericInterfaces = rawType.getGenericInterfaces();
        // Проверяем, дошли мы до нужного предка или нет - конец рекурсии
        if (rawGenericType.equals(rawType)) {
            return genericClassesStack;

            //При анализе в контексте обычных задач глубины в 50 достаточно, если больше - что то неладное
            //С другой стороны, 50 вызовов в рекурсии - не очень много, не будет переполения
        } else if (genericClassesStack.size() >= 50) {
            throw new RuntimeException("Recursion limit exceeded");

            //Больше никаких интерфейсов, но нужного не достигли
            //Ни один из найденных в цепочке интерфейсов класса нам не нужен - выходим из рекурсии поищем в другом классе
        } else if (genericInterfaces.length == 0) {
            genericClassesStack.clear();
            return genericClassesStack;
        } else {
            //Идем на уровень выше
            return Arrays.stream(genericInterfaces)
                    .map(parentInterface -> getParametrizedInterfaceStack(new LinkedList<>(genericClassesStack), genericInterface, parentInterface))
                    .filter(stack -> genericInterface.equals(Optional.ofNullable(stack.peek()).map(ParameterizedType::getRawType).orElse(null)))
                    .findFirst()
                    // В этой ветке интерфейсов значения нет, поищем в следующей (если есть)
                    .orElse(new LinkedList<>());
        }
    }

    private static Type getRawType(Type classType) {
        return isParameterizedType(classType) ? ((ParameterizedType) classType).getRawType() : classType;
    }

    private static Type pushType(LinkedList<ParameterizedType> genericClassesStack, Type genericSuperclass) {

        if (isParameterizedType(genericSuperclass)) {
            // Если предок - параметризованный класс, то запоминаем его - возможно он пригодится при спуске вниз.
            genericClassesStack.push((ParameterizedType) genericSuperclass);
        } else {
            // В иерархии встретился непараметризованный класс. Все ранее сохраненные параметризованные классы будут бесполезны.
            genericClassesStack.clear();
        }

        return genericSuperclass;
    }

    private static boolean isParameterizedType(Type genericSuperclass) {
        return genericSuperclass instanceof ParameterizedType;
    }

    public static int getParameterTypeDeclarationIndex(final TypeVariable typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();

        // Ищем наш параметр среди всех параметров того класса, где определен нужный нам параметр.
        TypeVariable[] typeVariables = genericDeclaration.getTypeParameters();
        Integer actualArgumentIndex = null;
        for (int i = 0; i < typeVariables.length; i++) {
            if (typeVariables[i].equals(typeVariable)) {
                actualArgumentIndex = i;
                break;
            }
        }
        if (actualArgumentIndex != null) {
            return actualArgumentIndex;
        } else {
            throw new IllegalStateException("Argument " + typeVariable.toString() + " is not found in "
                    + genericDeclaration.toString() + ".");
        }
    }

}