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
package org.openmrs.module.allergyapi.api.db;

import org.openmrs.module.allergyapi.AllergyReaction;

import java.util.List;

/**
 * Database methods for {@link org.openmrs.module.allergyapi.api.AllergyReactionService}.
 */
public interface AllergyReactionDAO {

    /**
     * Gets all allergy reactions
     *
     * @return list of allergy reactions
     * @should get the allergy reaction list
     */
    List<AllergyReaction> getAllAllergyReactions();
}
