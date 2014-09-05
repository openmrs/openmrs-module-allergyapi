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

import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.allergyapi.Allergies;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured
 * in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(AllergyService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
public interface PatientService extends OpenmrsService {
	
	/**
	 * Gets allergies for a given patient
	 * 
	 * @param patient the patient
	 * @return the allergies object
	 * @should get the allergy list and status
	 */
	Allergies getAllergies(Patient patient);
	
	/**
	 * Updates the patient's allergies
	 * 
	 * @param patient the patient
	 * @param allergies the allergies
	 * @return the saved allergies
	 * @should save the allergy list and status
	 * @should void removed allergies and maintain status as see list if some allergies are removed
	 * @should void all allergies and set status to unknown if all allergies are removed
	 * @should set status to no known allergies for patient without allergies
	 * @should void all allergies and set status to no known allergies if all allergies are removed and status set as such
	 * @should void allergies with edited comment
	 * @should void allergies with edited severity
	 * @should void allergies with edited coded allergen
	 * @should void allergies with edited non coded allergen
	 * @should void allergies with edited reaction coded
	 * @should void allergies with edited reaction non coded
	 * @should void allergies with removed reactions
	 * @should void allergies with added reactions
	 */
	Allergies setAllergies(Patient patient, Allergies allergies);
}
