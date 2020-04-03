package org.cdsframework.rs.cds;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.cdsframework.rs.support.CoreRsConstants;
import org.cdsframework.util.LogUtils;

/**
 *
 * @author sdn
 */
@Path(CoreRsConstants.GENERAL_RS_ROOT)
public class HealthCheckService {

    private final static LogUtils logger = LogUtils.getLogger(HealthCheckService.class);

    @GET
    @Path("healthcheck")
    @Produces(MediaType.APPLICATION_JSON)
    public HealthCheck healthCheck() {
        final String METHODNAME = "healthcheck ";
        logger.info(METHODNAME, "called!");
        return new HealthCheck(200, "all's good!");
    }

    @GET
    @Path("_k8s-health-check")
    @Produces(MediaType.APPLICATION_JSON)
    public Response k8sHealthCheck() {
        final String METHODNAME = "k8sHealthCheck ";
        logger.info(METHODNAME, "called!");
        return Response.ok().build();
    }
}
