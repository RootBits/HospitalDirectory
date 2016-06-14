package com.rootbits.hospitaldirectory.Database;

/**
 * Created by RootBits on 6/7/2016.
 */
public class DivisionInfo {
    String id,name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DivisionInfo(String id, String name) {

        this.id = id;
        this.name = name;
    }
}
