<?php
function formatMessage(String $message):String
{
        $numbers = "/-?\d+(\.\d+)?/";
        $rusWords = "/([а-яА-Я]+)(-?[а-яА-Я]+)*/u";
        $engWords = "/[a-zA-Z]+'?[a-zA-Z]*/";
        $message = preg_replace($engWords, " <span style='color:blue'>$0</span> ",$message);
        $message = preg_replace($rusWords, " <span style='color:red'>$0</span> ",$message);
        $message = preg_replace($numbers,"<span style='color:green'>$0</span>",$message);
        return $message;
}
function isCorrectEmail($email):bool{
    return preg_match("/[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]/u",$email);
}
function getUserEmail() : string {
    $pdo = new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8','root','ilyabkmz');

    $sth = $pdo->prepare("SELECT * FROM `users` WHERE `userId` = :cookieID");
    $sth->bindParam(':cookieID', $_COOKIE['id'], PDO::PARAM_STR);
    $sth->execute();

    $data = $sth->fetch(PDO::FETCH_ASSOC);
    return $data['userEmail'];
}
function sendMessage($message){
    echo "<script>alert('$message');</script>";
}
function checkCookies() {
    if (isset($_COOKIE['id']) and isset($_COOKIE['hash'])) {
        $pdo =new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8','root','ilyabkmz');

        $sth = $pdo->prepare("SELECT * FROM `users` WHERE `userId` = :cookieID");
        $sth->bindParam(':cookieID', $_COOKIE['id'], PDO::PARAM_STR);
        $sth->execute();
        $data = $sth->fetch(PDO::FETCH_ASSOC);
        if(($data['userHash'] != $_COOKIE['hash']) or ($data['userId'] != $_COOKIE['id'])) {
            unsetCookies();

            return false;
        }
        return true;
    } else {
        return false;
    }
}
function unsetCookies() {
    setcookie("id",   "", time() - 1);
    setcookie("hash", "", time() - 1);
}
