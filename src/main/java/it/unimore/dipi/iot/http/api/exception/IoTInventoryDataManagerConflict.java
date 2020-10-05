package it.unimore.dipi.iot.http.api.exception;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 12:59
 */
public class IoTInventoryDataManagerConflict extends Exception {

    public IoTInventoryDataManagerConflict(String errorMessage){
        super(errorMessage);
    }

}
