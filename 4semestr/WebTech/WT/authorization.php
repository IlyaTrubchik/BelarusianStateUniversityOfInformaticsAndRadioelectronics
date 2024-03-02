<?php
include 'functions.php';
    if(isset($_POST['email']) && isset($_POST['password'])) {
        $email = $_POST['email'];
        $password = $_POST['password'];
        if(isCorrectEmail($email) && $password!="") {
            if ($_POST['flag'] == 'Authorization') {
                $pdo = new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8', 'root', 'ilyabkmz');
                $sth = $pdo->prepare("SELECT * FROM `users` WHERE `userEmail` = :email");
                $sth->bindParam(':email', $email, PDO::PARAM_STR);
                $sth->execute();

                $data = $sth->fetch(PDO::FETCH_ASSOC);
                if (!empty($data) && $data['userPassword'] == hash("sha256", $password)) {
                    $userHash = hash("sha256", rand(10e5, 10e10));
                    $sth = $pdo->prepare("UPDATE `users` SET `userHash` = :userHash WHERE `userEmail` = :email");
                    $sth->bindParam(':email', $email, PDO::PARAM_STR);
                    $sth->bindParam(':userHash', $userHash, PDO::PARAM_STR);
                    $sth->execute();

                    setcookie("id", $data['userId']);
                    setcookie("hash", $userHash);
                    header("Location: recipes.php");
                } else {
                    sendMessage("Uncorrect login or password");
                }
            } else if ($_POST['flag'] == 'Registration') {
                $pdo = new PDO('mysql:dbname=culinarywebsite;host=127.0.0.1;charset=utf8', 'root', 'ilyabkmz');
                $sth = $pdo->prepare("SELECT * FROM `users` WHERE `userEmail` = :email");
                $sth->bindParam(':email', $email, PDO::PARAM_STR);
                $sth->execute();

                $data = $sth->fetch(PDO::FETCH_ASSOC);
                if (!empty($data) && $email == $data['userEmail']) {
                    sendMessage("User already exists");
                } else {
                    try {
                        $password = hash("sha256", $password);
                        $sth = $pdo->prepare("INSERT INTO `users` SET `userEmail` = :user_email,`userPassword`=:user_password");
                        $sth->bindParam(':user_email', $email);
                        $sth->bindParam(':user_password', $password);
                        $sth->execute();
                    } catch (Exception $e) {
                        sendMessage("ERROR");
                        echo($e->getMessage());
                    }
                    sendMessage("Registration successful");
                }
            }
        }

    }
