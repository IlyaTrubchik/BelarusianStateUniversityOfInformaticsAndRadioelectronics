<?php
include  'authorization.php';
$header =  file_get_contents("WebTech/Templates/header.html");
$footer = file_get_contents("WebTech/Templates/footer.html");
$head =  file_get_contents("WebTech/Templates/head.html");
$page =file_get_contents("WebTech/index.html");
$page= str_replace('{header}', $header, $page);
$page= str_replace('{footer}', $footer, $page);
$page= str_replace('{head}', $head, $page);
$page = str_replace('{title}', "Culinary_Main", $page);
$page=  str_replace('{styles_href}',"/WebTech/styles.css",$page);
$page = str_replace('{main}',file_get_contents("WebTech/Templates/auth_form.html"),$page);
$page = str_replace('{type}','Authorization',$page);
print($page);
