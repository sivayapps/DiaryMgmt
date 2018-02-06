package in.boshanam.diarymgmt.domain;

import java.util.List;

/**
 * Created by Siva on 2/2/2018.
 */
public class DairyOwner {
    private String uid;
    private String name;
    private String phone;
    private String email;
    private String dairyId;

    //TODO dairy registration should have another UI and remove dairy name from this domain
    // as DairyOwner can have multiple dairies owned by him.
    private String dairyName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getDairyId() {
        return dairyId;
    }

    public void setDairyId(String dairyId) {
        this.dairyId = dairyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDairyName() {
        return dairyName;
    }

    public void setDairyName(String dairyName) {
        this.dairyName = dairyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DairyOwner that = (DairyOwner) o;
        return uid != null ? uid.equals(that.uid) : that.uid == null;
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DairyOwner{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", dairyId='" + dairyId + '\'' +
                ", dairyName='" + dairyName + '\'' +
                '}';
    }
}
