package org.ewall.app;

import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Immunization;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.BaseClient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.EncodingEnum;

public abstract class AbstractDataProvider implements IDataProvider {
	protected String samplePtId;
	protected String serverBase;
	protected FhirContext ctx;
	protected IGenericClient client;
	
	public AbstractDataProvider(String url) {
		url = url.charAt(url.length()-1) != '/' ? url += "/" : url; // be sure to end with '/'
    	this.serverBase = url;
    	this.ctx = new FhirContext();
    	this.client = ctx.newRestfulGenericClient(serverBase);
    }

	public AbstractDataProvider(String url, EncodingEnum theEncoding, String ptid) {
    	this(url);
    	this.setSamplePtId(ptid);
    	this.setEncoding(theEncoding);
    }
	
	public String getServerBaseURL() {
		return serverBase;
	}

	public void setServerBaseUrl(String url) {
		url = url.charAt(url.length()-1) != '/' ? url += "/" : url; // be sure to end with '/'
		this.serverBase = url;
	}

	public void setEncoding(EncodingEnum theEncoding) {
		((BaseClient)client).setEncoding(theEncoding);
	}
	
	public String getSamplePtId() {
		return samplePtId;
	}

	public void setSamplePtId(String ptid) {
		this.samplePtId = ptid;
	}

	public Patient getPatientById(String id) {
		IdDt ptid = new IdDt(id);
        return client.read(Patient.class, ptid);
	}

	public Collection<Patient> getXPatients(int x) {
        Bundle response = client
                .search()
                .forResource(Patient.class)
                .limitTo(x)
                .execute();
        List<Patient> patients = response.getResources(Patient.class);
        return patients;
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
	    Bundle response = client
	    		.search()
	    		.forResource(MedicationPrescription.class)
	    		.where(Observation.SUBJECT.hasId(id))
	    		.execute();
		
	    List<MedicationPrescription> prescriptions = response.getResources(MedicationPrescription.class);
	    while (!response.getLinkNext().isEmpty()) {
    	   // load next page
    	   response = client.loadPage().next(response).execute();
    	   prescriptions.addAll(response.getResources(MedicationPrescription.class));
    	}
	    return prescriptions;
	}

	public Immunization getImmunizationById(String id) {
		IdDt rxid = new IdDt(id);
        return client.read(Immunization.class, rxid);		
	}
	
	public Collection<Immunization> getAllImmunizationsForPatient(String id) {
	    Bundle response = client
	    		.search()
	    		.forResource(Immunization.class)
	    		.where(Immunization.SUBJECT.hasId(id))
	    		.execute();
		
	    List<Immunization> immunizations = response.getResources(Immunization.class);
	    while (!response.getLinkNext().isEmpty()) {
    	   // load next page
    	   response = client.loadPage().next(response).execute();
    	   immunizations.addAll(response.getResources(Immunization.class));
    	}
	    return immunizations;
	}
}
