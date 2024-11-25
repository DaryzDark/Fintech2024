package org.fintech2024.logsandmetrics.errorsexample;

public class StackOverflowExample {
    public static void main(String[] args) {
        recursiveMethod();
    }

    public static void recursiveMethod() {
        recursiveMethod();
    }
}
