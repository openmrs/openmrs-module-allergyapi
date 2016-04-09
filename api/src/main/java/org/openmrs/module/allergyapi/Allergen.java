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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;

/**
 * Represent allergen
 */
public class Allergen {
	
	private AllergenType allergenType;
	
	private Concept codedAllergen;
	
	private String nonCodedAllergen;
	
	public static String OTHER_NON_CODED_UUID = "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	/**
	 * Default constructor
	 */
	public Allergen(){
	}
	
	/**
	 * @param allergenType the allergenType to set
	 * @param codedAllergen the codedAllergen to set
	 * @param nonCodedAllergen the nonCodedAllergen to set
	 */
	public Allergen(AllergenType allergenType, Concept codedAllergen, String nonCodedAllergen) {
		this.allergenType = allergenType;
		this.codedAllergen = codedAllergen;
		this.nonCodedAllergen = nonCodedAllergen;
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
     * @return the codedAllergen
     */
    public Concept getCodedAllergen() {
    	return codedAllergen;
    }

    /**
     * @param codedAllergen the codedAllergen to set
     */
    public void setCodedAllergen(Concept codedAllergen) {
		this.codedAllergen = codedAllergen;
		if (codedAllergen != null && !codedAllergen.getUuid().equals(OTHER_NON_CODED_UUID)) {
			nonCodedAllergen = null;
		}
    }
    
	/**
	 * Returns other non coded concept uuid constant.
	 * 
	 * @return other non coded concept uuid constant
	 * @since 1.5
	 */
	public static String getOtherNonCodedConceptUuid() {
		return OTHER_NON_CODED_UUID;
	}
	
    /**
     * @return the nonCodedAllergen
     */
    public String getNonCodedAllergen() {
    	return nonCodedAllergen;
    }

    /**
     * @param nonCodedAllergen the nonCodedAllergen to set
     */
    public void setNonCodedAllergen(String nonCodedAllergen) {
		this.nonCodedAllergen = nonCodedAllergen;
		if (StringUtils.isNotBlank(nonCodedAllergen)) {
			if (codedAllergen != null && !codedAllergen.getUuid().equals(OTHER_NON_CODED_UUID)) {
				codedAllergen = null;
			}
		}
    }

	public boolean isCoded(){
		if (codedAllergen == null || codedAllergen.getUuid().equals(OTHER_NON_CODED_UUID)) {
			return false;
		}
		return true;
	}

	@Override
    public String toString() {
	    if (StringUtils.isNotBlank(nonCodedAllergen)) {
	    	return nonCodedAllergen;
	    }
	    return codedAllergen.getName().getName();
    }
	
	public String getEscapeJavascript(){
		return StringEscapeUtils.escapeJavaScript(toString());
	}
	
	/**
	 * Checks if this allergen is the same as the given one
	 * 
	 * @param allergen the given allergen to test with
	 * 
	 * @should return true for same coded allergen
	 * @should return false for different coded allergen
	 * @should return true for same non coded allergen
	 * @should return false for different non coded allergen
	 * 
	 * @return true if the same, else false
	 */
	public boolean isSameAllergen(Allergen allergen) {
		if (isCoded()) {
			if (allergen.getCodedAllergen() == null) {
				return false;
			}
			if (!codedAllergen.equals(allergen.getCodedAllergen())) {
				return false;
			}
		}
		else {
			if (nonCodedAllergen == null || allergen.getNonCodedAllergen() == null) {
				return false;
			}
			if (!nonCodedAllergen.equalsIgnoreCase(allergen.getNonCodedAllergen())) {
				return false;
			}
		}
		
		return true;
	}
}
