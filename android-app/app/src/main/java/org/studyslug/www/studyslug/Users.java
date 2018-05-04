package org.studyslug.just_findpeople;
import java.util.ArrayList;

public class Users {

    public String name, status;
    private ArrayList<String> classes;
    private ArrayList<String> groups;

    public Users(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users(String name, String image, String status) {
        this.name = name;
        this.status = status;
    }
}
