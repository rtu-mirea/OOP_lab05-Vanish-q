package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.rmi.server.ExportException;

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

        JTextArea questionList = new JTextArea();
        JLabel questionNumberLabel = new JLabel("Номер вопроса");
        JTextField questionNamber = new JTextField();
        JButton changeQuestion = new JButton("Заменить вопрос");
        JButton delete = new JButton("Удалить вопрос");

        JLabel questionLabel = new JLabel("Вопрос");
        JTextField questionText = new JTextField();
        JLabel answerLabel = new JLabel("Ответ на вопрос");
        JTextField answerText = new JTextField();
        JButton addQuestion = new JButton("Добавить вопрос");
        JButton exiteAdmin = new JButton("Выйти из системы");

        JScrollPane scroll = new JScrollPane(questionList,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(11, 1));

        adminWindow.add(adminPanel);

        //adminPanel.add(questionList);
        //questionList.add(scroll);
        adminPanel.add(scroll);
        adminPanel.add(questionNumberLabel);
        adminPanel.add(questionNamber);

        adminPanel.add(questionLabel);
        adminPanel.add(questionText);
        adminPanel.add(answerLabel);
        adminPanel.add(answerText);
        adminPanel.add(addQuestion);

        adminPanel.add(changeQuestion);
        adminPanel.add(delete);

        adminPanel.add(exiteAdmin);

        adminWindow.pack();

        scroll.setVisible(false);
        //questionList.setVisible(false);
        questionNamber.setVisible(false);
        questionNumberLabel.setVisible(false);


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
                        logButton.setVisible(true);
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
                    logButton.setVisible(false);
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
        changeQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(questionNamber.isVisible()){
                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                        DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                        in.close();

                        distanceExaminator.changeQuestion(Integer.parseInt(questionNamber.getText()), questionText.getText(), answerText.getText());
                        JOptionPane.showMessageDialog(frame, "Вопрос успешно изменен");

                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                        out.writeObject(distanceExaminator);
                        out.close();

                        questionText.setText("");
                        answerText.setText("");
                        scroll.setVisible(false);
                        //questionList.setVisible(false);
                        questionNamber.setVisible(false);
                        questionNumberLabel.setVisible(false);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(frame, ex.getMessage());
                    }
                }
                else{
                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                        DistanceExaminator distanceExaminator = (DistanceExaminator) in.readObject();
                        in.close();
                        scroll.setVisible(true);
                        //questionList.setVisible(true);
                        questionNamber.setVisible(true);
                        questionNumberLabel.setVisible(true);
                        questionList.setText(distanceExaminator.getAllQuestions());
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(frame, ex.getMessage());

                    }
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(questionNamber.isVisible()){
                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                        DistanceExaminator distanceExaminator = (DistanceExaminator)in.readObject();
                        in.close();

                        //distanceExaminator.changeQuestion(Integer.parseInt(questionNamber.getText()), questionText.getText(), answerText.getText());
                        distanceExaminator.deleteQuestion(Integer.parseInt(questionNamber.getText()));
                        JOptionPane.showMessageDialog(frame, "Вопрос успешно удален");

                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dataBase.txt"));
                        out.writeObject(distanceExaminator);
                        out.close();

                        questionText.setText("");
                        answerText.setText("");
                        scroll.setVisible(false);
                        //questionList.setVisible(false);
                        questionNamber.setVisible(false);
                        questionNumberLabel.setVisible(false);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(frame, ex.getMessage());
                    }
                }
                else{
                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream("dataBase.txt"));
                        DistanceExaminator distanceExaminator = (DistanceExaminator) in.readObject();
                        in.close();
                        scroll.setVisible(true);
                        //questionList.setVisible(true);
                        questionNamber.setVisible(true);
                        questionNumberLabel.setVisible(true);
                        questionList.setText(distanceExaminator.getAllQuestions());
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(frame, ex.getMessage());

                    }
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
