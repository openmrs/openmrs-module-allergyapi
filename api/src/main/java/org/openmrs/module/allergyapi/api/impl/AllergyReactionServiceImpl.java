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
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.allergyapi.AllergyReaction;
import org.openmrs.module.allergyapi.api.AllergyReactionService;
import org.openmrs.module.allergyapi.api.db.AllergyReactionDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link org.openmrs.module.allergyapi.api.AllergyReactionService}.
 */
@Transactional
public class AllergyReactionServiceImpl extends BaseOpenmrsService implements AllergyReactionService {

    protected final Log log = LogFactory.getLog(this.getClass());

    private AllergyReactionDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(AllergyReactionDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public AllergyReactionDAO getDao() {
        return dao;
    }

    /**
     * @see org.openmrs.module.allergyapi.api.AllergyReactionService#getAllAllergyReactions()
     */
    @Override
    @Transactional(readOnly = true)
    public List<AllergyReaction> getAllAllergyReactions() {
        return dao.getAllAllergyReactions();
    }
}
