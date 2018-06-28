/**
 * The MTS support core project contains client related utilities, data transfer objects and remote EJB interfaces for communication with the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for more details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the
 * New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * to have (without restriction, limitation, and warranty) complete irrevocable
 * access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; THE SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see
 * https://www.hln.com/services/open-source/ or send correspondence to
 * ice@hln.com.
 */
package org.cdsframework.rs.support;

import java.util.Properties;
import org.cdsframework.rckms.data.upload.service.uploader.OpenCDSConfigUploader;
import org.cdsframework.rckms.data.upload.service.uploader.OpenCDSConfigUploaderImpl;
import org.cdsframework.util.LogUtils;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsConfiguration {

    private static final LogUtils logger = LogUtils.getLogger(CdsConfiguration.class);
    private static final Properties INSTANCE_PROPERTIES;
    private static final String OPENCDS_REST_HOST;
    private static final int OPENCDS_REST_PORT;
    private static final String OPENCDS_REST_USERNAME;
    private static final String OPENCDS_REST_PASSWORD;
    private static final String OPENCDS_REST_CDM_ENDPOINT;
    private static final String OPENCDS_REST_KM_ENDPOINT;
    private static final String OPENCDS_REST_HOST_TEST;
    private static final int OPENCDS_REST_PORT_TEST;
    private static final String OPENCDS_REST_USERNAME_TEST;
    private static final String OPENCDS_REST_PASSWORD_TEST;

    static {
        final String METHODNAME = "loadProperties ";
        INSTANCE_PROPERTIES = CoreConfiguration.getInstanceProperties();
        OPENCDS_REST_HOST = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_HOST");
        OPENCDS_REST_PORT = Integer.parseInt(INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_PORT"));
        OPENCDS_REST_USERNAME = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_USERNAME");
        OPENCDS_REST_PASSWORD = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_PASSWORD");
        OPENCDS_REST_CDM_ENDPOINT = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_CDM_ENDPOINT", "");
        OPENCDS_REST_KM_ENDPOINT = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_KM_ENDPOINT", "");

        OPENCDS_REST_HOST_TEST = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_HOST_TEST");
        OPENCDS_REST_PORT_TEST = Integer.parseInt(INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_PORT_TEST"));
        OPENCDS_REST_USERNAME_TEST = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_USERNAME_TEST");
        OPENCDS_REST_PASSWORD_TEST = INSTANCE_PROPERTIES.getProperty("OPENCDS_REST_PASSWORD_TEST");

        logger.info(METHODNAME, "OPENCDS_REST_HOST=", OPENCDS_REST_HOST);
        logger.info(METHODNAME, "OPENCDS_REST_PORT=", OPENCDS_REST_PORT);
        logger.info(METHODNAME, "OPENCDS_REST_USERNAME=", OPENCDS_REST_USERNAME);
//        logger.info(METHODNAME, "OPENCDS_REST_PASSWORD=", OPENCDS_REST_PASSWORD);
        logger.info(METHODNAME, "OPENCDS_REST_CDM_ENDPOINT=", OPENCDS_REST_CDM_ENDPOINT);
        logger.info(METHODNAME, "OPENCDS_REST_KM_ENDPOINT=", OPENCDS_REST_KM_ENDPOINT);

        logger.info(METHODNAME, "OPENCDS_REST_HOST_TEST=", OPENCDS_REST_HOST_TEST);
        logger.info(METHODNAME, "OPENCDS_REST_PORT_TEST=", OPENCDS_REST_PORT_TEST);
        logger.info(METHODNAME, "OPENCDS_REST_USERNAME_TEST=", OPENCDS_REST_USERNAME_TEST);
//        logger.info(METHODNAME, "OPENCDS_REST_PASSWORD_TEST=", OPENCDS_REST_PASSWORD_TEST);

    }

    public static String getOpenCdsRestHost() {
        return OPENCDS_REST_HOST;
    }

    public static int getOpenCdsRestPort() {
        return OPENCDS_REST_PORT;
    }

    public static String getOpenCdsRestUserName() {
        return OPENCDS_REST_USERNAME;
    }

    public static String getOpenCdsRestPassword() {
        return OPENCDS_REST_PASSWORD;
    }

    public static String getOpenCdsRestCdmEndpoint() {
        return OPENCDS_REST_CDM_ENDPOINT;
    }

    public static String getOpenCdsRestKmEndpoint() {
        return OPENCDS_REST_KM_ENDPOINT;
    }

    public static String getOpenCdsRestHostTest() {
        return OPENCDS_REST_HOST_TEST;
    }

    public static int getOpenCdsRestPortTest() {
        return OPENCDS_REST_PORT_TEST;
    }

    public static String getOpenCdsRestUserNameTest() {
        return OPENCDS_REST_USERNAME_TEST;
    }

    public static String getOpenCdsRestPasswordTest() {
        return OPENCDS_REST_PASSWORD_TEST;
    }

    public static OpenCDSConfigUploader getOpenCDSConfigUploader() {
        OpenCDSConfigUploader openCDSConfigUploader = new OpenCDSConfigUploaderImpl(
                getOpenCdsRestHost(),
                getOpenCdsRestPort(),
                getOpenCdsRestUserName(),
                getOpenCdsRestPassword(),
                getOpenCdsRestCdmEndpoint(),
                getOpenCdsRestKmEndpoint());
        return openCDSConfigUploader;
    }

    public static OpenCDSConfigUploader getOpenCDSConfigUploaderTest() {
        OpenCDSConfigUploader openCDSConfigUploader = new OpenCDSConfigUploaderImpl(
                getOpenCdsRestHostTest(),
                getOpenCdsRestPortTest(),
                getOpenCdsRestUserNameTest(),
                getOpenCdsRestPasswordTest(),
                getOpenCdsRestCdmEndpoint(),
                getOpenCdsRestKmEndpoint());
        return openCDSConfigUploader;
    }

}
