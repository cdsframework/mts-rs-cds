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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.SystemPropertyDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.ejb.local.GeneralMGRInterface;
import org.cdsframework.enumeration.DeploymentEnvironment;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.rs.GeneralRSService;
import org.cdsframework.rs.support.CoreRsConstants;
import org.cdsframework.util.ConceptUtils;
import org.cdsframework.util.VsacUtils;

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
        systemPropertyDTO.setName("VSAC_USERNAME");
        SystemPropertyDTO usernamePropertyDTO = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, propertyBagDTO);
        systemPropertyDTO.setName("VSAC_PASSWORD");
        SystemPropertyDTO passwordPropertyDTO = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, propertyBagDTO);

        String uri = uriPropertyDTO.getValue();
        String username = usernamePropertyDTO.getValue();
        String password = passwordPropertyDTO.getValue();

        List<String> list = new ArrayList<>();
        if ("draft".equalsIgnoreCase(type)) {
            list = VsacUtils.getProfileList(uri, username, password);
        } else if ("published".equalsIgnoreCase(type)) {
            list = VsacUtils.getVersionList(uri, username, password, oid);
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

    @GET
    @Path("concepts/predefaultdeploy/{environment}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response predeployDefaultConceptDeterminationMethod(@HeaderParam("accept") String accept,
            @PathParam("environment") final String environment,
            @QueryParam(CoreRsConstants.QUERYPARMSESSION) final String sessionId) {
        try {
            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setSessionId(sessionId);

            String code = ConceptUtils.getDefaultCdmCode(sessionDTO);
            String codeSystem = ConceptUtils.getDefaultCdmCodeSystem(sessionDTO);

            Response result = ConceptUtils.preDeployCdm(accept, environment, codeSystem, code, sessionId);
            return result;
        } catch (MtsException | ValidationException | NotFoundException | AuthenticationException
                | AuthorizationException | RuntimeException e) {
            logger.error(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("concepts/predeploy/{environment}/{codeSystem}/{code}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response preDeployConceptDeterminationMethod(@HeaderParam("accept") String accept,
            @PathParam("environment") final String environment,
            @PathParam("codeSystem") final String codeSystem, @PathParam("code") final String code,
            @QueryParam(CoreRsConstants.QUERYPARMSESSION) final String sessionId) {
        try {
            Response result = ConceptUtils.preDeployCdm(accept, environment, codeSystem, code, sessionId);
            return result;
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("concepts/{environment}/{codeSystem}/{code}")
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    public Response getCdm(@PathParam("environment") final String environment, @PathParam("codeSystem") final String codeSystem,
            @PathParam("code") final String code, @QueryParam(CoreRsConstants.QUERYPARMSESSION) final String sessionId) {

        final String METHODNAME = "getCdm ";

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(sessionId);

        try {
            DeploymentEnvironment deploymentEnvironment = ConceptUtils.checkConceptInput(environment, codeSystem, code, sessionId,
                    METHODNAME);
            Map<String, byte[]> exportData = ConceptUtils.getExportData(codeSystem, code, deploymentEnvironment, sessionDTO);

            String xml = new String(exportData.get("cdm.xml"));

            logger.debug(METHODNAME, "xml=", xml);

            return Response.ok(xml, MediaType.APPLICATION_XML).build();

        } catch (MtsException | ValidationException | NotFoundException | AuthenticationException
                | AuthorizationException | RuntimeException e) {
            logger.error(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}
