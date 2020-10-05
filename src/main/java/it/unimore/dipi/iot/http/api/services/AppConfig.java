package it.unimore.dipi.iot.http.api.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.unimore.dipi.iot.http.api.persistence.DefaultIotInventoryDataManger;
import it.unimore.dipi.iot.http.api.persistence.IIoInventoryDataManager;

public class AppConfig extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    private IIoInventoryDataManager inventoryDataManager = null;

    public IIoInventoryDataManager getInventoryDataManager(){

        if(this.inventoryDataManager == null)
            this.inventoryDataManager = new DefaultIotInventoryDataManger();

        return this.inventoryDataManager;
    }

}