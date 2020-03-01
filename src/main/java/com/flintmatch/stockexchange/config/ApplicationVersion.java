package com.flintmatch.stockexchange.config;

public class ApplicationVersion {

    public static String majorVersion = "1";
    public static String minorVersion = "0";
    public static String patchVersion = "0";
    public static String releaseName = "Aristotle";

    public static String getFullVersion() {
        return majorVersion + "." + minorVersion + (patchVersion != "0" ? "." + patchVersion : "") +
                " (" + releaseName + ")";
    }

    public static String getNumberedVersion() {
        return majorVersion + "." + minorVersion + "." + patchVersion;
    }

}
