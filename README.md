Vanilla Autocomplete
--------------------
A simple Autocomplete app written in JavaScript. The purpose of this application is to demonstrate
testing a client-side application with the ScalaTest Selenium DSL.

Using The App
=============
The application is built using vanilla ECMAScript 5 features. The interface sports a single text input that can 
toggle autocomplete mode on and off. To enable autocomplete mode, press ^. The recommended word can be accepted by
pressing tab. Other recommendations will be placed in a list underneath the text input. Clicking any of the list items
will accept that item as the current word.

To see the full list of expected behavior, check out the tests.

Starting The Server
===================
The server makes use of the built-in server provided by PHP 5.4. In order to run it you must have PHP 5.4 installed. You can run the server by executing the script in the bin directory. `bin/server`

Running The Tests
=================
First make sure the server is running via `bin/server`.

The tests are written using ScalaTest and the Selenium DSL. The scala project is located in the `tests` directory. If you have sbt installed, you can run the tests by switching to the tests directory and running `sbt test`.

### A Note On Chrome Testing ###
The Chrome browser requires a bit of extra work to get up and running. It is pretty easy, and the directions for doing so can be found on the [Selenium wiki](http://code.google.com/p/selenium/wiki/ChromeDriver)

Tested using Chrome 22.0.1229.79 and Firefox 15.0.1 on Ubuntu 12.04 and Mac OS X 10.7.5 (Lion)