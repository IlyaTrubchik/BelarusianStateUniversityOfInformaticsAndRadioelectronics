<?php

$header =  file_get_contents("WebTech/Templates/header.html");
$footer = file_get_contents("WebTech/Templates/footer.html");
$head =  file_get_contents("WebTech/Templates/head.html");
$page =file_get_contents("WebTech/index.html");

$page= str_replace('{header}', $header, $page);
$page= str_replace('{footer}', $footer, $page);
$page= str_replace('{head}', $head, $page);
$page = str_replace('{title}', "Culinary_Main", $page);
$page=  str_replace('{styles_href}',"/WebTech/styles.css",$page);
$page = str_replace('{main}',file_get_contents("WebTech/Templates/main_main.html"),$page);

//$foodNames=["Salad"=>"fa-solid fa-bowl-food","Cake"=>"fa-solid fa-cake-candles","Burger"=>"fa-solid fa-burger","IceCream"=>"fa-solid fa-ice-cream","Hot-dog"=>"fa-solid fa-hotdog","Cookies"=>"fa-solid fa-cookie"];
$food_container="{Food_Container}";
$food_container_pattern = file_get_contents("WebTech/Templates/food-container.html");

$pdo =new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8','root','ilyabkmz');
$result = $pdo->prepare("SELECT * FROM `foodtypes` WHERE `id`>:id");
$var = 0;
$result->bindParam('id',$var,PDO::PARAM_INT);
$result->execute();
$array = $result->fetchAll(PDO::FETCH_ASSOC);

foreach ($array as $foodData){
    $pos = strpos($page,$food_container);
    $page = substr_replace($page,$food_container_pattern,$pos,strlen($food_container));
    $page =  str_replace("{Food_Name}",$foodData['typeName'],$page);
    $page =  str_replace("{Food_Icon}",$foodData['typeIcon'],$page);
    $page =  str_replace("{Food_Description}",$foodData['foodDescription'],$page);
}


$recipe_Container_pattern = file_get_contents("WebTech/Templates/Recipe_Container.html");
$recipe_Containers=["/WebTech/FoodImg/number1.jpg","/WebTech/FoodImg/number2.jpg","/WebTech/FoodImg/number3.jpg","/WebTech/FoodImg/number4.jfif","/WebTech/FoodImg/number5.jpg","/WebTech/FoodImg/number6.jfif","/WebTech/FoodImg/number7.jpg","/WebTech/FoodImg/number8.jpg"];
foreach ($recipe_Containers as $Url)
{
    $pos = strpos($page,"{Recipe_Container}");
    $page = substr_replace($page,$recipe_Container_pattern,$pos,strlen("{Recipe_Container}"));
    $page = str_replace("{recipeUrl}",$Url,$page);
}

print($page);
