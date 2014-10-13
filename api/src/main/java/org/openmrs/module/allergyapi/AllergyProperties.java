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
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("allergyProperties")
public class AllergyProperties {
	
	@Autowired
	@Qualifier("conceptService")
	protected ConceptService conceptService;
	
	@Autowired
	@Qualifier("adminService")
	protected AdministrationService administrationService;
	
	protected Concept getConceptByGlobalProperty(String globalPropertyName) {
		String globalProperty = administrationService.getGlobalProperty(globalPropertyName);
		Concept concept = conceptService.getConceptByUuid(globalProperty);
		if (concept == null) {
			throw new IllegalStateException("Configuration required: " + globalPropertyName);
		}
		return concept;
	}
	
	public Concept getMildSeverityConcept() {
		return getConceptByGlobalProperty("allergy.concept.severity.mild");
	}
	
	public Concept getModerateSeverityConcept() {
		return getConceptByGlobalProperty("allergy.concept.severity.moderate");
	}
	
	public Concept getSevereSeverityConcept() {
		return getConceptByGlobalProperty("allergy.concept.severity.severe");
	}
	
	public Concept getFoodAllergensConcept() {
		return getConceptByGlobalProperty("allergy.concept.allergen.food");
	}
	
	public Concept getDrugAllergensConcept() {
		return getConceptByGlobalProperty("allergy.concept.allergen.drug");
	}
	
	public Concept getEnvironmentAllergensConcept() {
		return getConceptByGlobalProperty("allergy.concept.allergen.environment");
	}
	
	public Concept getAllergyReactionsConcept() {
		return getConceptByGlobalProperty("allergy.concept.reactions");
	}
	
	public Concept getOtherNonCodedConcept() {
		return getConceptByGlobalProperty("allergy.concept.otherNonCoded");
	}
	
	public Concept getUnknownConcept() {
		return getConceptByGlobalProperty("allergy.concept.unknown");
	}
}
