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
package org.openmrs.module.allergyapi.api.db.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.module.allergyapi.Allergies;
import org.openmrs.module.allergyapi.Allergy;
import org.openmrs.module.allergyapi.api.db.PatientDAO;

/**
 * Default implementation of {@link PatientDAO}.
 */
public class HibernatePatientDAO implements PatientDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.db.PatientDAO#getAllergies(org.openmrs.Patient)
	 */
	@Override
	public List<Allergy> getAllergies(Patient patient) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Allergy.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.db.PatientDAO#getAllergyStatus(org.openmrs.Patient)
	 */
	@Override
	public String getAllergyStatus(Patient patient) {
		Connection connection = sessionFactory.getCurrentSession().connection();
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT allergy_status FROM patient WHERE patient_id = ?");
			ps.setInt(1, patient.getPatientId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString(1);
		}
		catch (SQLException e) {
			throw new APIException("Error while trying to get the patient allergy status", e);
		}
	}
	
	/**
	 * @see org.openmrs.module.allergyapi.api.db.PatientDAO#saveAllergies(org.openmrs.Patient,
	 *      org.openmrs.module.allergyapi.Allergies)
	 */
	@Override
	public Allergies saveAllergies(Patient patient, Allergies allergies) {
		Connection connection = sessionFactory.getCurrentSession().connection();
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE patient SET allergy_status = ? WHERE patient_id = ?");
			ps.setString(1, allergies.getAllergyStatus());
			ps.setInt(2, patient.getPatientId());
			ps.executeUpdate();
			
			for (Allergy allergy : allergies) {
				sessionFactory.getCurrentSession().save(allergy);
			}
			return allergies;
		}
		catch (SQLException e) {
			throw new APIException("Error while trying to save patient allergies", e);
		}
	}
}
