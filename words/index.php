<?php
/**
 * The main handler for the server. Creates a dictionary out of the file
 * wordTest.txt. This server responds to requests at /words/[A-Za-z]+
 * The server will respond with a comma separated list of words beginning with the 
 * stem passed to /words (i.e /words/ab)
 */
require_once 'response.php';

$uri = $_SERVER['REQUEST_URI'];
$request = substr($uri, 7, strlen($uri));
$method = $_SERVER['REQUEST_METHOD'];

if($method != 'GET') write("Method $method not supported", 405);

$wordsPath = dirname(__FILE__) . DIRECTORY_SEPARATOR . 'wordTest.txt';
$fp = @fopen($wordsPath, "r");

if(!$fp) write("Could not open word file for reading", 500);

$dict = [];

while($line = fgets($fp)) {
    if(!trim($line)) continue;
    $char = $line[0];
    $dict[$char][] = $line;
}
if(!feof($fp)) write("Did not read file to end", 500);

$char = $request[0];
$words = $dict[$char];
$begins = "/^$request/";

$matches = [];
foreach($words as $word) {
    if(preg_match($begins, $word)) {
        $matches[] = trim($word);
    }
}

if($matches)
    write(implode(',', $matches), 200);
else
    write("", 204);