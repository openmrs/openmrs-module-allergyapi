/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.allergyapi.api;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.allergyapi.Allergen;
import org.openmrs.module.allergyapi.AllergenType;
import org.openmrs.module.allergyapi.Allergies;
import org.openmrs.module.allergyapi.Allergy;
import org.openmrs.module.allergyapi.AllergyReaction;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests allergy methods in {@link $ PatientService} .
 */
public class AllergyServiceTest extends BaseModuleContextSensitiveTest {
	
	private static final String ALLERGY_TEST_DATASET = "allergyTestDataset.xml";
	
	private PatientService allergyService;
	
	private static boolean statusFieldAdded = false;
	
	@Before
	public void runBeforeAllTests() throws Exception {
		if (allergyService == null) {
			allergyService = Context.getService(PatientService.class);
		}
		
		if (!statusFieldAdded) {
			String sql = "alter table patient add column allergy_status varchar(50)";
			Context.getAdministrationService().executeSQL(sql, false);
			statusFieldAdded = true;
		}
		
		executeDataSet(ALLERGY_TEST_DATASET);
	}
	
	/**
	 * @see PatientService#getAllergies(Patient)
	 * @verifies get the allergy list and status
	 */
	@Test
	public void getAllergies_shouldGetTheAllergyListAndStatus() throws Exception {
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//should properly load reactions
		Assert.assertEquals(2, allergies.get(0).getReactions().size());
		Assert.assertEquals(2, allergies.get(1).getReactions().size());
		Assert.assertEquals(0, allergies.get(2).getReactions().size());
		Assert.assertEquals(0, allergies.get(3).getReactions().size());
		
		//get a patient without allergies
		patient = Context.getPatientService().getPatient(6);
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.UNKNOWN, allergies.getAllergyStatus());
		Assert.assertEquals(0, allergies.size());
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies save the allergy list and status
	 */
	@Test
	public void setAllergies_shouldSaveTheAllergyListAndStatus() throws Exception {
		//get a patient without any allergies
		Patient patient = Context.getPatientService().getPatient(7);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.UNKNOWN, allergies.getAllergyStatus());
		Assert.assertEquals(0, allergies.size());
		
		//save some allergies for this patient
		Allergen allergen = new Allergen(AllergenType.DRUG, new Concept(3), null);
		Concept severity = new Concept(4);
		Allergy allergy = new Allergy(patient, allergen, severity, "some comment", new ArrayList<AllergyReaction>());
		AllergyReaction reaction = new AllergyReaction(allergy, new Concept(21), null);
		allergy.addReaction(reaction);
		allergies = new Allergies();
		allergies.add(allergy);
		allergyService.setAllergies(patient, allergies);
		
		//now the patient should have allergies and the correct allergy status
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(1, allergies.size());
		Assert.assertEquals(1, allergies.get(0).getReactions().size());
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void removed allergies and maintain status as see list if some allergies are
	 *           removed
	 */
	@Test
	public void setAllergies_shouldVoidRemovedAllergiesAndMaintainStatusAsSeeListIfSomeAllergiesAreRemoved()
	    throws Exception {
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//remove some three allergies
		allergies.remove(0);
		allergies.remove(0);
		allergies.remove(0);
		
		allergyService.setAllergies(patient, allergies);
		
		//should remain with one un voided allergy and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(1, allergies.size());
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void all allergies and set status to unknown if all allergies are removed
	 */
	@Test
	public void setAllergies_shouldVoidAllAllergiesAndSetStatusToUnknownIfAllAllergiesAreRemoved() throws Exception {
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//remove all allergies
		while (allergies.size() > 0) {
			allergies.remove(0);
		}
		
		allergyService.setAllergies(patient, allergies);
		
		//all allergies should be voided and status set to unknown
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.UNKNOWN, allergies.getAllergyStatus());
		Assert.assertEquals(0, allergies.size());
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void all allergies and set status to no known allergies if all allergies are
	 *           removed and status set as such
	 */
	@Test
	public void setAllergies_shouldVoidAllAllergiesAndSetStatusToNoKnownAllergiesIfAllAllergiesAreRemovedAndStatusSetAsSuch()
	    throws Exception {
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//remove all allergies
		while (allergies.size() > 0) {
			allergies.remove(0);
		}
		
		//set the status to no known allergies
		allergies.confirmNoKnownAllergies();
		allergyService.setAllergies(patient, allergies);
		
		//all allergies should be voided and status set to no known allergies
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.NO_KNOWN_ALLERGIES, allergies.getAllergyStatus());
		Assert.assertEquals(0, allergies.size());
	}
}
