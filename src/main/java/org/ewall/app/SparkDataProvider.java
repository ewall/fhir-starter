package org.ewall.app;

import ca.uhn.fhir.rest.server.EncodingEnum;

/**
 * Data Provider using Furore's Spark FHIR server
 * FMI see http://spark.furore.com/
 */
public class SparkDataProvider extends AbstractDataProvider {

    public SparkDataProvider() {
    	super("http://spark.furore.com/fhir/", EncodingEnum.JSON, "Patient/f201");
    }

}
