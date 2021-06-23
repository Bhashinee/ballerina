/*
 * Copyright (c) 2019, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballerinalang.langserver.codeaction.providers;

import io.ballerina.compiler.syntax.tree.SyntaxKind;
import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.tools.diagnostics.Diagnostic;
import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.langserver.codeaction.CodeActionUtil;
import org.ballerinalang.langserver.common.ImportsAcceptor;
import org.ballerinalang.langserver.common.utils.CommonUtil;
import org.ballerinalang.langserver.commons.CodeActionContext;
import org.ballerinalang.langserver.commons.codeaction.CodeActionNodeType;
import org.ballerinalang.langserver.commons.codeaction.spi.DiagBasedPositionDetails;
import org.ballerinalang.langserver.commons.codeaction.spi.NodeBasedPositionDetails;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.TextEdit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.ballerinalang.langserver.codeaction.CodeActionUtil.computePositionDetails;

/**
 * Code Action provider for implementing all the functions of an object.
 *
 * @since 1.2.0
 */
@JavaSPIService("org.ballerinalang.langserver.commons.codeaction.spi.LSCodeActionProvider")
public class ImplementAllCodeAction extends AbstractCodeActionProvider {

    public static final String NAME = "Implement All";

    public ImplementAllCodeAction() {
        super(Arrays.asList(CodeActionNodeType.CLASS,
                CodeActionNodeType.SERVICE,
                CodeActionNodeType.CLASS_FUNCTION,
                CodeActionNodeType.MODULE_VARIABLE,
                CodeActionNodeType.LOCAL_VARIABLE));
    }

    @Override
    public List<CodeAction> getNodeBasedCodeActions(CodeActionContext context,
                                                    NodeBasedPositionDetails posDetails) {

        if (posDetails.matchedTopLevelNode().kind() != SyntaxKind.CLASS_DEFINITION
                && posDetails.matchedTopLevelNode().kind() != SyntaxKind.OBJECT_METHOD_DEFINITION
                && posDetails.matchedTopLevelNode().kind() != SyntaxKind.MODULE_VAR_DECL
                && posDetails.matchedTopLevelNode().kind() != SyntaxKind.LOCAL_VAR_DECL
                && posDetails.matchedTopLevelNode().kind() != SyntaxKind.SERVICE_DECLARATION) {
            return Collections.emptyList();
        }

        List<Diagnostic> diags = context.diagnostics(context.filePath()).stream()
                .filter(diag -> CommonUtil
                        .isWithinRange(context.cursorPosition(), CommonUtil.toRange(diag.location().lineRange()))
                )
                .filter(diagnostic -> DIAGNOSTICCODES.contains(diagnostic.diagnosticInfo().code()))
                .collect(Collectors.toList());

        SyntaxTree syntaxTree = context.workspace().syntaxTree(context.filePath()).orElseThrow();
        ImportsAcceptor importsAcceptor = new ImportsAcceptor(context);
        List<TextEdit> edits = new ArrayList<>(importsAcceptor.getNewImportTextEdits());

        diags.forEach(diagnostic -> {
            DiagBasedPositionDetails positionDetails = computePositionDetails(syntaxTree, diagnostic, context);
            edits.addAll(getDiagBasedTextEdits(diagnostic, positionDetails, context));
        });

        String commandTitle = "Implement all";
        CodeAction quickFixCodeAction = createQuickFixCodeAction(commandTitle, edits, context.fileUri());
        quickFixCodeAction.setDiagnostics(CodeActionUtil.toDiagnostics(diags));
        return Collections.singletonList(quickFixCodeAction);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
