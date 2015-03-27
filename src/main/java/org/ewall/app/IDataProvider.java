package org.ewall.app;

import java.util.Collection;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.MedicationPrescription;

/**
 * FHIR backend API.
 */
public interface IDataProvider {
    /**
     * @param id 
     * @return A single Patient resource.
     */
	Patient getPatientById(String id);

	/**
     * @return A List of Patient resources.
     */
	Collection<Patient> getAllPatients();

    /**
     * @param id Condition identifier
     * @return A single Condition resource.
     */
	Condition getConditionById(String id);

    /**
     * @param id Patient's identifier
     * @return A single Patient resource.
     */
	Collection<Condition> getAllConditionsForPatient(String id);

    /**
     * @param id Observation identifier
     * @return A single Observation resource.
     */
	Observation getObservationById(String id);

    /**
     * @param id Patient's identifier
     * @return A list of Observation resources for a specific patient.
     */
	Collection<Observation> getAllObservationsForPatient(String id);

    /**
     * @param id MedicationPrescription identifier
     * @return A single MedicationPrescription resource.
     */
	MedicationPrescription getPrescriptionById(String id);

    /**
     * @param id Patient's identifier
     * @return A single Patient resource.
     */
	Collection<MedicationPrescription> getAllPrescriptionsForPatient(String id);
}
