/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.syntaxapicallsgen.parser;

import io.ballerina.compiler.syntax.tree.FunctionBodyBlockNode;
import io.ballerina.compiler.syntax.tree.FunctionDefinitionNode;
import io.ballerina.compiler.syntax.tree.ModuleMemberDeclarationNode;
import io.ballerina.compiler.syntax.tree.ModulePartNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeFactory;
import io.ballerina.compiler.syntax.tree.NodeList;
import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.tools.text.TextDocument;
import io.ballerina.tools.text.TextDocuments;

/**
 * Parser that parses as a statement.
 *
 * @since 2.0.0
 */
public class StatementParser extends SyntaxApiCallsGenParser {
    public StatementParser(long timeoutMs) {
        super(timeoutMs);
    }

    @Override
    public Node parse(String source) {
        String sourceCode = String.format("function main(){%s}", source);
        TextDocument document = TextDocuments.from(sourceCode);
        SyntaxTree tree = getSyntaxTree(document);
        ModulePartNode node = tree.rootNode();
        NodeList<ModuleMemberDeclarationNode> moduleDclns = node.members();
        assertIf(!moduleDclns.isEmpty(), "not a valid statement: " +
                "expected at least one member");
        ModuleMemberDeclarationNode moduleDeclaration = moduleDclns.get(0);
        FunctionDefinitionNode mainFunction = (FunctionDefinitionNode) moduleDeclaration;
        FunctionBodyBlockNode mainFunctionBody = (FunctionBodyBlockNode) mainFunction.functionBody();
        return NodeFactory.createBlockStatementNode(mainFunctionBody.openBraceToken(),
                mainFunctionBody.statements(), mainFunctionBody.closeBraceToken());
    }
}
