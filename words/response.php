<?php
function write($body, $status = 200) {
    header('Content-type: text/plain; charset="UTF-8"');
    http_response_code($status);
    echo $body;
    exit;
}