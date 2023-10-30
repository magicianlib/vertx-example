package io.ituknown.mysql.template.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 行政区划
 *
 * @author Shilin <br > mingrn97@gmail.com
 * @since 2023/09/02 21:29
 */
@DataObject
@RowMapped
public class SysAdministrativeRegion implements Serializable {

    private static final long serialVersionUID = -7160790594024473087L;

    /**
     * 主键
     */
    @Column(name = "id")
    private String id;
    /**
     * 区域名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 区域代码
     */
    @Column(name = "code")
    private Long code;
    /**
     * 父级行政区域
     */
    @Column(name = "parents")
    private String parents;
    /**
     * 国家(cn中国, en外国)
     */
    @Column(name = "country")
    private String country;
    /**
     * 区域级别
     */
    @Column(name = "level")
    private Integer level;
    /**
     * 经度
     */
    @Column(name = "longitude")
    private Double longitude;
    /**
     * 纬度
     */
    @Column(name = "latitude")
    private Double latitude;
    /**
     * 状态 1启用, 2禁用
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 删除状态 1正常, 2已删除
     */
    @Column(name = "deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @Column(name = "add_time")
    private LocalDateTime addTime;
    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    /**
     * 描述说明
     */
    @Column(name = "description")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getAddTime() {
        return addTime;
    }

    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
