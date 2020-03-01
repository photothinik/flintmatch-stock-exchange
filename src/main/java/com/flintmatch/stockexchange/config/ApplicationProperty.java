package com.flintmatch.stockexchange.config;

public enum ApplicationProperty {

    APPLICATION_CONFIG_SOURCE_FILEPATH {
        public String propertyName() {
            return "app.config.source_filepath";
        }

    },
    APPLICATION_MAIN_LOOP_DELAY {
        public String propertyName() {
            return "app.main.loop-delay-ms";
        }

        @Override
        public String defaultValue() {
            return "5000";
        }
    },
    APPLICATION_MAIN_LOOP_ERROR_DELAY {
        public String propertyName() {
            return "app.main.error-delay-ms";
        }

        @Override
        public String defaultValue() {
            return "3000";
        }
    },
    REPOSITORY_DATASOURCE_DRIVERCLASS {
        public String propertyName() {
            return "app.datasource.driver-classname";
        }

    },
    REPOSITORY_DATASOURCE_URL {
        public String propertyName() {
            return "app.datasource.url";
        }

    },
    REPOSITORY_DATASOURCE_USERNAME {
        public String propertyName() {
            return "app.datasource.username";
        }

    },
    REPOSITORY_DATASOURCE_PASSWORD {
        public String propertyName() {
            return "app.datasource.password";
        }

    };

    public abstract String propertyName();

    public String defaultValue() {return null;}

    public static ApplicationProperty findByName(String name) {

        for(ApplicationProperty p : ApplicationProperty.values()) {

            if( p.propertyName().equalsIgnoreCase(name))
                return p;

        }

        return null;
    }

}
