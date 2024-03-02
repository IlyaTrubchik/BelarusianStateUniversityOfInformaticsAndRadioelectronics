<?php
include 'buyProduct.php';
$header =  file_get_contents("WebTech/Templates/header.html");
$footer = file_get_contents("WebTech/Templates/footer.html");
$head =  file_get_contents("WebTech/Templates/head.html");
$page =file_get_contents("WebTech/index.html");

$page= str_replace('{header}', $header, $page);
$page= str_replace('{footer}', $footer, $page);
$page= str_replace('{head}', $head, $page);

$page=  str_replace('{styles_href}',"/WebTech/styles.css",$page);
$page = str_replace('{main}',file_get_contents("WebTech/Templates/recipes_main.html"),$page);
$page = str_replace('{title}', "Culinary_Recipes", $page);


$menuItem =file_get_contents("WebTech/Templates/menuItem.html");
$pdo = new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8','root','ilyabkmz');
$result = $pdo->prepare("SELECT * FROM `foodmenu` WHERE `id`>:id");
$var = 0;
$result->bindParam('id',$var,PDO::PARAM_INT);
$result->execute();
$array = $result->fetchAll(PDO::FETCH_ASSOC);
foreach ($array as $foodItem)
{
        $pos = strpos($page, "{menuItem}");
        $page = substr_replace($page, $menuItem, $pos, strlen("{menuItem}"));
        $page = str_replace('{foodName}', $foodItem['foodName'], $page);
        $page = str_replace('{foodPrice}', $foodItem['foodCost'], $page);
        $page = str_replace('{foodDescription}', $foodItem['foodDescription'], $page);
        $page = str_replace('{foodID}', $foodItem['id'], $page);
}

print($page);