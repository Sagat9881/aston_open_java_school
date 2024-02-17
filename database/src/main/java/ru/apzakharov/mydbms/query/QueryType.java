package ru.apzakharov.mydbms.query;

import lombok.Getter;

@Getter
public enum QueryType {
    SELECT("SELECT"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String typeString;

    QueryType(String typeString) {
        this.typeString = typeString;
    }
}
