package org.openmrs.module.allergyapi.resource;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.allergyapi.Allergies;
import org.openmrs.module.allergyapi.api.PatientService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.Retrievable;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Arrays;
import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/allergies", supportedClass = Allergies.class, supportedOpenmrsVersions = {"1.9.*", "1.10.*", "1.11.*", "1.12.*"})
public class AllergiesResource implements Retrievable {

    @Override
    public Object retrieve(String patientUuid, RequestContext requestContext) throws ResponseException {
        // TODO figure out how to make tests work and do this instead of directly using PatientService
        // DelegatingCrudResource<Patient> patientResource = (DelegatingCrudResource<Patient>) Context.getService(RestService.class).getResourceByName("patient");
        // Patient patient = patientResource.getByUniqueId(patientUuid);

        Patient patient = Context.getPatientService().getPatientByUuid(patientUuid);
        if (patient == null) {
            throw new NullPointerException();
        }
        Allergies allergies = Context.getService(PatientService.class).getAllergies(patient);

        SimpleObject ret = new SimpleObject();
        ret.add("status", allergies.getAllergyStatus());
        ret.add("allergies", ConversionUtil.convertToRepresentation(allergies, requestContext.getRepresentation()));
        return ret;
    }

    @Override
    public List<Representation> getAvailableRepresentations() {
        return Arrays.asList(Representation.REF, Representation.DEFAULT, Representation.FULL);
    }

    @Override
    public String getUri(Object delegate) {
        // TODO cannot be implemented because Allergies doesn't have a Patient on it
        return null;
    }
}
