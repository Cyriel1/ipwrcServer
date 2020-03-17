package nl.ipwrcServer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WebshopConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private CrossOriginFilterFactory cors = new CrossOriginFilterFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("cors")
    public void setCors(CrossOriginFilterFactory cors) {
        this.cors = cors;
    }

    @JsonProperty("cors")
    public CrossOriginFilterFactory getCors() {
        return cors;
    }

}
