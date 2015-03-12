package org.ewall.app;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;

/**
 * WTF, world?
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Let's get this FHIR started!" );

        // from https://jamesagnew.github.io/hapi-fhir/doc_intro.html "Parsing a resource from a String"
        
        FhirContext ctx = new FhirContext();

        // The following is an example Patient resource
		String msgString = "<Patient xmlns=\"http://hl7.org/fhir\">"
		  + "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal</div></text>"
		  + "<identifier><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
		  + "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
		  + "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
		  + "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
		  + "</Patient>";
		 
		// The hapi context object is used to create a new XML parser
		// instance. The parser can then be used to parse (or unmarshall) the
		// string message into a Patient object
		IParser parser = ctx.newXmlParser();
		Patient patient = parser.parseResource(Patient.class, msgString);
		 
		// The patient object has accessor methods to retrieve all of the
		// data which has been parsed into the instance. All of the
		// FHIR datatypes are represented by classes which end in "Dt".
		StringDt patientId = patient.getIdentifier().get(0).getValue();
		StringDt familyName = patient.getName().get(0).getFamily().get(0);
		CodeDt gender = patient.getGender().getCoding().get(0).getCode();
		 
		// The various datatype classes have accessors called getValue()
		System.out.println(patientId.getValue()); // PRP1660
		System.out.println(familyName.getValue()); // Cardinal
		System.out.println(gender.getValue()); // M

    }
}
