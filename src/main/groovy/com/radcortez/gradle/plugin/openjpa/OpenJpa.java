package com.radcortez.gradle.plugin.openjpa;

import org.apache.openjpa.enhance.PCEnhancer;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfigurationImpl;
import org.apache.openjpa.jdbc.meta.MappingTool;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.util.Options;

import java.io.IOException;

/**
 * Description.
 *
 * @author Roberto Cortez
 */
public class OpenJpa {
    private Options options;
    private JDBCConfiguration jdbcConfiguration;

    private OpenJpa(final Options options) {
        this.options = options;
        this.jdbcConfiguration = new JDBCConfigurationImpl();
        Configurations.populateConfiguration(this.jdbcConfiguration, this.options);
    }

    public static OpenJpa openJpa() {
        return new OpenJpa(new Options());
    }

    public static OpenJpa openJpa(final Options options) {
        return new OpenJpa(options);
    }

    public void enhance(final String[] entities) throws Exception {
        PCEnhancer.run(entities, options);
    }

    public Class<?>[] parseTypes(final String arg) {
        return jdbcConfiguration.newMetaDataRepositoryInstance().getMetaDataFactory().newClassArgParser()
                                .parseTypes(arg);
    }

    public boolean mappingTool(final String[] entities) throws Exception {
        return MappingTool.run(jdbcConfiguration, entities, options);
    }
}
