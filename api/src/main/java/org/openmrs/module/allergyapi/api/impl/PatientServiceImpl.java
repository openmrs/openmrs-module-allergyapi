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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.PatientService#confirmNoKnownAllergies(org.openmrs.Patient)
	 */
	@Override
	public void confirmNoKnownAllergies(Patient patient) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.PatientService#addAllergy(org.openmrs.Patient,
	 *      org.openmrs.activelist.Allergy)
	 */
	@Override
	public void addAllergy(Patient patient, Allergy allergy) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.PatientService#removeAllergy(org.openmrs.Patient,
	 *      org.openmrs.activelist.Allergy)
	 */
	@Override
	public void removeAllergy(Patient patient, Allergy allergy) {
		// TODO Auto-generated method stub
		
	}
}
