package com.example.trokyy.controllers.Reclamation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

class ReclamationChatbot extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextArea chatArea = new JTextArea();
    private JTextField inputField = new JTextField();
    private JButton sendButton = new JButton();
    private JLabel sendLabel = new JLabel();
    private Random random = new Random();

    ReclamationChatbot(){

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setSize(400,400);
        frame.getContentPane().setBackground(Color.decode("#f0f0f0"));
        frame.setTitle("Reclamation ChatBot");

        // Chat Area
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(Color.decode("#f0f0f0"));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(10, 10, 380, 300);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane);

        // Input Field
        inputField.setSize(300,30);
        inputField.setLocation(10,320);
        inputField.setBackground(Color.WHITE);
        frame.add(inputField);

        // Send Button
        sendLabel.setText("Send");
        sendButton.add(sendLabel);
        sendButton.setSize(70,30);
        sendButton.setLocation(320,320);
        sendButton.setBackground(Color.decode("#4CAF50"));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(e.getSource()==sendButton) {

                    String text = inputField.getText().toLowerCase();
                    chatArea.setForeground(Color.BLACK);
                    chatArea.append("You: "+text+"\n");
                    inputField.setText("");

                    // Bot's Responses
                    String botResponse = generateResponse(text);
                    chatArea.append("ChatBot: "+botResponse+"\n");
                }

            }

            // Generate Bot's Response
            private String generateResponse(String input) {
                if(input.contains("hello") || input.contains("hi")) {
                    return "Hello! How can I assist you with your complaint?";
                }
                else if(input.contains("how are you")) {
                    return "I'm just a computer program, but thank you for asking. How can I help?";
                }
                else if(input.contains("file a complaint") || input.contains("complaint")) {
                    return "Sure, what is the nature of your complaint?";
                }
                else if(input.contains("urgent")) {
                    return "Please provide more details about the urgency of your complaint.";
                }
                else if(input.contains("thank you")) {
                    return "You're welcome! If you need further assistance, feel free to ask.";
                }
                else if(input.contains("bye")) {
                    return "Goodbye! Take care.";
                }
                else {
                    String[] defaultResponses = {
                            "I'm sorry, I didn't understand that. Could you please rephrase?",
                            "I'm not sure I follow. Can you provide more details?",
                            "That's an interesting point. Could you elaborate?",
                            "I'm here to assist you with your complaint. What specifically would you like to address?"
                    };
                    return defaultResponses[random.nextInt(defaultResponses.length)];
                }
            }
        });
        frame.add(sendButton);
    }
}

public class ReclamationChatBotDemo {

    public static void main(String[] args) {
        new ReclamationChatbot();
    }

}
