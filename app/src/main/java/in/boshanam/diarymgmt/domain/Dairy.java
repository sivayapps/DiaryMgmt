package in.boshanam.diarymgmt.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Siva on 2/3/2018.
 */

public class Dairy {

    private String id;
    private String dairyName;
    private boolean active;
    private String address;
    private Date updateTime;
    private Map<String, Role> roles;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDairyName() {
        return dairyName;
    }

    public void setDairyName(String dairyName) {
        this.dairyName = dairyName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Role> getRoles() {
        if(roles == null) {
            this.roles = new HashMap<>();
        }
        return roles;
    }

    public void setRoles(Map<String, Role> roles) {
        this.roles = roles;
    }
}
