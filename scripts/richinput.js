(function(){

var root = this,
    KeyMap = this.KeyMap;

function RichInput(id) {
    this.elem = root.document.getElementById(id);

    this.shiftPressed = false;
    this.carrotPressed = false;

    this.onkeydown(function(e){
        KeyMap.isNum6(e) && this.shiftPressed && (this.carrotPressed = true);
        KeyMap.isShift(e) && (this.shiftPressed = true);
    }.bind(this));

    this.onkeyup(function(e){
        this.shiftPressed && KeyMap.isNum6(e) && (this.carrotPressed = false);
        /* disable all shift based cases as well - for now only carrot
         * might need to get meta if there were more */
        KeyMap.isShift(e) && !(this.shiftPressed = false) && (this.carrotPressed = false);
    }.bind(this));
}

RichInput.prototype = {

    get length() {
        return this.elem.value.length;
    },

    get value() {
        return this.elem.value;
    },

    set value(val) {
        return this.elem.value = val;
    },

    get cursorPos() {
        return this.elem.selectionStart;
    },

    onkeyup: function(fn) {
        this.elem.addEventListener("keyup", fn, false);
    },

    onkeydown: function(fn) {
        this.elem.addEventListener("keydown", fn, false);
    },

    isEmpty:function() {
        return this.elem.value.trim().length === 0;
    },

    highlight: function(start, end) {
        this.elem.setSelectionRange(start, end);
        this.elem.setAttribute('data-highlight-start', start);
        this.elem.setAttribute('data-highlight-end', end);
    },

    clearData: function() {
        this.elem.removeAttribute("data-highlight-start")
        this.elem.removeAttribute('data-highlight-end');
    },

    addClass: function(cls) {
        this.elem.className += ' ' + cls;
    },

    removeClass: function(cls) {
        this.elem.className = this.elem.className.replace(cls, '').trim();
    }
};

root.RichInput = RichInput;

}).call(this);