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

import java.util.ArrayList;
import java.util.List;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.Patient;

/**
 * Represent allergy
 */
public class Allergy extends BaseOpenmrsData implements java.io.Serializable {
	
	public static final long serialVersionUID = 1;
	
	private Integer allergyId;
	
	private Patient patient;
	
	private Allergen allergen;
	
	private Concept severity;
	
	private String comment;
	
	private List<AllergyReaction> reactions = new ArrayList<AllergyReaction>();
	
	/**
	 * Default constructor
	 */
	public Allergy(){
	}
	
	/**
	 * @param patient the patient to set
	 * @param allergen the allergen to set
	 * @param severity the severity to set
	 * @param comment the comment to set
	 * @param reactions the reactions to set
	 */
	public Allergy(Patient patient, Allergen allergen, Concept severity, String comment, List<AllergyReaction> reactions) {
		this.patient = patient;
		this.allergen = allergen;
		this.severity = severity;
		this.comment = comment;
		this.reactions = reactions;
	}
	
    /**
     * @return the allergyId
     */
    public Integer getAllergyId() {
    	return allergyId;
    }

    /**
     * @param allergyId the allergyId to set
     */
    public void setAllergyId(Integer allergyId) {
    	this.allergyId = allergyId;
    }

	/**
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	@Override
	public Integer getId() {
		return allergyId;
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer allergyId) {
		this.allergyId = allergyId;
	}
	
	/**
	 * @return Returns the patient
	 */
	public Patient getPatient() {
		return patient;
	}
	
	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	/**
	 * @return Returns the allergen
	 */
	public Allergen getAllergen() {
		return allergen;
	}
	
	/**
	 * @param allergen the allergen to set
	 */
	public void setAllergen(Allergen allergen) {
		this.allergen = allergen;
	}
	/**
	 * @return Returns the severity
	 */
	public Concept getSeverity() {
		return severity;
	}
	
	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(Concept severity) {
		this.severity = severity;
	}
	
	/**
	 * @return Returns the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return Returns the reactions
	 */
	public List<AllergyReaction> getReactions() {
		return reactions;
	}
	
	/**
	 * @param reactions the reactions to set
	 */
	public void setReactions(List<AllergyReaction> reactions) {
		this.reactions = reactions;
	}

	/**
	 * Adds a new allergy reaction
	 * 
	 * @param reaction the reaction to add
	 * @return true if the reaction was added, else false
	 */
	public boolean addReaction(AllergyReaction reaction) {
		reaction.setAllergy(this);
		return getReactions().add(reaction);
	}
	
	/**
	 * Removes an allergy reaction
	 * 
	 * @param reaction the reaction to remove
	 * @return true if the reaction was found and removed, else false.
	 */
	public boolean removeReaction(AllergyReaction reaction) {
		return getReactions().remove(reaction);
	}
}
