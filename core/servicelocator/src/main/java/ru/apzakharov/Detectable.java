package ru.apzakharov;

public interface Detectable<S extends Detectable> {

    default void registerForClass(){
        ServiceLocator.add(this.getClass(),this);
    }
}
