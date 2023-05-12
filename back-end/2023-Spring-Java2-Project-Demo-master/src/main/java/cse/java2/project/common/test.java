package cse.java2.project.common;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        Map<Integer, Integer> testt = new HashMap<>();
        testt.put(1, 1);

        int res = testt.get(2) == null ? 0 : testt.get(2);
        System.out.println(res);
    }
}
