package org.openmrs.module.allergyapi.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.allergyapi.Allergen;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.response.ConversionException;

/**
 * Outputs a REST-compatible representation of an Allergen
 */
@Handler(supports = Allergen.class)
public class AllergenConverter extends OutputOnlyConverter<Allergen> {

    @Override
    public SimpleObject asRepresentation(Allergen allergen, Representation representation) throws ConversionException {
        SimpleObject ret = new SimpleObject();
        ret.add("type", allergen.getAllergenType());
        ret.add("codedAllergen", ConversionUtil.convertToRepresentation(allergen.getCodedAllergen(), representation));
        ret.add("nonCodedAllergen", allergen.getNonCodedAllergen());
        return ret;
    }

}
