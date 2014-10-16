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
package org.openmrs.module.allergyapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.allergyapi.api.PatientService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class AllergyValidatorTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private AllergyValidator validator = new AllergyValidator();
	
	private Concept createMockConcept(String uuid) {
		Concept concept = mock(Concept.class);
		when(concept.getUuid()).thenReturn(uuid != null ? uuid : "some uuid");
		when(concept.getName()).thenReturn(new ConceptName());
		return concept;
	}
	
	/**
	 * @verifies fail for a null value
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldFailForANullValue() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Allergy should not be null");
		Allergy allergy = new Allergy();
		Errors errors = new BindException(allergy, "allergy");
		allergy = null;
		validator.validate(allergy, errors);
	}
	
	/**
	 * @verifies fail if patient is null
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldFailIfPatientIsNull() throws Exception {
		Allergy allergy = new Allergy();
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		assertTrue(errors.hasFieldErrors("patient"));
	}
	
	/**
	 * @verifies fail id allergenType is null
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldFailIdAllergenTypeIsNull() throws Exception {
		Allergy allergy = new Allergy();
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		assertTrue(errors.hasFieldErrors("allergen"));
	}
	
	/**
	 * @verifies fail if allergen is null
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldFailIfAllergenIsNull() throws Exception {
		Allergy allergy = new Allergy();
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		assertTrue(errors.hasFieldErrors("allergen"));
	}
	
	/**
	 * @verifies fail if codedAllergen is null
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldFailIfCodedAllergenIsNull() throws Exception {
		Allergy allergy = new Allergy();
		allergy.setAllergen(new Allergen(null, createMockConcept(null), null));
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		assertTrue(errors.hasFieldErrors("allergen"));
	}
	
	/**
	 * @verifies fail if nonCodedAllergen is null and allergen is set to other non coded
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldFailIfNonCodedAllergenIsNullAndAllergenIsSetToOtherNonCoded() throws Exception {
		Allergy allergy = new Allergy();
		allergy.setAllergen(new Allergen(null, createMockConcept(Allergen.OTHER_NON_CODED_UUID), null));
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		assertTrue(errors.hasFieldErrors("allergen"));
	}
	
	/**
	 * @verifies reject a duplicate allergen
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldRejectADuplicateAllergen() throws Exception {
		mockStatic(Context.class);
		PatientService ps = mock(PatientService.class);
		when(Context.getService(eq(PatientService.class))).thenReturn(ps);
		
		Allergies allergies = new Allergies();
		Concept aspirin = createMockConcept(null);
		Allergen allergen1 = new Allergen(AllergenType.DRUG, aspirin, null);
		allergies.add(new Allergy(null, allergen1, null, null, null));
		when(ps.getAllergies(any(Patient.class))).thenReturn(allergies);
		
		Allergen duplicateAllergen = new Allergen(AllergenType.FOOD, aspirin, null);
		Allergy allergy = new Allergy(mock(Patient.class), duplicateAllergen, null, null, null);
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		assertTrue(errors.hasFieldErrors("allergen"));
		assertEquals("allergyapi.message.duplicateAllergen", errors.getFieldError("allergen").getCode());
	}
	
	/**
	 * @verifies reject a duplicate non coded allergen
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldRejectADuplicateNonCodedAllergen() throws Exception {
		mockStatic(Context.class);
		PatientService ps = mock(PatientService.class);
		when(Context.getService(eq(PatientService.class))).thenReturn(ps);
		
		Allergies allergies = new Allergies();
		Concept nonCodedConcept = createMockConcept(Allergen.OTHER_NON_CODED_UUID);
		final String freeText = "some text";
		Allergen allergen1 = new Allergen(AllergenType.DRUG, nonCodedConcept, freeText);
		allergies.add(new Allergy(null, allergen1, null, null, null));
		when(ps.getAllergies(any(Patient.class))).thenReturn(allergies);
		
		Allergen duplicateAllergen = new Allergen(AllergenType.FOOD, nonCodedConcept, freeText);
		Allergy allergy = new Allergy(mock(Patient.class), duplicateAllergen, null, null, null);
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		assertTrue(errors.hasFieldErrors("allergen"));
		assertEquals("allergyapi.message.duplicateAllergen", errors.getFieldError("allergen").getCode());
	}
	
	/**
	 * @verifies pass for a valid allergy
	 * @see AllergyValidator#validate(Object, org.springframework.validation.Errors)
	 */
	@Test
	public void validate_shouldPassForAValidAllergy() throws Exception {
		mockStatic(Context.class);
		PatientService ps = mock(PatientService.class);
		when(Context.getService(eq(PatientService.class))).thenReturn(ps);
		
		Allergies allergies = new Allergies();
		Concept aspirin = new Concept();
		Allergen allergen1 = new Allergen(AllergenType.DRUG, aspirin, null);
		allergies.add(new Allergy(null, allergen1, null, null, null));
		when(ps.getAllergies(any(Patient.class))).thenReturn(allergies);
		
		Allergen anotherAllergen = new Allergen(AllergenType.DRUG, new Concept(), null);
		Allergy allergy = new Allergy(mock(Patient.class), anotherAllergen, null, null, null);
		Errors errors = new BindException(allergy, "allergy");
		validator.validate(allergy, errors);
		
		validator.validate(allergy, errors);
		assertFalse(errors.hasErrors());
	}
}
