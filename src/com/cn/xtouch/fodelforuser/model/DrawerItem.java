package com.cn.xtouch.fodelforuser.model;

public class DrawerItem {

    public static enum Type {HEADER, MENU, DIVIDER}

    private final Type type;

    public DrawerItem(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}