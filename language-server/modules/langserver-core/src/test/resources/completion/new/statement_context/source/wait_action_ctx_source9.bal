
function testFunction() {
    future<string> fs = start getStringResult();
    future<string> fs2 = start getStringResult2();
    wait fs|f
}

function getStringResult() returns string {
    return "Hello World";
}

function getStringResult2() returns string {
    return "Hello World2";
}