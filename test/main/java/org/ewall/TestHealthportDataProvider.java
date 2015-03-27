package org.ewall;

import org.junit.* ;
import static org.junit.Assert.* ;
import org.ewall.app.HealthportDataProvider;

public class TestHealthportDataProvider {

	private static final String id = "Patient/3.568001602-01";
	private static HealthportDataProvider hpdata;
	
	@BeforeClass
	public static void prepare()
	{
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
		hpdata = new HealthportDataProvider();
	}
	
	@Test
	public void test_LoadPatients() {
		System.out.println("Loading Patients...");
		assertTrue(hpdata.getAllPatients().size() == 305);
	}

	@Test
	public void test_LoadObservations() {
		System.out.println("Loading Observations...");
		assertTrue(hpdata.getAllObservationsForPatient(id).size() == 193);
	}

	@Test
	public void test_LoadConditions() {
		System.out.println("Loading Conditions...");
		assertTrue(hpdata.getAllConditionsForPatient(id).size() == 1);
	}

	@Test
	public void test_LoadPrescriptions() {
		System.out.println("Loading Prescriptions...");
		assertTrue(hpdata.getAllPrescriptionsForPatient(id).size() == 10);
	}

}
