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

package io.ballerina.shell.invoker.classload;

import io.ballerina.compiler.syntax.tree.ImportDeclarationNode;
import io.ballerina.compiler.syntax.tree.ModulePartNode;
import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.shell.snippet.types.ImportDeclarationSnippet;
import io.ballerina.shell.utils.QuotedIdentifier;
import io.ballerina.tools.text.TextDocument;
import io.ballerina.tools.text.TextDocuments;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Imports that were stored to be able to search with the prefix.
 * The prefixes used will always be quoted identifiers.
 *
 * @since 2.0.0
 */
public class HashedImports {
    private static final QuotedIdentifier ANON_SOURCE = new QuotedIdentifier("$");
    private static final Collection<QuotedIdentifier> ANON_IMPORT_PREFIXES = List.of(new QuotedIdentifier("java"));
    private static final String JAVA_IMPORT_SOURCE = "import ballerina/jballerina.java;";
    private static final ImportDeclarationSnippet JAVA_IMPORT;

    static {
        // Set the java import snippet
        TextDocument importText = TextDocuments.from(JAVA_IMPORT_SOURCE);
        SyntaxTree syntaxTree = SyntaxTree.from(importText);
        assert syntaxTree.rootNode() instanceof ModulePartNode;
        ModulePartNode modulePartNode = syntaxTree.rootNode();
        ImportDeclarationNode importDeclaration = modulePartNode.imports().get(0);
        JAVA_IMPORT = new ImportDeclarationSnippet(importDeclaration);
    }

    /**
     * This is a map of import prefix to the import statement used.
     * Import prefix must be a quoted identifier.
     */
    private final HashMap<QuotedIdentifier, String> imports;
    /**
     * Reverse map to search the imported module.
     */
    private final HashMap<String, QuotedIdentifier> reverseImports;
    /**
     * Import prefixes that are used in each module declaration/var declaration.
     * Key is the name of the module/var declaration. (quoted name)
     * Value is the prefixes used by that name. (quoted prefix)
     * All the implicit imports should be included under ANON_SOURCE name.
     */
    private final Map<QuotedIdentifier, Set<QuotedIdentifier>> usedPrefixes;

    public HashedImports() {
        this.imports = new HashMap<>();
        this.reverseImports = new HashMap<>();
        this.usedPrefixes = new HashMap<>();
        storeImport(JAVA_IMPORT);
        storeAnonImplicitPrefixes(ANON_IMPORT_PREFIXES);
    }

    /**
     * Clear the memory of previous imports and reset
     * to original state.
     */
    public void reset() {
        this.imports.clear();
        this.reverseImports.clear();
        this.usedPrefixes.clear();
        storeImport(JAVA_IMPORT);
        storeAnonImplicitPrefixes(ANON_IMPORT_PREFIXES);
    }

    /**
     * Get the import statement of the given prefix.
     *
     * @param prefix Prefix to search.
     * @return The import statement of the prefix.
     */
    public String getImport(QuotedIdentifier prefix) {
        String moduleName = this.imports.get(prefix);
        if (moduleName == null) {
            return null;
        }
        return String.format("import %s as %s;", moduleName, prefix);
    }

    /**
     * Whether the prefix was previously added.
     *
     * @param prefix Prefix to search.
     * @return If prefix was added.
     */
    public boolean containsPrefix(QuotedIdentifier prefix) {
        return this.imports.containsKey(prefix);
    }

    /**
     * Whether the module was imported before.
     * If yes, then this import does not need to be checked again.
     *
     * @param moduleName Module name to check in 'orgName/module' format.
     * @return If module was added.
     */
    public boolean moduleImported(String moduleName) {
        return this.reverseImports.containsKey(moduleName);
    }

    /**
     * Get the prefix this module name was imported as.
     *
     * @param moduleName Module name to check in 'orgName/module' format.
     * @return Prefix of the import.
     */
    public QuotedIdentifier prefix(String moduleName) {
        return this.reverseImports.get(moduleName);
    }

    /**
     * Add the prefix and import to the set of remembered imports.
     *
     * @param snippet Import snippet to add.
     * @return The prefix the import was added as.
     */
    public QuotedIdentifier storeImport(ImportDeclarationSnippet snippet) {
        QuotedIdentifier quotedPrefix = snippet.getPrefix();
        String importedModule = snippet.getImportedModule();
        return storeImport(quotedPrefix, importedModule);
    }

    /**
     * Add the prefix and import to the set of remembered imports.
     *
     * @param quotedPrefix Prefix of import.
     * @param moduleName   Module name to add.
     * @return The prefix the import was added as.
     */
    public QuotedIdentifier storeImport(QuotedIdentifier quotedPrefix, String moduleName) {
        this.imports.put(quotedPrefix, moduleName);
        this.reverseImports.put(moduleName, quotedPrefix);
        return quotedPrefix;
    }

    /**
     * Add prefixes to persisted list of imports.
     * The name will be linked with the import prefix.
     *
     * @param name     Usage source declaration name of the import.
     * @param prefixes Used prefixes.
     */
    public void storeImportUsages(QuotedIdentifier name, Collection<QuotedIdentifier> prefixes) {
        // Get the prefixes previously used by this name and add prefix this to it.
        Set<QuotedIdentifier> sourcePrefixes = this.usedPrefixes.getOrDefault(name, new HashSet<>());
        sourcePrefixes.addAll(prefixes);
        this.usedPrefixes.put(name, sourcePrefixes);
    }

    /**
     * Add prefixes to persisted list of imports th
     * at originated without a source.
     * Will be added as an import from ANON_SOURCE.
     */
    public void storeAnonImplicitPrefixes(Collection<QuotedIdentifier> prefixes) {
        storeImportUsages(ANON_SOURCE, prefixes);
    }

    /**
     * All the prefixes that were added. Prefixes will be quoted.
     *
     * @return Set of prefixes.
     */
    public Set<QuotedIdentifier> prefixes() {
        return this.imports.keySet();
    }

    /**
     * All the implicit import statements that were remembered.
     *
     * @return Set of implicit import statements.
     */
    public Set<String> getUsedImports() {
        return getUsedImports(List.of(ANON_SOURCE));
    }

    /**
     * All the import statements that were used by the given names.
     *
     * @return Set of import statements used in given names.
     */
    public Set<String> getUsedImports(Collection<QuotedIdentifier> names) {
        Set<QuotedIdentifier> allUsedImportPrefixes = new HashSet<>();
        names.stream()
                .map(this.usedPrefixes::get) // get used prefixes of names
                .filter(Objects::nonNull) // discard null lists
                .forEach(allUsedImportPrefixes::addAll);

        Set<String> importStrings = new HashSet<>();
        allUsedImportPrefixes.stream()
                .map(this::getImport) // get the import statement
                .filter(Objects::nonNull) // discard null imports
                .forEach(importStrings::add);

        return importStrings;
    }
}
