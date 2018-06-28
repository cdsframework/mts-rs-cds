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

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.SystemPropertyDTO;
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
import org.cdsframework.rs.util.PropertyBagUtils;
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
    public List<String> getVsacImportList(
            @QueryParam(CoreRsConstants.QUERYPARMPROPERTY) String property,
            @PathParam("type") final String type,
            @PathParam("oid") final String oid,
            @QueryParam(CoreRsConstants.QUERYPARMSESSION) String sessionId)
            throws MtsException, ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        final String METHODNAME = "getVsacImportList ";
        PropertyBagDTO propertyBagDTO = PropertyBagUtils.getJsonPropertyBagDTO(property);
        SessionDTO sessionDTO = getSessionDTO(sessionId);

        PropertyBagDTO systemPropertyBagDTO = new PropertyBagDTO();
        systemPropertyBagDTO.setQueryClass("ByNameScope");
        SystemPropertyDTO systemPropertyDTO = new SystemPropertyDTO();
        systemPropertyDTO.setScope("cds");
        systemPropertyDTO.setName("VSAC_BASE_URI");

        SystemPropertyDTO uriPropertyDTO = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, systemPropertyBagDTO);
        systemPropertyDTO.setName("VSAC_USERNAME");
        SystemPropertyDTO usernamePropertyDTO = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, systemPropertyBagDTO);
        systemPropertyDTO.setName("VSAC_PASSWORD");
        SystemPropertyDTO passwordPropertyDTO = getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO, systemPropertyBagDTO);

        String uri = uriPropertyDTO.getValue();
        String username = usernamePropertyDTO.getValue();
        String password = passwordPropertyDTO.getValue();

        List<String> list = new ArrayList<String>();
        if ("draft".equalsIgnoreCase(type)) {
            list = VsacUtils.getProfileList(uri, username, password);
        } else {
            list = VsacUtils.getVersionList(uri, username, password, oid);
        }
        logger.info(METHODNAME, "Value Set Profiles: " + (list != null ? list.size() : "null"));
        Collections.sort(list);
        return list;
    }
}
