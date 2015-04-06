package org.ewall;

import org.junit.* ;
import static org.junit.Assert.* ;
import org.ewall.app.HealthportDataProvider;

public class TestHealthportDataProvider {

	private static final String id = "Patient/3.568001602-01";
	private static HealthportDataProvider fhirdata;
	
	@BeforeClass
	public static void prepare()
	{
		System.out.println("Testing HealthPort...");
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
		fhirdata = new HealthportDataProvider();
	}
	
	@Test
	public void test_LoadSomePatients() {
		assertTrue(fhirdata.getXPatients(33).size() == 33);
	}
	
	@Test
	public void test_LoadALlPatients() {
		assertTrue(fhirdata.getAllPatients().size() == 305);
	}

	@Test
	public void test_LoadObservations() {
		assertTrue(fhirdata.getAllObservationsForPatient(id).size() == 193);
	}

	@Test
	public void test_LoadConditions() {
		assertTrue(fhirdata.getAllConditionsForPatient(id).size() == 1);
	}

	@Test
	public void test_LoadPrescriptions() {
		assertTrue(fhirdata.getAllPrescriptionsForPatient(id).size() == 10);
	}

}
