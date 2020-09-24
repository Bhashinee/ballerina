import ballerina/http;
import ballerina/io;
import ballerina/log;

public function sortModules(Module[] modules) returns Module[] {
    return modules.sort(compareModules);
}

function compareModules(Module m1, Module m2) returns int {
    if (m1.level > m2.level) {
        return 1;
    } else if (m1.level < m2.level) {
        return -1;
    } else {
        return 0;
    }
}

public function getModuleJsonArray() returns json[] {
    var result = readFileAndGetJson(CONFIG_FILE_PATH);
    if (result is error) {
        logAndPanicError("Error occurred while reading the config file", result);
    }
    json jsonFile = <json>result;
    return <json[]>jsonFile.modules;
}

public function getModuleArray(json[] modulesJson) returns Module[] {
    Module[] modules = [];
    foreach json moduleJson in modulesJson {
        Module|error result = Module.constructFrom(moduleJson);
        if (result is error) {
            logAndPanicError("Error building the module record", result);
        }
        Module module = <Module>result;
        modules.push(module);
    }
    return modules;
}

public function createRequest(string accessTokenHeaderValue) returns http:Request {
    http:Request request = new;
    request.setHeader(ACCEPT_HEADER_KEY, ACCEPT_HEADER_VALUE);
    request.setHeader(AUTH_HEADER_KEY, accessTokenHeaderValue);
    return request;
}

function readFileAndGetJson(string path) returns json|error {
    io:ReadableByteChannel rbc = check <@untainted>io:openReadableFile(path);
    io:ReadableCharacterChannel rch = new (rbc, "UTF8");
    var result = <@untainted>rch.readJson();
    closeReadChannel(rch);
    return result;
}

function closeReadChannel(io:ReadableCharacterChannel rc) {
    var result = rc.close();
    if (result is error) {
        log:printError("Error occurred while closing character stream", result);
    }
}

public function validateResponse(http:Response response, string moduleName) returns boolean {
    int statusCode = response.statusCode;
    if (statusCode != 200 && statusCode != 201 && statusCode != 202 && statusCode != 204) {
        return false;
    }
    return true;
}

public function logAndPanicError(string message, error e) {
    log:printError(message, e);
    panic e;
}

public function printModules(Module[] modules) {
    string[] moduleStrings = modules.map(function (Module m) returns string {
        return m.name + " " + m.'version;
    });
    foreach string moduleString in moduleStrings {
        log:printInfo(moduleString);
    }
}

public function logNewLine() {
    log:printInfo("------------------------------");
}
