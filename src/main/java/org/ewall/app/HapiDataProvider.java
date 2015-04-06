package org.ewall.app;

import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.server.EncodingEnum;

/**
 * Data Provider using UHN's HAPI-FHIR demo server
 * FMI see http://fhirtest.uhn.ca/
 */
public class HapiDataProvider extends AbstractDataProvider {

	public HapiDataProvider() {
		super("http://fhirtest.uhn.ca/baseDstu1", EncodingEnum.XML, "d1132446701");
	}

	public Collection<MedicationPrescription> getAllPrescriptionsForPatient(String id) {
	    //   surprise! the HAPI-FHIR server doesn't like how HAPI-FHIR v0.8 does this search... so we'll do the search by URL
		String searchstr = serverBase + "MedicationPrescription?patient=" + id;
	    UriDt searchurl = new UriDt(searchstr);
	    Bundle response = client.search(searchurl);

	    List<MedicationPrescription> prescriptions = response.getResources(MedicationPrescription.class);
	    while (!response.getLinkNext().isEmpty()) {
    	   // load next page
    	   response = client.loadPage().next(response).execute();
    	   prescriptions.addAll(response.getResources(MedicationPrescription.class));
    	}
	    return prescriptions;
	}

}
