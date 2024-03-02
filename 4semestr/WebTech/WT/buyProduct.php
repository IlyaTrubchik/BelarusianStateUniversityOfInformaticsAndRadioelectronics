<?php
include "functions.php";
include './phpmailer/Exception.php';
include './phpmailer/PHPMailer.php';
include './phpmailer/SMTP.php';

//Import PHPMailer classes into the global namespace
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;
function sendMailMessage(String $message)
{
    // header("Location: Main.php");
    if (checkCookies()) {
//Create an instance; passing `true` enables exceptions
        $mail = new PHPMailer(true);
        $mail->isSMTP();  //Send using SMTP
        $mail->Host = 'smtp.gmail.com';
        $mail->SMTPAuth = true;  //Enable SMTP authentication
        $mail->Username = 'webTech151001@gmail.com';
        $mail->Password = 'ueawcqnqoypgnniu';
        $mail->SMTPSecure = 'ssl';
        $mail->Port = 465;
        $mail->addAddress('webTech151001@gmail.com');//Add a сontact
        $mail->setFrom('webTech151001@gmail.com',"CulinaryWebSite");
//Content
        $mail->isHTML(true);
        $mail->Subject = "Request from web-site";
        $mail->Body = $message;
        $mail->send();
    }
}
if(isset($_COOKIE['id']) && isset($_COOKIE['hash']))
{
    $pdo = new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8', 'root', 'ilyabkmz');
    $sth = $pdo->prepare("SELECT * FROM `users` WHERE `userId` = :id");
    $sth->bindParam(':id', $_COOKIE['id'], PDO::PARAM_STR);
    $sth->execute();

    $data =  $sth->fetch(PDO::FETCH_ASSOC);
    if(!empty($data) && $data['userHash']==$_COOKIE['hash'])
    {
        $pdo = new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8', 'root', 'ilyabkmz');
        $sth = $pdo->prepare("SELECT * FROM `foodmenu` WHERE `id` = :id");
        $sth->bindParam(':id',$_POST['id']);
        $sth->execute();
        $email = $data['userEmail'];
        $id  = $data['userId'];
        $data = $sth->fetch(PDO::FETCH_ASSOC);
        if(!empty($data))
        {
            $productCost = $data['foodCost'];
            $productName = $data['foodName'];
            $sth = $pdo->prepare("INSERT INTO `orders` SET `userEmail` = :user_email,`userId`=:user_id,
            `productName` = :product_name,`productCost` = :product_cost");
            $sth->bindParam(':user_email', $email);
            $sth->bindParam(':user_id', $id);
            $sth->bindParam(':product_name', $productName);
            $sth->bindParam(':product_cost', $productCost);
            $sth->execute();
            sendMailMessage("Order from:".$email.";"."Order product name:".$productName.";"."Order cost:".$productCost.".");
        }

    }else{
        header("Location: 403.php");
    }

}else
{
    header("Location: 403.php");
}
