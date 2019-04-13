package com.algorithm.probabillity;

import java.util.HashMap;
import java.util.Map;

public class Match03Info {
    public int continuous = 0;
    public int count = 0;
    public Map<Integer, Integer> interMap = new HashMap<>();

    @Override
    public String toString() {
        return "Match02Info{" +
                "continuous=" + continuous +
                ", count=" + count +
                ", interMap=" + interMap +
                '}';
    }
}
