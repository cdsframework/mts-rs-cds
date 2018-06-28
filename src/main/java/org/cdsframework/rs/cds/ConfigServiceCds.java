/**
 * CAT CDS support plugin project.
 *
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
 * For more information about this software, see https://www.hln.com/services/open-source/ or send
 * correspondence to ice@hln.com.
 */
package org.cdsframework.rs.cds;

import java.util.List;
import org.cdsframework.rs.core.support.CatResourceConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.cdsframework.rs.base.BaseConfigService;
import org.cdsframework.rs.core.support.ConfigServiceConstantsCore;
import org.cdsframework.rs.support.ConfigServiceConstantsCds;

/**
 *
 * @author sdn
 */
@Path(ConfigServiceConstantsCore.BASE_RS_ROOT + "/" + ConfigServiceConstantsCds.CDS_RS_ROOT)
public class ConfigServiceCds extends BaseConfigService {

    public ConfigServiceCds() {
        super(PluginConfigCds.class, ConfigServiceConstantsCds.CDS_RS_ROOT);
    }

    @GET
    @Path("{resource}")
    @Produces(MediaType.APPLICATION_JSON)
    public CatResourceConfig config(@PathParam("resource") final String resource) {
        return configMain(resource);
    }

    @GET
    @Path("configs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getConfigs() {
        return getConfigsMain();
    }

}
