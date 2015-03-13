package org.ewall.app;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * What the duck, world?
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Let's get this FHIR started!" );

        // warning: slf4j-simple will log messages to stdout here
        
        FhirContext ctx = new FhirContext();

        String serverBase = "http://fhirtest.uhn.ca/base";
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
         
        // Perform a search
        Bundle results = client
              .search()
              .forResource(Patient.class)
              .where(Patient.FAMILY.matches().value("duck"))
              .execute();
         
        System.out.println("Found " + results.size() + " patients named 'duck'. Here's the first one:\n");
        
        Patient patient = results.getResources(Patient.class).get(0);

	    StringDt patientId = patient.getIdentifier().get(0).getValue();
	    System.out.println("Patient Id:  " + patientId.getValue()); // 7000135

	    HumanNameDt name = patient.getName().get(0);
	    System.out.println("First Name:  " + name.getGiven().get(0).getValue()); // Donald
	    System.out.println("Last Name:   " + name.getFamily().get(0).getValue()); // Duck

	    System.out.println("Birth Date:  " + patient.getBirthDate().getValueAsString()); // 1980-06-01
	    
	    CodeDt gender = patient.getGender().getCoding().get(0).getCode();
	    System.out.println("Gender:      " + gender.getValue()); // M
	    
	    AddressDt address = patient.getAddress().get(0);
	    System.out.println("Address:     " + address.getLine().get(0));
	    System.out.println("             " + address.getCity().getValue() + ", "
	    								   + address.getState().getValue() + " "
	    								   + address.getZip().getValue());
	    System.out.println("             " + address.getCountry().getValue());
	    
	    //System.out.println("Married?:    " + patient.getMaritalStatus().getCoding().get(0).getCode().getValue());
    }
}
