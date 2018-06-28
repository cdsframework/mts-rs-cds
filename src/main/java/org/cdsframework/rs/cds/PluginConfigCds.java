/**
 * CAT CDS support plugin project.
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
 * ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING, BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR
 * OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see
 * https://www.hln.com/services/open-source/ or send correspondence to
 * ice@hln.com.
 */
package org.cdsframework.rs.cds;

import org.cdsframework.rs.core.support.CatResourceConfig;
import org.cdsframework.dto.CdsBusinessScopeDTO;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.CdsCodeSystemDTO;
import org.cdsframework.dto.CdsListDTO;
import org.cdsframework.dto.CdsListItemDTO;
import org.cdsframework.dto.CdsListVersionRelDTO;
import org.cdsframework.dto.CdsVersionConceptDeterminationMethodRelDTO;
import org.cdsframework.dto.CdsVersionDTO;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.CriteriaDTO;
import org.cdsframework.dto.CriteriaDataTemplateRelDTO;
import org.cdsframework.dto.CriteriaDataTemplateRelNodeDTO;
import org.cdsframework.dto.CriteriaPredicateDTO;
import org.cdsframework.dto.CriteriaPredicatePartConceptDTO;
import org.cdsframework.dto.CriteriaPredicatePartDTO;
import org.cdsframework.dto.CriteriaPredicatePartRelDTO;
import org.cdsframework.dto.CriteriaResourceDTO;
import org.cdsframework.dto.CriteriaResourceParamDTO;
import org.cdsframework.dto.CriteriaVersionRelDTO;
import org.cdsframework.dto.DataModelClassDTO;
import org.cdsframework.dto.DataModelClassNodeDTO;
import org.cdsframework.dto.DataModelDTO;
import org.cdsframework.dto.DataTemplateDTO;
import org.cdsframework.dto.DataTemplateLinkRelDTO;
import org.cdsframework.dto.DataTemplateNodeRelDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.dto.OpenCdsConceptDeploymentLogDTO;
import org.cdsframework.dto.OpenCdsConceptRelDTO;
import org.cdsframework.dto.TestCaseDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.dto.ValueSetSubValueSetRelDTO;
import org.cdsframework.rs.base.BasePluginConfig;

/**
 *
 * @author sdn
 */
public class PluginConfigCds extends BasePluginConfig {

    public PluginConfigCds(String baseCrudUri) {
        super(baseCrudUri);
        registerConfig(CdsCodeDTO.class, new CatResourceConfig(CdsCodeDTO.class) {

            @Override
            public void initialize() {
                setBaseHeader("Code");
                addDataTableColumn("code");
                addDataTableColumn("displayName");
                addDataTableColumn("lastModDatetime");
            }
        });

        registerConfig(CdsCodeSystemDTO.class, new CatResourceConfig(CdsCodeSystemDTO.class) {

            @Override
            public void initialize() {
                setBaseHeader("Code System");
                addDataTableColumn("oid", "OID");
                addDataTableColumn("name");
                addDataTableColumn("lastModDatetime");
            }
        });

        registerConfig(CdsBusinessScopeDTO.class, new CatResourceConfig(CdsBusinessScopeDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Knowledge Module");
                addDataTableColumn("businessId");
                addDataTableColumn("scopingEntityId");
                addDataTableColumn("status");
                addDataTableColumn("lastModDatetime");
            }
        });

        registerConfig(CdsVersionDTO.class, new CatResourceConfig(CdsVersionDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Knowledge Module Version");
                addDataTableColumn("name");
                addDataTableColumn("version");
                addDataTableColumn("status");
                addDataTableColumn("lastModDatetime");
            }
        });

        registerConfig(CdsVersionConceptDeterminationMethodRelDTO.class, new CatResourceConfig(CdsVersionConceptDeterminationMethodRelDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Version Concept Determination Method Relationships");
                addDataTableColumn("conceptDeterminationMethodDTO");
                addDataTableColumn("lastModDatetime");
            }
        });

        registerConfig(ValueSetDTO.class, new CatResourceConfig(ValueSetDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Value Set");
                addDataTableColumn("name");
                addDataTableColumn("code");
                addDataTableColumn("oid", "OID");
                addDataTableColumn("version");
                addDataTableColumn("lastModDatetime");
            }
        });

        registerConfig(ValueSetSubValueSetRelDTO.class, new CatResourceConfig(ValueSetSubValueSetRelDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Sub-value Set");
                addDataTableColumn("subValueSetDTO");
            }
        });

        registerConfig(ValueSetCdsCodeRelDTO.class, new CatResourceConfig(ValueSetCdsCodeRelDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Code System Code Relationship");
            }
        });

        registerConfig(CdsListDTO.class, new CatResourceConfig(CdsListDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CdsListItemDTO.class, new CatResourceConfig(CdsListItemDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CdsListVersionRelDTO.class, new CatResourceConfig(CdsListVersionRelDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(ConceptDeterminationMethodDTO.class, new CatResourceConfig(ConceptDeterminationMethodDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaDTO.class, new CatResourceConfig(CriteriaDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaDataTemplateRelDTO.class, new CatResourceConfig(CriteriaDataTemplateRelDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaDataTemplateRelNodeDTO.class, new CatResourceConfig(CriteriaDataTemplateRelNodeDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaPredicateDTO.class, new CatResourceConfig(CriteriaPredicateDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaPredicatePartConceptDTO.class, new CatResourceConfig(CriteriaPredicatePartConceptDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaPredicatePartDTO.class, new CatResourceConfig(CriteriaPredicatePartDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaPredicatePartRelDTO.class, new CatResourceConfig(CriteriaPredicatePartRelDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaResourceDTO.class, new CatResourceConfig(CriteriaResourceDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaResourceParamDTO.class, new CatResourceConfig(CriteriaResourceParamDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(CriteriaVersionRelDTO.class, new CatResourceConfig(CriteriaVersionRelDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(DataModelClassDTO.class, new CatResourceConfig(DataModelClassDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(DataModelClassNodeDTO.class, new CatResourceConfig(DataModelClassNodeDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(DataModelDTO.class, new CatResourceConfig(DataModelDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(DataTemplateDTO.class, new CatResourceConfig(DataTemplateDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(DataTemplateLinkRelDTO.class, new CatResourceConfig(DataTemplateLinkRelDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(DataTemplateNodeRelDTO.class, new CatResourceConfig(DataTemplateNodeRelDTO.class) {
            @Override
            public void initialize() {
            }
        });

        registerConfig(OpenCdsConceptDTO.class, new CatResourceConfig(OpenCdsConceptDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Concept");
                addDataTableColumn("code");
                addDataTableColumn("displayName", "Name");
                addDataTableColumn("lastDeployedDate", "Last Deployed");
            }
        });

        registerConfig(OpenCdsConceptRelDTO.class, new CatResourceConfig(OpenCdsConceptRelDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Concept Mapping");
                addDataTableColumn("oid", "OID");
                addDataTableColumn("name");
                addDataTableColumn("cdsCodeDTO", "Code");
                addDataTableColumn("mappingType", "Type");
                addDataTableColumn("lastDeployedDate", "Last Deployed");
            }
        });

        registerConfig(OpenCdsConceptDeploymentLogDTO.class, new CatResourceConfig(OpenCdsConceptDeploymentLogDTO.class) {
            @Override
            public void initialize() {
                setBaseHeader("Concept Deployment Log");
                addDataTableColumn("deploymentAction", "Action");
                addDataTableColumn("deploymentEnvironment", "Environment");
                addDataTableColumn("lastModDatetime", "Deployed");
            }
        });

        registerConfig(TestCaseDTO.class, new CatResourceConfig(TestCaseDTO.class) {
            @Override
            public void initialize() {
            }
        });

    }

}
