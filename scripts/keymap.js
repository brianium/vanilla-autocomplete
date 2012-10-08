(function(){

this.KeyMap = {
   "Enter":13,
   "Shift":16,
   "Tab":9,
   "Num6":54,
   "Backspace":8,
   "Spacebar":32
};

for(var key in this.KeyMap) {
    KeyMap["is" + key] = (function(compare) {
      return function(ev) {
         var code = ev.keyCode || ev.which;
         return code === compare;
      }
    })(KeyMap[key]);
}

}).call(this);