/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.chatapp;

import javax.swing.JFrame;

/**
 *
 * @author jimd
 */
public class ChatApp {

    public static void main(String[] args) {
        Dashboard chat_app = new Dashboard();
        chat_app.setVisible(true);
        chat_app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
