package org.ewall.app;

import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.server.EncodingEnum;

/**
 * Data Provider using Georgia Tech's HealthPort FHIR server
 * requires VPN connection to GT network
 */
public class HealthportDataProvider extends AbstractDataProvider {

    public HealthportDataProvider() {
    	//super("https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/", EncodingEnum.JSON, "Patient/3.568001602-01");
    	super("http://localhost:8080/HealthPort/fhir/", EncodingEnum.JSON, "Patient/3.568001602-01");
    }

	public Collection<MedicationPrescription> getAllPrescriptionsForPatient(String id) {
	    //   surprise! HealthPort doesn't support PATIENT search parameter, need to use SUBJECT instead... so we'll do the search by URL
		String searchstr = serverBase + "MedicationPrescription?subject:Patient=" + id;
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