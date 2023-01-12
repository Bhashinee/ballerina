/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.langserver.completion;

import org.ballerinalang.langserver.commons.workspace.WorkspaceDocumentException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Expression Context tests.
 *
 * @since 2.0.0
 */
public class ExpressionContextTest extends CompletionTest {

    @Test(dataProvider = "completion-data-provider")
    @Override
    public void test(String config, String configPath) throws WorkspaceDocumentException, IOException {
        super.test(config, configPath);
    }

    @DataProvider(name = "completion-data-provider")
    @Override
    public Object[][] dataProvider() {
        return this.getConfigsList();
    }

    @Override
    public String getTestResourceDir() {
        return "expression_context";
    }

    @Override
    public List<String> skipList() {
        return Arrays.asList(
                "object_constructor_expr_ctx_config12a.json",
                "object_constructor_expr_ctx_config6.json", // LS fix needed
                "object_constructor_expr_ctx_config11.json", // LS fix needed
                "conditional_expr_ctx_config12.json", //#34145
                
                // TODO ContextTypeResolver's context type for method call expressions should be revisited
                "method_call_expression_ctx_config9.json",

                // expectedType cases
                "mapping_constructor_expr_ctx_config3.json",
                "new_expr_ctx_config15.json",
                "new_expr_ctx_config11.json",
                "new_expr_ctx_config16.json",
                "new_expr_ctx_config26.json",
                "new_expr_ctx_config14.json",
                "mapping_expr_ctx_config38_negative.json",
                "anon_func_expr_ctx_config1a.json",

                "new_expr_ctx_config13.json",
                "new_expr_ctx_config24.json",
                "function_call_expression_ctx_config15.json",
                "function_call_expression_ctx_config13.json",
                "mapping_expr_ctx_config32c.json",
                "mapping_constructor_expr_ctx_config3.json",

                // var declaration cases
                "mapping_expr_ctx_config5.json",
                "mapping_expr_ctx_config4.json",
                "mapping_expr_ctx_config8.json",
                "mapping_expr_ctx_config6.json",
                "mapping_expr_ctx_config2.json",
                "mapping_expr_ctx_config1.json",
                "mapping_expr_ctx_config3.json",
                "mapping_expr_ctx_config9.json",

                "anon_func_expr_ctx_config1b.json",
                "object_constructor_expr_ctx_config1.json",

                // init fix cases

                "mapping_expr_ctx_config32b.json"

                );
    }
}
