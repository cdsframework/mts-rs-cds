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
import java.util.List;
import javax.ws.rs.ApplicationPath;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.dto.CdsBusinessScopeDTO;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeOpenCdsConceptRelDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.CdsListItemDTO;
import org.cdsframework.dto.CdsListVersionRelDTO;
import org.cdsframework.dto.CdsVersionConceptDeterminationMethodRelDTO;
import org.cdsframework.dto.CdsVersionDTO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.CriteriaDTO;
import org.cdsframework.dto.CriteriaPredicateDTO;
import org.cdsframework.dto.CriteriaPredicatePartRelDTO;
import org.cdsframework.dto.CriteriaResourceDTO;
import org.cdsframework.dto.CriteriaResourceParamDTO;
import org.cdsframework.dto.CriteriaVersionRelDTO;
import org.cdsframework.dto.DataInputNodeDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptDeploymentLogDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.TestCaseDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.dto.ValueSetSubValueSetRelDTO;
import org.cdsframework.rs.base.BaseResourceConfig;
import org.cdsframework.rs.provider.CoreJacksonJsonProvider;
import org.cdsframework.rs.support.CoreRsConstants;

/**
 *
 * @author HLN Consulting, LLC
 */
@ApplicationPath(CoreRsConstants.RESOURCE_ROOT)
public class ApplicationConfig extends BaseResourceConfig {    
    
    public ApplicationConfig() {
        super();
        
        // Create a list of the DTO's that need to be deserialized
        List<Class<? extends BaseDTO>> dtoClasses = new ArrayList<Class<? extends BaseDTO>>();
        dtoClasses.add(CdsBusinessScopeDTO.class);
        dtoClasses.add(CdsCodeDTO.class);
        dtoClasses.add(CdsCodeOpenCdsConceptRelDTO.class);
        dtoClasses.add(CdsCodeSystemDTO.class);
        dtoClasses.add(CdsListDTO.class);
        dtoClasses.add(CdsListItemDTO.class);
        dtoClasses.add(CdsListVersionRelDTO.class);
        dtoClasses.add(CdsVersionConceptDeterminationMethodRelDTO.class);
        dtoClasses.add(CdsVersionDTO.class);
        dtoClasses.add(ConceptDeterminationMethodDTO.class);
        dtoClasses.add(CriteriaDTO.class);
        dtoClasses.add(CriteriaPredicateDTO.class);
        dtoClasses.add(CriteriaPredicatePartRelDTO.class);
        dtoClasses.add(CriteriaResourceDTO.class);
        dtoClasses.add(CriteriaResourceParamDTO.class);
        dtoClasses.add(CriteriaVersionRelDTO.class);
        dtoClasses.add(DataInputNodeDTO.class);
        dtoClasses.add(OpenCdsConceptDTO.class);
        dtoClasses.add(OpenCdsConceptRelDTO.class);
        dtoClasses.add(OpenCdsConceptDeploymentLogDTO.class);
        dtoClasses.add(TestCaseDTO.class);
        dtoClasses.add(ValueSetCdsCodeRelDTO.class);
        dtoClasses.add(ValueSetDTO.class);
        dtoClasses.add(ValueSetSubValueSetRelDTO.class);

        CoreJacksonJsonProvider coreJacksonJsonProvider = new CoreJacksonJsonProvider();
        coreJacksonJsonProvider.registerDTOs(dtoClasses);
        register(coreJacksonJsonProvider);
        register(org.cdsframework.rs.cds.CdsRSService.class); 
        register(ConfigServiceCds.class);
        register(HealthCheckService.class);
    }

    
}
