package in.boshanam.diarymgmt.domain;

import java.util.Date;

/**
 * Created by Siva on 2/3/2018.
 */
public class Farmer {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String milkType;
    private Date updateTime;
    private String dairyAllocatedId;
    private String dairyUid;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMilkType() {
        return milkType;
    }

    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDairyAllocatedId() {
        return dairyAllocatedId;
    }

    public void setDairyAllocatedId(String dairyAllocatedId) {
        this.dairyAllocatedId = dairyAllocatedId;
    }

    public String getDairyUid() {
        return dairyUid;
    }

    public void setDairyUid(String dairyUid) {
        this.dairyUid = dairyUid;
    }
}
