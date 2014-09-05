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
		
		//remove one allergy
		allergies.remove(0);
		
		//remove one reaction out of the two
		allergies.get(0).getReactions().remove(0);
		
		//add a reaction to the third allergy
		AllergyReaction reaction = new AllergyReaction(null, new Concept(22), null);
		allergies.get(2).addReaction(reaction);
		
		allergyService.setAllergies(patient, allergies);
		
		//should remain with three un voided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(3, allergies.size());
		Assert.assertEquals(1, allergies.get(0).getReactions().size());
		Assert.assertEquals(0, allergies.get(1).getReactions().size());
		Assert.assertEquals(1, allergies.get(2).getReactions().size());
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
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies set status to no known allergies for patient without allergies
	 */
	@Test
	public void setAllergies_shouldSetStatusToNoKnownAllergiesForPatientWithoutAllergies()
	    throws Exception {
		//get a patient without any allergies
		Patient patient = Context.getPatientService().getPatient(7);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.UNKNOWN, allergies.getAllergyStatus());
		Assert.assertEquals(0, allergies.size());
		
		//confirm that patient has no known allergies
		allergies = new Allergies();
		allergies.confirmNoKnownAllergies();
		allergyService.setAllergies(patient, allergies);
		
		//now the patient should have the no known allergies status
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.NO_KNOWN_ALLERGIES, allergies.getAllergyStatus());
		Assert.assertEquals(0, allergies.size());
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with edited comment
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithEditedComment()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//edit comment
		editedAllergy.setComment("edited comment");
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with edited severity
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithEditedSeverity()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//edit severity
		editedAllergy.setSeverity(new Concept(24));
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with edited coded allergen
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithEditedCodedAllergen()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//edit coded allergen
		editedAllergy.getAllergen().setCodedAllergen(new Concept(24));
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with edited non coded allergen
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithEditedNonCodedAllergen()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//edit non coded allergen
		editedAllergy.getAllergen().getCodedAllergen().setUuid(Allergen.OTHER_NON_CODED_UUID);
		editedAllergy.getAllergen().setNonCodedAllergen("some non coded allergen");
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with removed reactions
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithRemovedReactions()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//remove a reaction
		editedAllergy.getReactions().remove(0);
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with added reactions
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithAddedReactions()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//add a reaction
		AllergyReaction reaction = new AllergyReaction(null, new Concept(22), null);
		editedAllergy.addReaction(reaction);
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with edited reaction coded
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithEditedReactionCoded()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//edit a reaction
		AllergyReaction reaction = editedAllergy.getReactions().get(0);
		reaction.setReaction(new Concept(11));
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
	
	/**
	 * @see PatientService#setAllergies(Patient,Allergies)
	 * @verifies void allergies with edited reaction non coded
	 */
	@Test
	public void setAllergies_shouldVoidAllergiesWithEditedReactionNonCoded()
	    throws Exception {
		
		//get a patient with some allergies
		Patient patient = Context.getPatientService().getPatient(2);
		Allergies allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
				
		Allergy editedAllergy = allergies.get(0);
		//clear any cache for this object such that the next calls fetch it from the database
		Context.evictFromSession(editedAllergy);
		//edit a reaction
		AllergyReaction reaction = editedAllergy.getReactions().get(0);
		reaction.setReactionNonCoded("some non coded text");
		
		Assert.assertTrue(allergies.contains(editedAllergy));

		allergyService.setAllergies(patient, allergies);
		
		//should remain with four unvoided allergies and status maintained as see list
		allergies = allergyService.getAllergies(patient);
		Assert.assertEquals(Allergies.SEE_LIST, allergies.getAllergyStatus());
		Assert.assertEquals(4, allergies.size());
		
		//the edited allergy should have been voided
		Assert.assertFalse(allergies.contains(editedAllergy));
	}
}
