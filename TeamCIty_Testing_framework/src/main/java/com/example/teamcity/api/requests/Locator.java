package com.example.teamcity.api.requests;

public final class Locator {
    private final String value;

    private Locator(String value) {
        this.value = value;
    }

    public static Locator id(String id) {
        return new Locator("id:" + id);
    }

    public static Locator name(String name) {
        return new Locator("name:" + name);
    }

    @Override
    public String toString() {
        return value;
    }
}
