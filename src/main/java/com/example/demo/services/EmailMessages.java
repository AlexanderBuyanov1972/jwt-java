package com.example.demo.services;

import java.time.LocalDate;

public class EmailMessages {

    public String createActivationMessageText(String path) {
            return "Для активации перейдите по ссылке ... \n" +
                path;

    }

    public String createActivationMessageTitle() {
        return "Активируйте ваш почтовый ящик.";

    }
    public String activationMessage(String path) {
        return "<h1>Please go to the link: <a href=" + path + ">Click Me</a></h1>";
    }

    private String createTitle() {
        return "Сегодня " + LocalDate.now() + " день рождения Вашего друга!!!";
    }

    private String createMessage(String yeas, String fio, String phoneNumber) {
        return "Сегодня " + LocalDate.now()
                + " день рождения Вашего друга!!! \n Ему исполнилось уже "
                + yeas + "!!! \n Позвоните и поздравьте " + fio + " по номеру телефона "
                + phoneNumber + ". \n Удачного дня!!!";
    }

}
