package com.uid2.shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationVersion {
    private final String appName;
    private final String appVersion;

    private final String buildNumber;

    private final Map<String, String[]> componentVersions;

    public static ApplicationVersion load(String appName, String... componentNames) throws IOException {
        Map<String, String[]> componentVersions = new HashMap<>();
        for (String componentName : componentNames) {
            componentVersions.put(componentName, loadVersion(componentName));
        }

        String[] version = loadVersion(appName);
        return new ApplicationVersion(appName, version[0], version[1], componentVersions);
    }

    public ApplicationVersion(String appName, String[] appVersion) {
        this(appName, appVersion[0], appVersion[1], new HashMap<>());
    }

    public ApplicationVersion(String appName, String appVersion, String buildNumber, Map<String, String[]> componentVersions) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.buildNumber = buildNumber;
        this.componentVersions = Collections.unmodifiableMap(componentVersions);
    }

    public String getAppName() { return appName; }
    public String getAppVersion() { return appVersion; }
    public String getBuildNumber() { return buildNumber; }
    public Map<String, String[]> getComponentVersions() { return componentVersions; }

    private static String[] loadVersion(String componentName) throws IOException {
        InputStream is = ApplicationVersion.class.getClassLoader().getResourceAsStream(componentName + ".properties");
        Properties properties = new Properties();
        properties.load(is);
        return new String[] {properties.getProperty("image.version"), properties.getProperty("project.build.number")};
    }
}