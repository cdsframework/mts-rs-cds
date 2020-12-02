/*
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see <http://www.gnu.org/licenses/> for more
 * details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the New York City
 * Department of Health and Mental Hygiene, Bureau of Immunization to have (without restriction,
 * limitation, and warranty) complete irrevocable access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING,
 * BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about the this software, see http://www.hln.com/ice or send
 * correspondence to ice@hln.com.
 */
package org.cdsframework.rs.cds;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.SystemPropertyDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.ejb.local.GeneralMGRInterface;
import org.cdsframework.enumeration.DeploymentEnvironment;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.rckms.data.upload.service.uploader.OpenCDSConfigUploader;
import org.cdsframework.rckms.data.upload.service.uploader.OpenCDSConfigUploaderImpl;
import org.cdsframework.rs.GeneralRSService;
import org.cdsframework.rs.support.CdsConfiguration;
import org.cdsframework.rs.support.CoreRsConstants;
import org.cdsframework.util.VsacUtils;
import org.opencds.config.schema.ConceptDeterminationMethods;

/**
 *
 * @author HLN Consulting, LLC
 */
@Path(CoreRsConstants.GENERAL_RS_ROOT)
public class CdsRSService extends GeneralRSService {

    public CdsRSService() {
        super(CdsRSService.class);
    }

    @GET
    @Path("conceptdeterminationmethods/predefaultdeploy/{codeSystem}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, ConceptDeterminationMethods> predeployDefaultConceptDeterminationMethod(@PathParam("codeSystem") final String codeSystem, @QueryParam(CoreRsConstants.QUERYPARMSESSION) String sessionId)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException, URISyntaxException, UnsupportedEncodingException, JAXBException {
        final String METHODNAME = "predeployDefaultConceptDeterminationMethod ";

        logger.debug(METHODNAME, "codeSystem=", codeSystem);
        logger.debug(METHODNAME, "sessionId=", sessionId);

        Map<String, ConceptDeterminationMethods> result = new HashMap<>();

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(sessionId);

        String defaultCdm = getDefaultConceptDeterminationMethod(sessionDTO);

        String cdmId = codeSystem + "^" + defaultCdm + "^1.0";
        logger.info(METHODNAME, "cdmId=", cdmId);

        Map<String, byte[]> productionExportData = getExportData(codeSystem, defaultCdm, DeploymentEnvironment.PRODUCTION, cdmId, false, sessionDTO);

        Map<String, byte[]> testExportData = getExportData(codeSystem, defaultCdm, DeploymentEnvironment.TEST, cdmId, false, sessionDTO);

        result.put("productionConceptDeterminationMethods", getConceptDeterminationMethods(productionExportData.get("cdm.xml")));
        result.put("testConceptDeterminationMethods", getConceptDeterminationMethods(testExportData.get("cdm.xml")));

        return result;
    }

    private ConceptDeterminationMethods getConceptDeterminationMethods(byte[] input) throws JAXBException {
        final String METHODNAME = "getConceptDeterminationMethods ";
        JAXBContext jaxbContext = JAXBContext.newInstance(ConceptDeterminationMethods.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(new String(input));
        return (ConceptDeterminationMethods) unmarshaller.unmarshal(reader);
    }

    private String getDefaultConceptDeterminationMethod(SessionDTO sessionDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {

        final String METHODNAME = "getDefaultConceptDeterminationMethod ";
        PropertyBagDTO systemPropertyBagDTO = new PropertyBagDTO();
        systemPropertyBagDTO.setQueryClass("ByNameScope");
        SystemPropertyDTO systemPropertyDTO = new SystemPropertyDTO();
        systemPropertyDTO.setScope("cds");
        systemPropertyDTO.setName("DEFAULT_CDM");

        SystemPropertyDTO defaultCdm = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, systemPropertyBagDTO);

        logger.debug(METHODNAME, "defaultCdm=", defaultCdm);
        if (defaultCdm == null) {
            throw new MtsException("defaultCdm is null!");
        }

        return defaultCdm.getValue();
    }

    private Map<String, byte[]> getExportData(String codeSystem, String defaultCdm, DeploymentEnvironment environment, String cdmId, boolean deploy, SessionDTO sessionDTO)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getExportData ";
        ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = new ConceptDeterminationMethodDTO();
        conceptDeterminationMethodDTO.setCode(defaultCdm);
        conceptDeterminationMethodDTO.getQueryMap().put("codeSystem", codeSystem);
        conceptDeterminationMethodDTO.getQueryMap().put("environment", environment);

        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        propertyBagDTO.setQueryClass("OpenCdsExport");
        propertyBagDTO.put("deploy", deploy);
        propertyBagDTO.put("cdmId", cdmId);

        if (deploy) {
            OpenCDSConfigUploader openCDSConfigUploader;
            if (environment == DeploymentEnvironment.PRODUCTION) {
                openCDSConfigUploader = CdsConfiguration.getOpenCDSConfigUploader();
            } else {
                openCDSConfigUploader = CdsConfiguration.getOpenCDSConfigUploaderTest();
            }
            propertyBagDTO.put("openCDSConfigUploader", openCDSConfigUploader);
        }

        return getGeneralMGR().exportData(conceptDeterminationMethodDTO, sessionDTO, propertyBagDTO);
    }

    @GET
    @Path("conceptdeterminationmethods/defaultdeploy/{codeSystem}/{environment}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, ConceptDeterminationMethods> deployDefaultConceptDeterminationMethod(
            @PathParam("codeSystem") final String codeSystem,
            @PathParam("environment") final String environment,
            @QueryParam(CoreRsConstants.QUERYPARMSESSION) String sessionId)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException, URISyntaxException, UnsupportedEncodingException, JAXBException {
        final String METHODNAME = "deployDefaultConceptDeterminationMethod ";

        logger.debug(METHODNAME, "codeSystem=", codeSystem);
        logger.debug(METHODNAME, "environment=", environment);
        logger.debug(METHODNAME, "sessionId=", sessionId);

        DeploymentEnvironment deploymentEnvironment = DeploymentEnvironment.valueOf(environment);
        logger.info(METHODNAME, "deploymentEnvironment=", deploymentEnvironment);

        Map<String, ConceptDeterminationMethods> result = new HashMap<>();

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(sessionId);

        String defaultCdm = getDefaultConceptDeterminationMethod(sessionDTO);

        String cdmId = codeSystem + "^" + defaultCdm + "^1.0";
        logger.info(METHODNAME, "cdmId=", cdmId);

        Map<String, byte[]> exportData = getExportData(codeSystem, defaultCdm, deploymentEnvironment, cdmId, true, sessionDTO);

        result.put("conceptDeterminationMethods", getConceptDeterminationMethods(exportData.get("cdm.xml")));

        return result;
    }

    @GET
    @Path("conceptdeterminationmethods/deploy/{codeSystem}/{code}/{environment}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deployConceptDeterminationMethod(@PathParam("codeSystem") final String codeSystem, @PathParam("code") final String code, @PathParam("environment") final String environment, @QueryParam(CoreRsConstants.QUERYPARMSESSION) String sessionId)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException, URISyntaxException, UnsupportedEncodingException {
        final String METHODNAME = "deployConceptDeterminationMethod ";
        String data = "codeSystem=" + codeSystem + "; code=" + code + "; environment=" + environment + "; sessionId=" + sessionId;
        logger.info(METHODNAME, "data=", data);

        DeploymentEnvironment deploymentEnvironment = DeploymentEnvironment.valueOf(environment);
        logger.info(METHODNAME, "deploymentEnvironment=", deploymentEnvironment);

        ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = new ConceptDeterminationMethodDTO();
        conceptDeterminationMethodDTO.setCode(code);
        conceptDeterminationMethodDTO.getQueryMap().put("codeSystem", codeSystem);
        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        propertyBagDTO.setQueryClass("OpenCdsExport");
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(sessionId);
        Map<String, byte[]> exportData = getGeneralMGR().exportData(conceptDeterminationMethodDTO, sessionDTO, propertyBagDTO);

        String cdmId = codeSystem + "^" + code + "^1.0";
        logger.info(METHODNAME, "cdmId=", cdmId);

        logger.info(METHODNAME, "exportData=", exportData);
        logger.info(METHODNAME, "exportData.get(\"cdm.xml\")=", exportData.get("cdm.xml"));

        OpenCDSConfigUploader openCDSConfigUploader;

        if (deploymentEnvironment == DeploymentEnvironment.PRODUCTION) {
            openCDSConfigUploader = CdsConfiguration.getOpenCDSConfigUploader();
        } else {
            openCDSConfigUploader = CdsConfiguration.getOpenCDSConfigUploaderTest();
        }

        try {
            openCDSConfigUploader.deleteCdm(cdmId);
        } catch (Exception e) {
            logger.error(METHODNAME, e.getMessage());
        }

        org.cdsframework.rckms.data.upload.service.uploader.Response response = openCDSConfigUploader.addCdm(new String(exportData.get("cdm.xml")));
        logger.info(METHODNAME, "responseType=", response.getResponseType());
        logger.info(METHODNAME, "code=", response.getCode());
        logger.info(METHODNAME, "description=", response.getDescription());
        logger.info(METHODNAME, "type=", response.getType());

        return Response.ok().build();
    }

    @GET
    @Path("conceptdeterminationmethods/deploy/{codeSystem}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deployConceptDeterminationMethod(@PathParam("codeSystem") final String codeSystem, @PathParam("code") final String code, @QueryParam(CoreRsConstants.QUERYPARMSESSION) String sessionId)
            throws ValidationException, NotFoundException, MtsException, AuthenticationException, AuthorizationException, ConstraintViolationException, URISyntaxException, UnsupportedEncodingException {
        final String METHODNAME = "deployConceptDeterminationMethod ";
        String data = "codeSystem=" + codeSystem + "; code=" + code + "; sessionId=" + sessionId;
        logger.info(METHODNAME, "data=", data);
        ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = new ConceptDeterminationMethodDTO();
        conceptDeterminationMethodDTO.setCode(code);
        conceptDeterminationMethodDTO.getQueryMap().put("codeSystem", codeSystem);
        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        propertyBagDTO.setQueryClass("OpenCdsExport");
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(sessionId);
        Map<String, byte[]> exportData = getGeneralMGR().exportData(conceptDeterminationMethodDTO, sessionDTO, propertyBagDTO);

        String cdmId = codeSystem + "^" + code + "^1.0";
        logger.info(METHODNAME, "cdmId=", cdmId);

        logger.info(METHODNAME, "exportData=", exportData);
        logger.info(METHODNAME, "exportData.get(\"cdm.xml\")=", exportData.get("cdm.xml"));

        OpenCDSConfigUploader openCDSConfigUploader = new OpenCDSConfigUploaderImpl(
                CdsConfiguration.getOpenCdsRestHost(),
                CdsConfiguration.getOpenCdsRestPort(),
                CdsConfiguration.getOpenCdsRestUserName(),
                CdsConfiguration.getOpenCdsRestPassword(),
                CdsConfiguration.getOpenCdsRestCdmEndpoint(),
                CdsConfiguration.getOpenCdsRestKmEndpoint());

        try {
            openCDSConfigUploader.deleteCdm(cdmId);
        } catch (Exception e) {
            logger.error(METHODNAME, e.getMessage());
        }

        org.cdsframework.rckms.data.upload.service.uploader.Response response = openCDSConfigUploader.addCdm(new String(exportData.get("cdm.xml")));
        logger.info(METHODNAME, "responseType=", response.getResponseType());
        logger.info(METHODNAME, "code=", response.getCode());
        logger.info(METHODNAME, "description=", response.getDescription());
        logger.info(METHODNAME, "type=", response.getType());

        return Response.ok().build();
    }

    @GET
    @Path("vsac/{type}/{oid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Map<String, Map<String, Object>> getVsacImportList(
            @QueryParam(CoreRsConstants.QUERYPARMPROPERTY) String property,
            @PathParam("type") final String type,
            @PathParam("oid") final String oid,
            @QueryParam(CoreRsConstants.QUERYPARMSESSION) String sessionId)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getVsacImportList ";
        logger.info(METHODNAME, "type=", type);
        logger.info(METHODNAME, "oid=", oid);
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();
        SessionDTO sessionDTO = getSessionDTO(sessionId);

        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        propertyBagDTO.setQueryClass("ByNameScope");
        SystemPropertyDTO systemPropertyDTO = new SystemPropertyDTO();
        systemPropertyDTO.setScope("cds");
        systemPropertyDTO.setName("VSAC_BASE_URI");

        SystemPropertyDTO uriPropertyDTO = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, propertyBagDTO);
        systemPropertyDTO.setName("VSAC_APIKEY");
        SystemPropertyDTO apiKeyPropertyDTO = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, propertyBagDTO);

        String uri = uriPropertyDTO.getValue();
        String apiKey = apiKeyPropertyDTO.getValue();

        List<String> list = new ArrayList<>();
        if ("draft".equalsIgnoreCase(type)) {
            list = VsacUtils.getProfileList(uri, apiKey);
        } else if ("published".equalsIgnoreCase(type)) {
            list = VsacUtils.getVersionList(uri, apiKey, oid);
        } else {
            throw new MtsException("Type not supported: " + type);
        }

        GeneralMGRInterface generalMGR = getGeneralMGR();
        Collections.sort(list);
        for (String item : list) {
            logger.info(METHODNAME, "list item=", item);

            PropertyBagDTO vcPropertyBagDTO = new PropertyBagDTO();
            vcPropertyBagDTO.setQueryClass("ByOid");

            //Determine if the value set already exists (by oid and version)...
            ValueSetDTO queryDTO = new ValueSetDTO();
            queryDTO.setOid(oid);
            List<ValueSetDTO> results = generalMGR.findByQueryList(queryDTO, sessionDTO, vcPropertyBagDTO);
            logger.info(METHODNAME, "results=", results);

            ValueSetDTO existingValueSet = VsacUtils.getExistingValueSetFromList(results, item, type);

            if (existingValueSet != null) {
                Map<String, Object> itemResult = new HashMap<>();
                itemResult.put("exists", true);
                String existingType = existingValueSet.getVersionStatus().toLowerCase();
                if (!"DRAFT".equalsIgnoreCase(existingType) && !"PUBLISHED".equalsIgnoreCase(existingType)) {
                    if ("ACTIVE".equalsIgnoreCase(existingType) && "N/A".equalsIgnoreCase(existingValueSet.getVersion())) {
                        existingType = "draft";
                    } else {
                        existingType = "published";
                    }
                }
                itemResult.put("type", existingType);
                result.put(item, itemResult);
            } else {
                Map<String, Object> itemResult = new HashMap<>();
                itemResult.put("exists", false);
                itemResult.put("type", null);
                result.put(item, itemResult);
            }
        }
        logger.info(METHODNAME, "property=", property);
        logger.info(METHODNAME, "type=", type);
        logger.info(METHODNAME, "oid=", oid);
        logger.info(METHODNAME, "sessionId=", sessionId);
        logger.info(METHODNAME, "Value Set Profiles: " + (list != null ? list.size() : "null"));
        logger.info(METHODNAME, "result: " + result);
        return result;
    }
}
