package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            File f = new File("dataBase.txt");
            if (!f.exists()) {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                out.writeObject(new DistanceExaminator());
                out.close();
            }
        }
        catch (Exception e){System.out.println(e.getMessage());}
        //Main window
        JFrame frame = new JFrame("Main window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel nameLabel = new JLabel("Имя");
        JTextField nameText = new JTextField();
        JLabel loginLabel = new JLabel("Логин");
        JTextField loginText = new JTextField();
        JLabel passwordLabel = new JLabel("Пароль");
        JTextField passwordText = new JTextField();
        JLabel repPasswordLabel = new JLabel("Повтор пароля");
        JTextField repPasswordText = new JTextField();
        JButton logButton = new JButton("Войти");
        JButton regButton = new JButton("Зарегистрироваться");

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new GridLayout(10, 1));
        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(loginLabel);
        panel.add(loginText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(repPasswordLabel);
        panel.add(repPasswordText);
        panel.add(logButton);
        panel.add(regButton);

        nameLabel.setVisible(false);
        nameText.setVisible(false);
        repPasswordLabel.setVisible(false);
        repPasswordText.setVisible(false);

        frame.setPreferredSize(new Dimension(200, 300));
        frame.pack();
        frame.setVisible(true);

        //Admin window

        JFrame adminWindow = new JFrame("Admin");
        JLabel questionLabel = new JLabel("Вопрос");
        JTextField questionText = new JTextField();
        JLabel answerLabel = new JLabel("Ответ на вопрос");
        JTextField answerText = new JTextField();
        JButton addQuestion = new JButton("Добавить вопрос");
        JButton exiteAdmin = new JButton("Выйти из системы");

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(6, 1));

        adminWindow.add(adminPanel);
        adminPanel.add(questionLabel);
        adminPanel.add(questionText);
        adminPanel.add(answerLabel);
        adminPanel.add(answerText);
        adminPanel.add(addQuestion);
        adminPanel.add(exiteAdmin);

        adminWindow.pack();

        // User window

        JFrame userWindow = new JFrame("User");

        JButton examination = new JButton("Пройти экзамен");
        JButton getMark = new JButton("Узнать оценку за экзамен");
        JButton exiteUser = new JButton("Выйти из системы");

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(3, 1));

        userWindow.add(userPanel);
        userPanel.add(examination);
        userPanel.add(getMark);
        userPanel.add(exiteUser);

        userWindow.pack();

        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameLabel.isVisible()){
                    nameLabel.setVisible(false);
                    nameText.setVisible(false);
                    repPasswordLabel.setVisible(false);
                    repPasswordText.setVisible(false);
                    nameText.setText("");
                    loginText.setText("");
                    passwordText.setText("");
                    repPasswordText.setText("");
                }
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                    DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                    in.close();

                    distanceExaminator.logining(loginText.getText(), passwordText.getText());

                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                    out.writeObject(distanceExaminator);
                    out.close();

                    if (distanceExaminator.getCurreantUser() == -1) {
                        adminWindow.setVisible(true);
                        frame.setVisible(false);
                    }
                    else if (distanceExaminator.getCurreantUser() > -1){
                        userWindow.setVisible(true);
                        frame.setVisible(false);
                    }

                    loginText.setText("");
                    passwordText.setText("");
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameLabel.isVisible()){
                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                        DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                        in.close();

                        distanceExaminator.addUser(nameText.getText(), loginText.getText(),
                                passwordText.getText(), repPasswordText.getText());
                        JOptionPane.showMessageDialog(frame, "Успешная регистрация");

                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                        out.writeObject(distanceExaminator);
                        out.close();

                        nameLabel.setVisible(false);
                        nameText.setVisible(false);
                        repPasswordLabel.setVisible(false);
                        repPasswordText.setVisible(false);
                        nameText.setText("");
                        loginText.setText("");
                        passwordText.setText("");
                        repPasswordText.setText("");
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(frame, ex.getMessage());
                    }
                }
                else {
                    nameLabel.setVisible(true);
                    nameText.setVisible(true);
                    repPasswordLabel.setVisible(true);
                    repPasswordText.setVisible(true);
                }
            }
        });

        addQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                    DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                    in.close();

                    distanceExaminator.addQuestion(questionText.getText(), answerText.getText());
                    JOptionPane.showMessageDialog(frame, "Вопрос успешно добавлен");

                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                    out.writeObject(distanceExaminator);
                    out.close();

                    questionText.setText("");
                    answerText.setText("");
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
        exiteAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                    DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                    in.close();

                    distanceExaminator.exit();

                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                    out.writeObject(distanceExaminator);
                    out.close();

                    frame.setVisible(true);
                    adminWindow.setVisible(false);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });

        examination.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                    DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                    in.close();

                    JOptionPane.showMessageDialog(frame, "Начало экзамена");
                    distanceExaminator.examination();
                    JOptionPane.showMessageDialog(frame, "Экзамен завершен");

                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                    out.writeObject(distanceExaminator);
                    out.close();
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
        getMark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                    DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                    in.close();

                    JOptionPane.showMessageDialog(frame, "Ваша оценка за экзамен: " + distanceExaminator.getMark());

                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                    out.writeObject(distanceExaminator);
                    out.close();
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
        exiteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                    DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                    in.close();

                    distanceExaminator.exit();

                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                    out.writeObject(distanceExaminator);
                    out.close();

                    frame.setVisible(true);
                    userWindow.setVisible(false);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
    }
}
