<?php
/**
 * Write the body as plain text and optionally
 * set the status
 * @param string $body the message body
 * @param int $status the http status code
 */
function write($body, $status = 200) {
    header('Content-type: text/plain; charset="UTF-8"');
    http_response_code($status);
    echo $body;
    exit;
}