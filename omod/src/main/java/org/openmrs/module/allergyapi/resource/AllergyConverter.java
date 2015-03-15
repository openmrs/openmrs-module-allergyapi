package org.openmrs.module.allergyapi.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.allergyapi.Allergy;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.response.ConversionException;

/**
 * Outputs a REST-compatible representation of a single Allergy (within an Allergies object)
 */
@Handler(supports = Allergy.class)
public class AllergyConverter extends OutputOnlyConverter<Allergy> {

    @Override
    public SimpleObject asRepresentation(Allergy allergy, Representation representation) throws ConversionException {
        SimpleObject ret = new SimpleObject()
                .add("uuid", allergy.getUuid())
                .add("display", allergy.getAllergen().toString());
        if (representation.equals(Representation.DEFAULT)) {
            ret.add("allergen", ConversionUtil.convertToRepresentation(allergy.getAllergen(), Representation.REF));
            ret.add("severity", ConversionUtil.convertToRepresentation(allergy.getSeverity(), Representation.REF));
            ret.add("reactions", ConversionUtil.convertToRepresentation(allergy.getReactions(), Representation.REF));
            ret.add("comment", allergy.getComment());
        }
        else if (representation.equals(Representation.FULL)) {
            ret.add("patient", ConversionUtil.convertToRepresentation(allergy.getAllergen(), Representation.REF));
            ret.add("allergen", ConversionUtil.convertToRepresentation(allergy.getAllergen(), Representation.DEFAULT));
            ret.add("severity", ConversionUtil.convertToRepresentation(allergy.getSeverity(), Representation.DEFAULT));
            ret.add("reactions", ConversionUtil.convertToRepresentation(allergy.getReactions(), Representation.DEFAULT));
            ret.add("comment", allergy.getComment());
        }
        return ret;
    }

}
