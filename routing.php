<?php
/**
 * The routing file for the PHP server
 * This file will first check if the request uri is a static file, if it is, serve as is
 * Next it will be determined if the route is requesting the /words route, if it is require the handling file
 * in words/main.php
 * Otherwise serve up the JavaScript app
 */
$uri = $_SERVER['REQUEST_URI'];
$words = '/^\/words(\/[\w]+)/';
echo $uri;
if(file_exists(__DIR__ . DIRECTORY_SEPARATOR . $uri)) {
    return false;
else if (preg_match($words, $uri)) {
    require_once __DIR__ . DIRECTORY_SEPARATOR . 'words' . DIRECTORY_SEPARATOR . 'index.php';
} else {
    require_once 'index.html';
}