package org.fintech2024.logsandmetrics.errorsexample;

import java.util.ArrayList;
import java.util.List;

public class OutOfMemoryExample {
    public static void main(String[] args) {
        List<int[]> memoryLeak = new ArrayList<>();

        while (true) {
            memoryLeak.add(new int[1_000_000]);
        }
    }
}
