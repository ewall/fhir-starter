fhir-starter
============

Learning some client basics of the [HAPI-FHIR API](https://jamesagnew.github.io/hapi-fhir/) from the [University Health Network](http://www.uhn.ca/).

Execute with `mvn exec:java -Dexec.mainClass="org.ewall.app.App" -q`

Current output should look like this:

		Let's get this FHIR started!
		Found 305 patients in total.

		Fetching Patient with RES_ID 'Patient/3.568001602-01'.
		Patient Id:  3.568001602-01
		First Name:  Lacy C.
		Last Name:   Mcpherson
		Birth Date:  null
		Married?:    null

		Found 193 observations for this patient. Here's the first one:

		Name:        Diastolic BP
		 - System:   http://loinc.org
		 - Code:     8462-4
		Value:       93.0
		Units:       mm[Hg]
		Applies:     2005-06-30T19:45:00-04:00

		Found 1 conditions for this patient. Here's the first one:

		Name:        Hypertension
		 - System:   http://hl7.org/fhir/sid/icd-9
		 - Code:     401.9
		Onset:       2002-05-26T00:00:00
		Status:      confirmed

		Found 10 prescriptions for this patient. Here's the first one:

		Display:     Hydrochlorothiazide
		 - System:   urn:oid:2.16.840.1.113883.6.69
		 - Code:     0172-2089-80
		Written:     2005-06-30T19:45:00-04:00
		Prescriber:  Athena V Morris, MD
		Status:      active
		Dosage:
		 - Quantity: 50.0
		 - Units:    mg
		 - How?:     po qd
		Dispense:
		 - Quantity: mg
		 - Refill?:  po qd

