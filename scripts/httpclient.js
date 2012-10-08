(function(){

var root = this;

function HttpClient() {
}

function isOK(status) {
    return status == 200 || status == 204;
}

HttpClient.prototype = {
    get: function(url, success, fail) {
        var xhr = new XMLHttpRequest();
        xhr.addEventListener("load", function(e) {
            isOK(xhr.status) && success(xhr.responseText);
            fail && !isOK(xhr.status) && fail(xhr);
        }, false);
        xhr.open("GET", url, true);
        xhr.send(null);
    }
}

root.HttpClient = HttpClient;

}).call(this);