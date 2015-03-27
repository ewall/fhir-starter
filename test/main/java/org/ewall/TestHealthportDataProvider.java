package org.ewall;

import org.junit.* ;

import static org.junit.Assert.* ;
import org.junit.runners.MethodSorters;

import org.ewall.app.HealthportDataProvider;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHealthportDataProvider extends AbstractBenchmark {

	private static final String id = "Patient/3.568001602-01";
	private static HealthportDataProvider hpdata;
	
	@BeforeClass
	public static void prepare()
	{
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
		hpdata = new HealthportDataProvider();
	}
	
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test_LoadPatientsFIRST() {
		assertTrue(hpdata.getAllPatients().size() == 305);
	}
	
	@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
	@Test
	public void test_LoadPatientsREST() {
		assertTrue(hpdata.getAllPatients().size() == 305);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test_LoadObservationsFIRST() {
		assertTrue(hpdata.getAllObservationsForPatient(id).size() == 193);
	}

	@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
	@Test
	public void test_LoadObservationsREST() {
		assertTrue(hpdata.getAllObservationsForPatient(id).size() == 193);
	}

	@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
	@Test
	public void test_LoadConditions() {
		assertTrue(hpdata.getAllConditionsForPatient(id).size() == 1);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test_LoadPrescriptionsFIRST() {
		assertTrue(hpdata.getAllPrescriptionsForPatient(id).size() == 10);
	}

	@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
	@Test
	public void test_LoadPrescriptionsREST() {
		assertTrue(hpdata.getAllPrescriptionsForPatient(id).size() == 10);
	}

}
