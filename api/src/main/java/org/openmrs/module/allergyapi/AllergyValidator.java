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

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.allergyapi.api.PatientService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AllergyValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Allergy.class.isAssignableFrom(clazz);
	}
	
	/**
	 * @see Validator#validate(Object, org.springframework.validation.Errors)
	 * @param target
	 * @param errors
	 * @should fail for a null value
	 * @should fail if patient is null
	 * @should fail id allergenType is null
	 * @should fail if allergen is null
	 * @should fail if codedAllergen is null
	 * @should fail if nonCodedAllergen is null and allergen is set to other non coded
	 * @should reject a duplicate allergen
	 * @should reject a duplicate non coded allergen
	 * @should pass for a valid allergy
	 */
	@Override
	public void validate(Object target, Errors errors) {
		
		if (target == null) {
			throw new IllegalArgumentException("Allergy should not be null");
		}
		
		ValidationUtils.rejectIfEmpty(errors, "patient", "allergyapi.patient.required");
		
		Allergy allergy = (Allergy) target;
		
		if (allergy.getAllergen() == null) {
			errors.rejectValue("allergen", "allergyapi.allergen.required");
		} else {
			Allergen allergen = allergy.getAllergen();
			if (allergen.getAllergenType() == null) {
				errors.rejectValue("allergen", "allergyapi.allergenType.required");
			}
			
			if (allergen.getCodedAllergen() == null && StringUtils.isBlank(allergen.getNonCodedAllergen())) {
				errors.rejectValue("allergen", "allergyapi.allergen.codedOrNonCodedAllergen.required");
			} else if (!allergen.isCoded() && StringUtils.isBlank(allergen.getNonCodedAllergen())) {
				errors.rejectValue("allergen", "allergyapi.allergen.nonCodedAllergen.required");
			}
			
			if (allergy.getAllergyId() == null && allergy.getPatient() != null) {
				PatientService ps = Context.getService(PatientService.class);
				Allergies existingAllergies = ps.getAllergies(allergy.getPatient());
				if (existingAllergies.containsAllergen(allergy)) {
					errors.rejectValue("allergen", "allergyapi.message.duplicateAllergen", new Object[] { allergy
					        .getAllergen().toString() }, null);
				}
			}
		}
	}
}
