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
package org.openmrs.module.allergyapi.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.allergyapi.Allergies;
import org.openmrs.module.allergyapi.Allergy;
import org.openmrs.module.allergyapi.api.PatientService;
import org.openmrs.module.allergyapi.api.db.PatientDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * It is a default implementation of {@link PatientService}.
 */
@Transactional
public class PatientServiceImpl extends BaseOpenmrsService implements PatientService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private PatientDAO dao;
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(PatientDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @return the dao
	 */
	public PatientDAO getDao() {
		return dao;
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.PatientService#getAllergies(org.openmrs.Patient)
	 */
	@Override
	@Transactional(readOnly = true)
	public Allergies getAllergies(Patient patient) {
		if (patient == null) {
			throw new IllegalArgumentException("An existing (NOT NULL) patient is required to get allergies");
		}
		
		Allergies allergies = new Allergies();
		List<Allergy> allergyList = dao.getAllergies(patient);
		if (allergyList.size() > 0) {
			allergies.addAll(allergyList);
		} else {
			String status = dao.getAllergyStatus(patient);
			if (Allergies.NO_KNOWN_ALLERGIES.equals(status)) {
				allergies.confirmNoKnownAllergies();
			}
		}
		return allergies;
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.PatientService#setAllergies(org.openmrs.Patient,
	 *      org.openmrs.module.allergyapi.Allergies)
	 */
	@Override
	public Allergies setAllergies(Patient patient, Allergies allergies) {
		//NOTE We neither delete nor edit allergies. We instead void them.
		//Because we shield the API users from this business logic,
		//we end up with the complicated code below. :)
		
		//get the current allergies as stored in the database
		List<Allergy> dbAllergyList = getAllergies(patient);
		for (Allergy originalAllergy : dbAllergyList) {
			//check if we still have each allergy, else it has just been deleted
			if (allergies.contains(originalAllergy)) {
				//we still have this allergy, check if it has been edited/changed
				Allergy potentiallyEditedAllergy = allergies.getAllergy(originalAllergy.getAllergyId());
				if (!potentiallyEditedAllergy.hasSameValues(originalAllergy)) {
					//allergy has been edited, so void it and create a new one with the current values
					Allergy newAllergy = new Allergy();
					try {
						//remove the edited allergy from our current list, and void id
						allergies.remove(potentiallyEditedAllergy);
						
						//copy values from edited allergy, and add it to the current list
						newAllergy.copy(potentiallyEditedAllergy);
						allergies.add(newAllergy);
						
						//we void its original values, as came from the database, 
						//instead the current ones which have just been copied 
						//into the new allergy we have just created above
						voidAllergy(originalAllergy);
					}
					catch (Exception ex) {
						throw new APIException("Failed to copy edited values", ex);
					}
				}
				continue;
			}
			
			//void the allergy that has been deleted
			voidAllergy(originalAllergy);
		}
		
		return dao.saveAllergies(patient, allergies);
	}
	
	/**
	 * Voids a given allergy
	 * 
	 * @param allergy the allergy to void
	 */
	private void voidAllergy(Allergy allergy) {
		allergy.setVoided(true);
		allergy.setVoidedBy(Context.getAuthenticatedUser());
		allergy.setDateVoided(new Date());
		allergy.setVoidReason("Voided by API");
		dao.saveAllergy(allergy);
	}
}
