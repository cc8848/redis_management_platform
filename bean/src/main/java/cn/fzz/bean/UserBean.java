package cn.fzz.bean;

import java.util.Date;

/**
 * Created by fanzezhen on 2017/12/13.
 * Desc:
 */
public class UserBean {
    private int id;
    private String username;
    private String password;
    private String email;
    private Date dateJoined;
    private Date dateUpdate;

    public UserBean() {
    }

    public UserBean(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserBean(String username, String password, String email, Date dateJoined, Date dateUpdate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateJoined = dateJoined;
        this.dateUpdate = dateUpdate;
    }

    public UserBean(int id, String username, String password, String email, Date dateJoined, Date dateUpdate) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateJoined = dateJoined;
        this.dateUpdate = dateUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Date getdateUpdate() {
        return dateUpdate;
    }

    public void setdateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
