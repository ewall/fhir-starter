package org.ewall.app;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;

/**
 * What the duck, world?
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Let's get this FHIR started!" );

        // warning: slf4j-simple will log messages to stdout
        
        FhirContext ctx = new FhirContext();

        String serverBase = "https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/";
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
         
        // Perform a search
        Bundle patients = client
              .search()
              .forResource(Patient.class)
              .execute();
        System.out.println("Found " + patients.size() + " patients (page limit).\n");
//        Patient patient = patients.getResources(Patient.class).get(0);
//        IdDt id = patient.getId();
        
        // Get specific Patient ID
        String resid = "Patient/3.568001602-01";
        System.out.println("Fetching Patient with RES_ID '" + resid + "'.");
        IdDt id = new IdDt(resid);
        Patient patient = client.read(Patient.class, id);

	    StringDt patientId = patient.getIdentifier().get(0).getValue();
	    System.out.println("Patient Id:  " + patientId.getValue());

	    HumanNameDt name = patient.getName().get(0);
	    System.out.println("First Name:  " + name.getGiven().get(0).getValue());
	    System.out.println("Last Name:   " + name.getFamily().get(0).getValue());

	    System.out.println("Birth Date:  " + patient.getBirthDate().getValueAsString());
	    
	    if (!patient.getGender().getCoding().isEmpty()) {
	    	CodeDt gender = patient.getGender().getCoding().get(0).getCode();
	    	System.out.println("Gender:      " + gender.getValue());
	    }
		
	    System.out.println("Married?:    " + patient.getMaritalStatus().getCodingFirstRep().getCode().getValue());
	    
   	    List<AddressDt> address = patient.getAddress();
	    if (address!=null & !address.isEmpty()) {
		    System.out.println("Address:     " + address.get(0).getLine().get(0));
		    System.out.println("             " + address.get(0).getCity().getValue() + ", "
		    								   + address.get(0).getState().getValue() + " "
		    								   + address.get(0).getZip().getValue());
		    System.out.println("             " + address.get(0).getCountry().getValue());
		}
	    
	    // Get Observations
	    Bundle observations = client
	    		.search()
	    		.forResource(Observation.class)
	    		.where(Observation.SUBJECT.hasId(id))
	    		.execute();
	    if (observations.size() > 0) {
	    	System.out.println("\nFound " + observations.size() + " observations for this patient. Here's the first one:\n");
	    	
	    	Observation obs = observations.getResources(Observation.class).get(0);
	    	
	    	System.out.println("Name:        " + obs.getNameElement().getCodingFirstRep().getDisplayElement().getValue());
	    	System.out.println(" - System:   " + obs.getNameElement().getCodingFirstRep().getSystemElement().getValue());
	    	System.out.println(" - Code:     " + obs.getNameElement().getCodingFirstRep().getCodeElement().getValue());

	    	QuantityDt quan = (QuantityDt) obs.getValue();
	    	System.out.println("Value:       " + quan.getValueElement().getValueAsString());
	    	System.out.println("Units:       " + quan.getUnitsElement().getValueAsString());
	    	
	    	DateTimeDt applies = (DateTimeDt) obs.getAppliesElement();
	    	if (applies!=null) System.out.println("Applies:     " + applies.getValueAsString());
	    	
	    	if (!obs.getPerformerElement().isEmpty()) System.out.println("Performer:   " + obs.getPerformerElement().get(0).getDisplay().getValue());
	    } else {
	    	System.out.println("\nSorry, no observations found for this patient.");
	    }
	    
	    // Get Conditions
	    Bundle conditions = client
	    		.search()
	    		.forResource(Condition.class)
	    		.where(Condition.SUBJECT.hasId(id))
	    		.execute();
	    if (conditions.size() > 0) {
	    	System.out.println("\nFound " + conditions.size() + " conditions for this patient. Here's the first one:\n");
	    	
	    	Condition cond = conditions.getResources(Condition.class).get(0);
	    	
	    	System.out.println("Subject:     " + cond.getSubject().getDisplay().getValue());
	    	
	    	// TODO: explore conditions data
	    	
	    } else {
	    	System.out.println("\nSorry, no conditions found for this patient.");
	    }
	    
	    // Get MedicationPrescription
	    //   surprise! HealthPort doesn't support PATIENT search parameter, need to use SUBJECT instead
	    //   ...so we'll do the search by URL for now
	    String searchstr = serverBase + "MedicationPrescription?subject:Patient=" + id.getIdPart();
	    UriDt searchurl = new UriDt(searchstr);
	    Bundle prescriptions = client.search(searchurl);
	    
	    if (prescriptions.size() > 0) {
	    	System.out.println("\nFound " + prescriptions.size() + " prescriptions for this patient. Here's the first one:\n");
	    	
	    	MedicationPrescription rx = prescriptions.getResources(MedicationPrescription.class).get(0);
	    	
	    	System.out.println("Display:     " + rx.getMedicationElement().getDisplay().getValue());
	    	
	    	// TODO: explore prescriptions data
	    	
	    } else {
	    	System.out.println("\nSorry, no prescriptions found for this patient.");
	    }
	    
    }
}
