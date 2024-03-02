<?php
include "functions.php";
$header =  file_get_contents("WebTech/Templates/header.html");
$footer = file_get_contents("WebTech/Templates/footer.html");
$head =  file_get_contents("WebTech/Templates/head.html");
$page = file_get_contents("WebTech/index.html");

$page = str_replace('{header}', $header, $page);
$page = str_replace('{footer}', $footer, $page);
$page = str_replace('{head}', $head, $page);

$page =  str_replace('{styles_href}',"/WebTech/styles.css",$page);
$page = str_replace('{main}',file_get_contents("WebTech/Templates/contactUs_main.html"),$page);
$page = str_replace('{title}', "Culinary_Contacts", $page);

$contactBox_pattern=file_get_contents("WebTech/Templates/contact_box.html");
$pdo = new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8','root','ilyabkmz');
$result = $pdo->prepare("SELECT * FROM `contacts`");
$result->execute();
$array = $result->fetchAll(PDO::FETCH_ASSOC);
foreach ($array as $contact)
{
            $pos = strpos($page,"{contactBox}");
            $page = substr_replace($page,$contactBox_pattern,$pos,strlen("{contactBox}"));
            $page=str_replace("{contactName}",$contact['name'],$page);
            $page=str_replace("{contactIcon}",$contact['icon'],$page);
            $page=str_replace("{contactDescription}",$contact['description'],$page);
}
if(isset($_POST['message']))
{
    $text = $_POST['message'];
    $text = formatMessage($text);
    $page = str_replace("{text}",$text,$page);
}else $page = str_replace("{text}","",$page);

print($page);