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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;

/**
 * Tests methods in {@link org.openmrs.module.allergyapi.Allergen}.
 */
public class AllergenTest {
	Allergen allergen;
	
	@Test
	public void shouldEitherBeCodedOrFreeText() {
		allergen = new Allergen(AllergenType.DRUG, new Concept(3), null);
		assertCoded();
		
		allergen.setNonCodedAllergen("Non coded allergen");
		assertNonCoded();
		
		allergen.setCodedAllergen(new Concept(3));
		assertCoded();
		
		allergen = new Allergen(AllergenType.DRUG, null, "Non coded allergen");
		assertNonCoded();
	}
	
	private void assertCoded() {
		Assert.assertNotEquals(allergen.getCodedAllergen().getUuid(), Allergen.OTHER_NON_CODED_UUID);
		Assert.assertNull(allergen.getNonCodedAllergen());
		Assert.assertTrue(allergen.isCoded());
	}
	
	private void assertNonCoded() {
		Assert.assertNull(allergen.getCodedAllergen());
		Assert.assertEquals(allergen.getNonCodedAllergen(), "Non coded allergen");
		Assert.assertFalse(allergen.isCoded());
	}
	
	@Test
	public void isSameAllergen_shouldReturnTrueForSameCodedAllergen() {
        Concept c = new Concept();
		Assert.assertTrue(new Allergen(null, c, null).isSameAllergen(new Allergen(null, c, null)));
	}
	
	@Test
	public void isSameAllergen_shouldReturnFalseForDifferentCodedAllergen() {
		Assert.assertFalse(new Allergen(null, new Concept(1), null).isSameAllergen(new Allergen(null, new Concept(2), null)));
	}
	
	@Test
	public void isSameAllergen_shouldReturnTrueForSameNonCodedAllergen() {
		Concept concept = new Concept();
		concept.setUuid(Allergen.OTHER_NON_CODED_UUID);
		Assert.assertTrue(new Allergen(null, concept, "OTHER VALUE").isSameAllergen(new Allergen(null, concept, "OTHER VALUE")));
	}
	
	@Test
	public void isSameAllergen_shouldBeCaseInsensitiveForNonCodedAllergen() {
		Concept concept = new Concept();
		concept.setUuid(Allergen.OTHER_NON_CODED_UUID);
		Assert.assertTrue(new Allergen(null, concept, "other value").isSameAllergen(new Allergen(null, concept, "OTHER VALUE")));
	}
	
	@Test
	public void isSameAllergen_shouldReturnFalseForDifferentNonCodedAllergen() {
		Concept concept = new Concept();
		concept.setUuid(Allergen.OTHER_NON_CODED_UUID);
		Assert.assertFalse(new Allergen(null, concept, "OTHER VALUE1").isSameAllergen(new Allergen(null, concept, "OTHER VALUE2")));
	}
}
