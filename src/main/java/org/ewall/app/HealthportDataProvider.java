package org.ewall.app;

import java.util.Collection;
import java.util.List;

import org.ewall.app.IDataProvider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.client.BaseClient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.EncodingEnum;

/**
 * An implementation for the FHIR backend API.
 */
public class HealthportDataProvider implements IDataProvider {
	
	private static final String serverBase = "https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/";
	private FhirContext ctx;
	private IGenericClient client;

    /**
     * Initialize the data provider.
     */
    public HealthportDataProvider() {
    	ctx = new FhirContext();
    	client = ctx.newRestfulGenericClient(serverBase);
    	((BaseClient)client).setEncoding(EncodingEnum.JSON); //prefer JSON encoding
    }
	
	public Patient getPatientById(String id) {
		IdDt ptid = new IdDt(id);
        return client.read(Patient.class, ptid);
	}

	public Collection<Patient> getAllPatients() {
        Bundle response = client
                .search()
                .forResource(Patient.class)
                .execute();
        List<Patient> patients = response.getResources(Patient.class);
        while (!response.getLinkNext().isEmpty()) {
       	   // load next page
       	   response = client.loadPage().next(response).execute();
       	   patients.addAll(response.getResources(Patient.class));
       	}
        return patients;
	}

	public Condition getConditionById(String id) {
		IdDt condid = new IdDt(id);
        return client.read(Condition.class, condid);
	}

	public Collection<Condition> getAllConditionsForPatient(String id) {
	    Bundle response = client
	    		.search()
	    		.forResource(Condition.class)
	    		.where(Condition.SUBJECT.hasId(id))
	    		.execute();
	    List<Condition> conditions = response.getResources(Condition.class);
	    while (!response.getLinkNext().isEmpty()) {
    	   // load next page
    	   response = client.loadPage().next(response).execute();
    	   conditions.addAll(response.getResources(Condition.class));
    	}
	    return conditions;
	}

	public Observation getObservationById(String id) {
		IdDt obsid = new IdDt(id);
        return client.read(Observation.class, obsid);
	}

	public Collection<Observation> getAllObservationsForPatient(String id) {
	    Bundle response = client
	    		.search()
	    		.forResource(Observation.class)
	    		.where(Observation.SUBJECT.hasId(id))
	    		.execute();
	    List<Observation> observations = response.getResources(Observation.class);
    	while (!response.getLinkNext().isEmpty()) {
    	   // load next page
    	   response = client.loadPage().next(response).execute();
    	   observations.addAll(response.getResources(Observation.class));
    	}
    	return observations;
	}

	public MedicationPrescription getPrescriptionById(String id) {
		IdDt rxid = new IdDt(id);
        return client.read(MedicationPrescription.class, rxid);
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