package cse.java2.project.entity;

import lombok.Data;

/**
 * Entity for range.
 */
@Data
public class Range {
    String name;
    String value;

    public Range(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
