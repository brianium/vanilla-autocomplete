<?php
$uri = $_SERVER['REQUEST_URI'];
$words = '/^\/words(\/[\w]+)?/';
if(file_exists(__DIR__ . DIRECTORY_SEPARATOR . $uri)) {
    return false;
else if (preg_match($words, $uri)) {
    require_once __DIR__ . DIRECTORY_SEPARATOR . 'words' . DIRECTORY_SEPARATOR . 'index.php';
} else {
    require_once 'index.html';
}