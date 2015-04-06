package org.ewall;

import static org.junit.Assert.assertTrue;

import org.ewall.app.SparkDataProvider;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSparkDataProvider {

	private static final String id = "Patient/f201";
	private static SparkDataProvider fhirdata;
	
	@BeforeClass
	public static void prepare()
	{
		System.out.println("Testing Spark demo server...");
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
		fhirdata = new SparkDataProvider();
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
		assertTrue(fhirdata.getAllObservationsForPatient(id).size() == 6);
	}

	@Test
	public void test_LoadConditions() {
		assertTrue(fhirdata.getAllConditionsForPatient(id).size() == 5);
	}

	@Test
	public void test_LoadPrescriptions() {
		assertTrue(fhirdata.getAllPrescriptionsForPatient(id).size() == 12);
	}

}
