package io.ituknown.mysql.template.model;

import java.util.ArrayList;
import java.util.List;

public class SysAdministrativeRegionExample {

    private static final String tableName = "sys_administrative_region";

    private final List<String> fieldList = new ArrayList<>();

    public SysAdministrativeRegionExample allFields() {
        return id().name().code().parents().country().level().latitude().longitude().status().deleted().addTime().updateTime().deleted();
    }

    public SysAdministrativeRegionExample id() {
        fieldList.add("id id");
        return this;
    }

    public SysAdministrativeRegionExample name() {
        fieldList.add("name name");
        return this;
    }

    public SysAdministrativeRegionExample code() {
        fieldList.add("code code");
        return this;
    }

    public SysAdministrativeRegionExample parents() {
        fieldList.add("parents parents");
        return this;
    }

    public SysAdministrativeRegionExample country() {
        fieldList.add("country country");
        return this;
    }

    public SysAdministrativeRegionExample level() {
        fieldList.add("level level");
        return this;
    }

    public SysAdministrativeRegionExample longitude() {
        fieldList.add("longitude longitude");
        return this;
    }

    public SysAdministrativeRegionExample latitude() {
        fieldList.add("latitude latitude");
        return this;
    }

    public SysAdministrativeRegionExample status() {
        fieldList.add("status status");
        return this;
    }

    public SysAdministrativeRegionExample deleted() {
        fieldList.add("deleted deleted");
        return this;
    }

    public SysAdministrativeRegionExample addTime() {
        fieldList.add("add_time addTime");
        return this;
    }

    public SysAdministrativeRegionExample updateTime() {
        fieldList.add("update_time updateTime");
        return this;
    }

    public SysAdministrativeRegionExample description() {
        fieldList.add("description description");
        return this;
    }

    public String fields() {
        return String.join(", ", fieldList);
    }
}
