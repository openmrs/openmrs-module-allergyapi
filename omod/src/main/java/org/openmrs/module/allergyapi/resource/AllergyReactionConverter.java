package org.openmrs.module.allergyapi.resource;

import org.openmrs.Concept;
import org.openmrs.annotation.Handler;
import org.openmrs.module.allergyapi.AllergyReaction;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.response.ConversionException;

/**
 * Outputs a REST-compatible representation of an AllergyReaction
 */
@Handler(supports = AllergyReaction.class)
public class AllergyReactionConverter extends OutputOnlyConverter<AllergyReaction> {

    @Override
    public SimpleObject asRepresentation(AllergyReaction allergyReaction, Representation representation) throws ConversionException {
        SimpleObject ret = new SimpleObject();
        ret.add("reaction", ConversionUtil.convertToRepresentation(allergyReaction.getReaction(), representation));
        ret.add("reactionNonCoded", allergyReaction.getReactionNonCoded());
        return ret;
    }
}
