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

import org.openmrs.Concept;

/**
 * Represent allergen
 */
public class Allergen {
	
	private AllergenType allergenType;
	
	private Concept concept;
	
	private String nonCodedConcept;
	
	/**
	 * Default constructor
	 */
	public Allergen(){
	}
	
	/**
	 * @param allergenType the allergenType to set
	 * @param concept the concept to set
	 * @param nonCodedConcept the nonCodedConcept to set
	 */
	public Allergen(AllergenType allergenType, Concept concept, String nonCodedConcept) {
		this.allergenType = allergenType;
		this.concept = concept;
		this.nonCodedConcept = nonCodedConcept;
	}
	
	/**
	 * @return Returns the allergenType
	 */
	public AllergenType getAllergenType() {
		return allergenType;
	}
	
	/**
	 * @param allergenType the allergenType to set
	 */
	public void setAllergenType(AllergenType allergenType) {
		this.allergenType = allergenType;
	}
	
	/**
	 * @return Returns the concept
	 */
	public Concept getConcept() {
		return concept;
	}
	
	/**
	 * @param concept the concept to set
	 */
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	
	/**
	 * @return Returns the nonCodedConcept
	 */
	public String getAllergenNonCoded() {
		return nonCodedConcept;
	}
	
	/**
	 * @param nonCodedConcept the nonCodedConcept to set
	 */
	public void setAllergenNonCoded(String nonCodedConcept) {
		this.nonCodedConcept = nonCodedConcept;
	}
	
	boolean isCoded(){
		return concept != null;
	}
}
