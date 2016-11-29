
package com.gaiya.easybuy.constant;

/**
 * Created by dengt on 15-10-8.
 */
public enum Type {
    SUPPLIER("SUPPLIER", "供应商"), AGENT("AGENT", "代理商"), BUYER("BUYER", "采购商");

    private String type;
    private String name;

    private Type(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
