package org.ewall.app;

import java.util.List;

import org.ewall.app.HealthportDataProvider;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;
import ca.uhn.fhir.model.dstu.resource.Medication;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;

/**
 * Exploring data from HealthPort FHIR server
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Let's get this FHIR started!\n" );

        // Warning: slf4j-simple will log messages to STDOUT
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
        
        // Get a data provider
        HealthportDataProvider hpdata = new HealthportDataProvider();
         
        // Get all Patients
        List<Patient> patients = hpdata.getAllPatients();
        System.out.println("Found " + patients.size() + " patients in total.\n");
        
        // Get specific Patient ID
        String resid = "Patient/3.568001602-01"; // GT HealthPort FHIR server
        //String resid = "d1132446701"; // HAPI-FHIR DSTU1 demo server
        System.out.println("Fetching Patient with RES_ID '" + resid + "'.\n");
        Patient patient = hpdata.getPatientById(resid);

        // Explore Patient properties
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
	    List<Observation> observations = hpdata.getAllObservationsForPatient(resid);
	    
	    // Explore Observation properties
	    if (observations.size() > 0) {
	    	System.out.println("\nFound " + observations.size() + " observations for this patient. Here's the first one:\n");
	    	
	    	Observation obs = observations.get(0);
	    	
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
	    List<Condition> conditions = hpdata.getAllConditionsForPatient(resid);
	    
	    // Explore Condition properties
	    if (conditions.size() > 0) {
	    	System.out.println("\nFound " + conditions.size() + " conditions for this patient. Here's the first one:\n");
	    	
	    	Condition cond = conditions.get(0);
	    	
	    	System.out.println("Name:        " + cond.getCode().getCodingFirstRep().getDisplayElement().getValue());
	    	System.out.println(" - System:   " + cond.getCode().getCodingFirstRep().getSystemElement().getValue());
	    	System.out.println(" - Code:     " + cond.getCode().getCodingFirstRep().getCodeElement().getValue());
	    	
	    	DateDt onset = (DateDt) cond.getOnsetElement();
	    	if (onset!=null) System.out.println("Onset:       " + onset.getValueAsString());
	    	
	    	System.out.println("Status:      " + cond.getStatusElement().getValue());
	    	
	    } else {
	    	System.out.println("\nSorry, no conditions found for this patient.");
	    }
	    
	    // Get MedicationPrescription
	    List<MedicationPrescription> prescriptions = hpdata.getAllPrescriptionsForPatient(resid);

	    // Explore MedicationPrescription properties
	    if (prescriptions.size() > 0) {
	    	System.out.println("\nFound " + prescriptions.size() + " prescriptions for this patient. Here's the first one:\n");
	    	
	    	MedicationPrescription rx = prescriptions.get(0);
	    	
	    	System.out.println("Display:     " + rx.getMedicationElement().getDisplay().getValue());
	    	
	    	if (!rx.getContained().getContainedResources().isEmpty()) {
	    		Medication med = (Medication) rx.getContained().getContainedResources().get(0);
	    		System.out.println(" - System:   " + med.getCodeElement().getCodingFirstRep().getSystemElement().getValueAsString());
	    		System.out.println(" - Code:     " + med.getCodeElement().getCodingFirstRep().getCodeElement().getValueAsString());
	    	}
	    	
	    	DateTimeDt written = (DateTimeDt) rx.getDateWrittenElement();
	    	if (written!=null) System.out.println("Written:     " + written.getValueAsString());
	    	System.out.println("Prescriber:  " + rx.getPrescriberElement().getDisplay().getValue());
	    	System.out.println("Status:      " + rx.getStatusElement().getValue());
	    	
	    	System.out.println("Dosage:");
	    	System.out.println(" - Quantity: " + rx.getDosageInstructionFirstRep().getDoseQuantityElement().getValueElement().getValue());
	    	System.out.println(" - Units:    " + rx.getDosageInstructionFirstRep().getDoseQuantityElement().getUnitsElement().getValue());
	    	System.out.println(" - How?:     " + rx.getDosageInstructionFirstRep().getTextElement().getValue());

	    	System.out.println("Dispense:");
	    	System.out.println(" - Quantity: " + rx.getDosageInstructionFirstRep().getDoseQuantityElement().getUnitsElement().getValue());
	    	System.out.println(" - Refill?:  " + rx.getDosageInstructionFirstRep().getTextElement().getValue());
	    	

	    } else {
	    	System.out.println("\nSorry, no prescriptions found for this patient.");
	    }
	    
    }
}
