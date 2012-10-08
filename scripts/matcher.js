(function(){

var root = this;

function Matcher(httpClient) {
    this.client = httpClient;
}

Matcher.prototype = {

    events: {"match":[]},

    onmatch: function(fn, ctxt) {
        this.events["match"].push([fn, ctxt || this]);
    },

    match: function(text) {
        var self = this, matches = [];
        this.client.get('/words/' + text, function(resp){
            resp && (matches = resp.split(','));
            self.trigger('match', matches);
        });
    },

    trigger: function(type) {
        var rest = Array.prototype.slice.call(arguments, 1);
        this.events["match"].forEach(function(cb){
            cb[0].apply(cb[1], rest);
        });
    }
};

root.Matcher = Matcher;

}).call(this);