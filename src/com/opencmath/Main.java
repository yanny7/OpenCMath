package com.opencmath;

public class Main {
    public static void main(String[] args) {
        Package pkg = Main.class.getPackage();
        String name = pkg.getSpecificationTitle();
        String version = pkg.getSpecificationVersion();

        System.out.println("Package name: " + name);
        System.out.println("Package version: " + version);
    }
}
