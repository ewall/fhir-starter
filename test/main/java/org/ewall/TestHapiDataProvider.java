package org.ewall;

import static org.junit.Assert.assertTrue;

import org.ewall.app.HapiDataProvider;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestHapiDataProvider {

	private static final String id = "d1132446701";
	private static HapiDataProvider fhirdata;
	
	@BeforeClass
	public static void prepare()
	{
		System.out.println("Testing HAPI-FHIR demo server...");
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
		fhirdata = new HapiDataProvider();
	}
	
	@Test
	public void test_LoadSomePatients() {
		assertTrue(fhirdata.getXPatients(33).size() == 33);
	}
	
//	@Test
//	public void test_LoadALlPatients() {
//		assertTrue(hpdata.getAllPatients().size() == 305);
//	}

	@Test
	public void test_LoadObservations() {
		assertTrue(fhirdata.getAllObservationsForPatient(id).size() == 103);
	}

	@Test
	public void test_LoadConditions() {
		assertTrue(fhirdata.getAllConditionsForPatient(id).size() == 0);
	}

	@Test
	public void test_LoadPrescriptions() {
		assertTrue(fhirdata.getAllPrescriptionsForPatient(id).size() == 0);
	}

}
