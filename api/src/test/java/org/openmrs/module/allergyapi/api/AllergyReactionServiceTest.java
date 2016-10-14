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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.allergyapi.AllergyReaction;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.List;

/**
 * Tests allergy methods in {@link AllergyReactionService} .
 */
public class AllergyReactionServiceTest extends BaseModuleContextSensitiveTest {

    private static final String ALLERGY_TEST_DATASET = "allergyTestDataset.xml";

    private AllergyReactionService allergyReactionService;

    @Before
    public void runBeforeAllTests() throws Exception {
        if (allergyReactionService == null) {
            allergyReactionService = Context.getService(AllergyReactionService.class);
        }

        executeDataSet(ALLERGY_TEST_DATASET);
    }

    /**
     * @see org.openmrs.module.allergyapi.api.AllergyReactionService#getAllAllergyReactions()
     * @verifies get the allergy reaction list
     */
    @Test
    public void getAllAllergyReactions_shouldGetTheAllergyReactionList() {
        List<AllergyReaction> allergyReactions = allergyReactionService.getAllAllergyReactions();
        Assert.assertEquals(4, allergyReactions.size());

        AllergyReaction allergyReaction = allergyReactions.get(0);
        Assert.assertEquals("29bf4fbc-fcdb-4a5b-97ea-0d5c4b4315a1", allergyReaction.getUuid());
        Assert.assertEquals("21543629-7d8c-11e1-909d-c80aa9edcf4e", allergyReaction.getAllergy().getUuid());
    }
}
