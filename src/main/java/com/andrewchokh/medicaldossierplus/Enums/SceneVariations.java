package com.andrewchokh.medicaldossierplus.Enums;

public enum SceneVariations {
    HOME("Home"),
    DIARY("Diary"),
    RECORDS("Records");

    private String name;
    SceneVariations(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
