// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/test;

# Execute tests to verify the string diff error messages during assert equality checks

@test:Config {}
function testAssertIntValues() {
    error? err = trap test:assertEquals(124, 123);
    error result = <error>err;
    test:assertEquals(result.message().toString(), "Assertion Failed!\nexpected: '123'\nactual\t: '124'\n\nDiff\t:\n\n\n--- expected\n+++ actual\n@@ -1,3 +1,3 @@\n\n12\n-4\n+3\n\n\n");
}

@test:Config {}
function testAssertDecimalValues() {
    decimal d = 27.5;
    decimal f = 27.6;
    error? err = trap test:assertEquals(d, f);
    error result = <error>err;
    test:assertEquals(result.message().toString(), "Assertion Failed!\nexpected: '27.6'\nactual\t: '27.5'\n\nDiff\t:\n\n\n--- expected\n+++ actual\n@@ -1,4 +1,4 @@\n\n27.\n-5\n+6\n\n\n");
}

@test:Config {}
function testAssertJsonValues() {
    json bioData = {name:"John Doe", age:25, address:{city:"Colombo", country:"Sri Lanka"}};
    json bioData2 = {name:"John Doe New", age:25, address:{city:"Colombo", country:"Sri Lanka"}};
    error? err = trap test:assertEquals(bioData, bioData2);
    error result = <error>err;
    test:assertEquals(result.message().toString(), "Assertion Failed!\nexpected: '{\"name\":\"John Doe New\",\"age\":25,\"address\":{\"city\":\"Colombo\",\"country\":\"Sri Lanka'\nactual\t: '{\"name\":\"John Doe\",\"age\":25,\"address\":{\"city\":\"Colombo\",\"country\":\"Sri Lanka\"}}'\n\nDiff\t:\n\n\n--- expected\n+++ actual\n@@ -1,79 +1,83 @@\n\n{\"name\":\"John Doe\n+ \n+N\n+e\n+w\n\",\"age\":25,\"address\":{\"city\":\"Colombo\",\"country\":\"Sri Lanka\"}}\n\n");
}

@test:Config {}
function testAssertLongJsonValues() {
    json bioData = {name:"John Doe Old", age:25, designation: "SSE", address:{city:"Colombo", country:"Sri Lankaa"}};
    json bioData2 = {name:"John Doe New", age:25, designation: "SSE", address:{city:"Colombo", country:"Sri Lanka"}};
    error? err = trap test:assertEquals(bioData, bioData2);
    error result = <error>err;
    test:assertEquals(result.message().toString(), "Assertion Failed!\nexpected: '{\"name\":\"John Doe New\",\"age\":25,\"designation\":\"SSE\",\"address\":{\"city\":\"Colombo\",'\nactual\t: '{\"name\":\"John Doe Old\",\"age\":25,\"designation\":\"SSE\",\"address\":{\"city\":\"Colombo\",'\n\nDiff\t:\n\n\n--- expected\n+++ actual\n@@ -1,104 +1,103 @@\n\n{\"name\":\"John Doe \n-O\n-l\n-d\n+N\n+e\n+w\n\",\"age\":25,\"designation\":\"SSE\",\"address\":{\"city\":\"Colombo\",\"country\":\"Sri Lanka\n-a\n\"}}\n\n");
}

@test:Config {}
function testAssertTuples() {
    [int, string] a = [10, "John"];
    [int, string] b = [12, "John"];
    error? err = trap test:assertEquals(a, b);
    error result = <error>err;
    test:assertEquals(result.message().toString(), "Assertion Failed!\nexpected: '12 John'\nactual\t: '10 John'\n\nDiff\t:\n\n\n--- expected\n+++ actual\n@@ -1,7 +1,7 @@\n\n1\n-0\n+2\n John\n\n");
}

@test:Config {}
function testAssertObjects() {
    Person person = new();
    Person person2 = new();
    person.name = "dilhasha";
    error? err = trap test:assertExactEquals(person, person2);
    error result = <error>err;
    test:assertEquals(result.message().toString(), "Assertion Failed!\nexpected: 'object assertions-error-messages:Person'\nactual\t: 'object assertions-error-messages:Person'");
}
