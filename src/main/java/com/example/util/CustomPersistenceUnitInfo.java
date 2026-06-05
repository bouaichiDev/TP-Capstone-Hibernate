package com.example.util;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomPersistenceUnitInfo implements PersistenceUnitInfo {
    private final String persistenceUnitName;
    private final List<String> managedClassNames;
    private final Map<String, Object> properties;

    public CustomPersistenceUnitInfo(String name, List<String> classes, Map<String, Object> props) {
        this.persistenceUnitName = name;
        this.managedClassNames = classes;
        this.properties = props;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    public DataSource getJtaDataSource() {
        return null;
    }

    public DataSource getNonJtaDataSource() {
        return null;
    }

    public List<String> getMappingFileNames() {
        return Collections.emptyList();
    }

    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    public List<String> getManagedClassNames() {
        return managedClassNames;
    }

    public boolean excludeUnlistedClasses() {
        return false;
    }

    public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.ALL;
    }

    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    public Properties getProperties() {
        Properties props = new Properties();
        props.putAll(properties);
        return props;
    }

    public String getPersistenceXMLSchemaVersion() {
        return "2.2";
    }

    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public void addTransformer(ClassTransformer transformer) {
    }

    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}
