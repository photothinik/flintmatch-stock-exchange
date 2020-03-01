package com.flintmatch.stockexchange.config;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationConfiguration {

    private Map<String,String> properties = new HashMap<String, String>();

    public ApplicationConfiguration(File sourceFile) {
        setConfigurationSourceFile(sourceFile);
    }

    public void loadFromSource() throws IOException {

        File sourceFile = getConfigurationSourceFile();

        if( sourceFile.exists() && sourceFile.isFile()) {

            // Read file contents
            List<String> lines = FileUtils.readLines(getConfigurationSourceFile(), (Charset) null);

            // Clear current properties map
            this.properties.clear();

            // Set source file
            setConfigurationSourceFile(sourceFile);

            // Read lines and populate values
            for(String line : lines) {

                // Ignore comment lines
                if( line.trim().startsWith("#"))
                    continue;

                // Ignore lines that do not contain '='
                if( line.indexOf("=") == -1)
                    continue;

                // Parse property name and value
                String name = line.split("=")[0].trim();
                String value = null;

                if( line.split("=").length >= 2 )
                    value = line.split("=")[1].trim();

                // Find the ServiceProperty
                ApplicationProperty sp = ApplicationProperty.findByName(name);

                if( sp != null ) {
                    // Populate config object
                    this.properties.put(sp.propertyName(), value);
                }


            }

        }
        else
            throw new ApplicationConfigurationException(
                    "Configuration file '" + getConfigurationSourceFile().getAbsolutePath() +
                            "' does not exist or is not a regular file"
            );


    }

    private File getConfigurationSourceFile() {
        return new File(getProperty(ApplicationProperty.APPLICATION_CONFIG_SOURCE_FILEPATH));
    }

    private void setConfigurationSourceFile(File f) {
        this.properties.put(ApplicationProperty.APPLICATION_CONFIG_SOURCE_FILEPATH.propertyName(), f.getAbsolutePath());
    }

    public String getProperty(ApplicationProperty p) {
        if( this.properties.containsKey(p.propertyName()))
            return this.properties.get(p.propertyName());
        else if( p.defaultValue() != null)
            return p.defaultValue();
        else
            throw new ApplicationConfigurationException("Configuration property not found: " + p.propertyName());
    }
}
