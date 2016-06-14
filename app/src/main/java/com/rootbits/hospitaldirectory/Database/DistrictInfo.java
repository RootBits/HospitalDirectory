package com.rootbits.hospitaldirectory.Database;

/**
 * Created by RootBits on 6/7/2016.
 */
public class DistrictInfo {
    String id,name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DistrictInfo(String id, String name) {

        this.id = id;
        this.name = name;
    }
}
