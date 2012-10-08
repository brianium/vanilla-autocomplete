(function(){

var KeyMap = this.KeyMap;

function Autocompleter(inputId, containerId) {
    this.client = new HttpClient();
    this.input = new RichInput(inputId);
    this.matcher = new Matcher(this.client);
    this.container = document.getElementById(containerId);
    this.init();
};

function createParagraph(text, attrs) {
    attrs || (attrs = {});
    var p = document.createElement('p'),
        t = document.createTextNode(text);
    for(var k in attrs) {
        p.setAttribute(k, attrs[k]);
    }
    p.appendChild(t);
    return p;
}

Autocompleter.prototype = {

    isAuto: false,

    triggeredAt: -1,

    isSpecialKey: false,

    init: function() {
        this.input.onkeydown(this.handleSpecialKeys.bind(this));
        this.input.onkeydown(this.toggleAutocomplete.bind(this));
        this.input.onkeyup(this.complete.bind(this));
        this.matcher.onmatch(this.update, this);
        document.forms[0].addEventListener('submit', this.submit.bind(this), false);
    },

    complete: function() {
        var text = this.input.value.substring(this.triggeredAt);
        this.isAuto && text && this.matcher.match(text);
    },

    update: function(matches) {
        if(matches.length === 0) return !this.disableAutocomplete();
        this.populateMatches(matches);
        this.autocomplete(matches[0]);
    },

    autocomplete: function(match, halt) {
        var last = /([\s]+)[\S]+$/
          , start = this.input.length
          , halt;

        (halt === true || halt === false) || (halt = this.isSpecialKey);
        if(halt) return;
        if(last.test(this.input.value))
            this.input.value = this.input.value.replace(last, "$1" + match);
        else this.input.value = match;
        this.input.highlight(start, this.input.length);
        this.isSpecialKey = false;
    },

    toggleAutocomplete: function(e) {
        this.handleEnableAutocomplete(e);
        this.handleDisableAutocomplete(e);
        this.handleTabComplete(e);
    },

    handleEnableAutocomplete: function(e) {
        if(this.input.carrotPressed && this.inputEmptyOrAtWhitespace())
            this.enableAutocomplete() && e.preventDefault();
    },

    handleDisableAutocomplete: function(e) {
        KeyMap.isSpacebar(e) && this.disableAutocomplete();
        if(KeyMap.isBackspace(e)) {
            (this.atTriggerPoint() || this.input.cursorPos === 0)
                && this.disableAutocomplete();
        }
    },

    handleTabComplete: function(e) {
        if(KeyMap.isTab(e) && this.isAuto) {
            this.autocomplete(this.container.getElementsByTagName("p")[0].getAttribute("data-text"));
            this.disableAutocomplete();
            this.moveToEnd();
            e.preventDefault();
        }
    },

    handleSpecialKeys: function(e) {
        var code = (e.keyCode || e.which)
          , specials = [37, 8, 36, 16, 39, 35];
        this.isSpecialKey = specials.some(function(value) { return value === code; });
    },

    submit: function(e) {
        var start = parseInt(this.input.elem.getAttribute('data-highlight-start'));
        (start > 0 && this.isAuto) && (this.input.value = this.input.value.substring(0, start));
        this.disableAutocomplete();
        this.moveToEnd();
        e.preventDefault();
    },

    populateMatches: function(matches) {
        this.clearMatches(); 
        matches.forEach(function(m){
            var para = createParagraph(m, {'data-text':m}), self = this;
            para.addEventListener('click', this.bindClick(para), false);
            this.container.appendChild(para);
        }.bind(this));
    },

    bindClick: function(elem) {
        return function(e) {
            this.autocomplete(elem.getAttribute('data-text'), false);
            this.disableAutocomplete();
            this.moveToEnd();
        }.bind(this);
    },

    clearMatches: function() {
        while(this.container.hasChildNodes())
            this.container.removeChild(this.container.lastChild);
    },

    enableAutocomplete: function() {
        this.input.addClass("auto");
        this.triggeredAt = this.input.length;
        return this.isAuto = true;
    },

    disableAutocomplete: function() {
        this.input.removeClass("auto");
        this.triggeredAt = -1;
        this.clearMatches();
        return !(this.isAuto = false);
    },

    inputEmptyOrAtWhitespace: function() {
        return this.input.isEmpty() || this.input.value.charAt(this.input.length - 1) === " ";
    },

    atTriggerPoint: function() {
        return this.input.length - 1 === this.triggeredAt;
    },

    moveToEnd: function() {
        this.input.value = this.input.value;
    }
};

var complete = new Autocompleter("input", "matches");


}).call(this);