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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.util.OpenmrsUtil;

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
		
		//we do not allow to be in a state where reactions is null
		if (reactions != null) {
			this.reactions = reactions;
		}
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
		//we do not allow to be in a state where reactions is null
		if (reactions != null) {
			this.reactions = reactions;
		}
		else {
			this.reactions.clear();
		}
	}

	/**
	 * Adds a new allergy reaction
	 * 
	 * @param reaction the reaction to add
	 * @return true if the reaction was added, else false
	 */
	public boolean addReaction(AllergyReaction reaction) {
        if(getReactionConcepts().contains(reaction.getReaction())){
            return false;
        }
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
	
	public Date getDateLastUpdated() {
		if (getDateChanged() != null) {
			return getDateChanged();
		}
		return getDateCreated();
	}
	
	/**
	 * Checks if this allergy has the same values as a given one.
	 * 
	 * @param allergy the allergy whose values to compare with
	 * @return true if the values match, else false
	 */
	public boolean hasSameValues(Allergy allergy) {
		if (!OpenmrsUtil.nullSafeEquals(getAllergyId(), allergy.getAllergyId())) {
			return false;
		}
		if (!OpenmrsUtil.nullSafeEquals(getPatient(), allergy.getPatient())) {
			//if object instances are different but with the same patient id, then not changed
			if (getPatient() != null && allergy.getPatient() != null) {
				if (!OpenmrsUtil.nullSafeEquals(getPatient().getPatientId(), allergy.getPatient().getPatientId())) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		if (!OpenmrsUtil.nullSafeEquals(getAllergen().getCodedAllergen(), allergy.getAllergen().getCodedAllergen())) {
			//if object instances are different but with the same concept id, then not changed
			if (getAllergen().getCodedAllergen() != null && allergy.getAllergen().getCodedAllergen() != null) {
				if (!OpenmrsUtil.nullSafeEquals(getAllergen().getCodedAllergen().getConceptId(), allergy.getAllergen().getCodedAllergen().getConceptId())) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		if (!OpenmrsUtil.nullSafeEquals(getAllergen().getNonCodedAllergen(), allergy.getAllergen().getNonCodedAllergen())) {
			return false;
		}
		if (!OpenmrsUtil.nullSafeEquals(getSeverity(), allergy.getSeverity())) {
			//if object instances are different but with the same concept id, then not changed
			if (getSeverity() != null && allergy.getSeverity() != null) {
				if (!OpenmrsUtil.nullSafeEquals(getSeverity().getConceptId(), allergy.getSeverity().getConceptId())) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		if (!OpenmrsUtil.nullSafeEquals(getComment(), allergy.getComment())) {
			return false;
		}
		if (!hasSameReactions(allergy)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if this allergy has the same reaction values as those in the given one
	 * 
	 * @param allergy the allergy who reaction values to compare with
	 * @return true if the values match, else false
	 */
	private boolean hasSameReactions(Allergy allergy) {
		if (getReactions().size() != allergy.getReactions().size()) {
			return false;
		}
		
		for (AllergyReaction reaction : getReactions()) {
			AllergyReaction rc = allergy.getAllergyReaction(reaction.getAllergyReactionId());
			if (!reaction.hasSameValues(rc)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Gets an allergy reaction with a given id
	 * 
	 * @param allergyReactionId the allergy reaction id
	 * @return the allergy reaction with a matching id
	 */
	public AllergyReaction getAllergyReaction(Integer allergyReactionId) {
		for (AllergyReaction reaction : reactions) {
			if (OpenmrsUtil.nullSafeEquals(reaction.getAllergyReactionId(), allergyReactionId)) {
				return reaction;
			}
		}
		
		return null;
	}
	
	/**
	 * Copies all property values, apart from the id and uuid,
	 * from the given allergy into this object
	 * 
	 * @param allergy the allergy whose propery values to copy
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void copy(Allergy allergy) throws InvocationTargetException, IllegalAccessException {
		setAllergyId(null);
		setUuid(UUID.randomUUID().toString());
		setPatient(allergy.getPatient());
		setAllergen(allergy.getAllergen());
		setSeverity(allergy.getSeverity());
		setComment(allergy.getComment());
		setReactions(new ArrayList<AllergyReaction>());
		
		for (AllergyReaction reaction : allergy.getReactions()) {
			reactions.add(reaction);
			reaction.setAllergyReactionId(null);
			reaction.setUuid(UUID.randomUUID().toString());
		}
	}

    private List<Concept> getReactionConcepts(){
        List<Concept> reactionConcepts = new ArrayList<Concept>(getReactions().size());
        for (AllergyReaction ar : getReactions()) {
            reactionConcepts.add(ar.getReaction());
        }
        return reactionConcepts;
    }
}
