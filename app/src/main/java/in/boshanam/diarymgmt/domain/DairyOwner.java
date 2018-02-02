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
    private List<String> dairyIds;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
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

    public List<String> getDairyIds() {
        return dairyIds;
    }

    public void setDairyIds(List<String> dairyIds) {
        this.dairyIds = dairyIds;
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
                ", dairyIds=" + dairyIds +
                '}';
    }
}
