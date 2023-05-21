package cse.java2.project.entity;

import lombok.Data;

/**
 * Entity for user.
 */
@Data
public class Users {
    String name;
    int num;

    public Users(String name, int num) {
        this.name = name;
        this.num = num;
    }
}
