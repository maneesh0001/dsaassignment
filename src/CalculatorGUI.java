//Question 2a

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CalculatorGUI extends JFrame {
    private JTextField inputField;
    private JButton[] numberButtons;
    private JButton decimalButton, equalsButton, addButton, subtractButton, multiplyButton, divideButton;
    private JButton clearButton, sqrtButton, changeSignButton, percentButton, memoryClearButton, memoryRecallButton, memoryAddButton, exponentButton, leftParenButton, rightParenButton;
    private JLabel resultLabel;
    private double memory = 0;

    public CalculatorGUI() {
        setTitle("Advanced Calculator");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set modern Look and Feel
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatIntelliJLaf");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Background color similar to the gradient in the image
        Color backgroundColor = new Color(70, 130, 180);
        getContentPane().setBackground(backgroundColor);

        inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setFont(new Font("Arial", Font.PLAIN, 28));
        inputField.setHorizontalAlignment(JTextField.RIGHT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 10, 10));
        buttonPanel.setBackground(backgroundColor);

        initializeButtons();

        // Add buttons to the panel
        for (int i = 1; i <= 9; i++) {
            buttonPanel.add(numberButtons[i]);
        }
        buttonPanel.add(decimalButton);
        buttonPanel.add(numberButtons[0]);
        buttonPanel.add(equalsButton);
        buttonPanel.add(addButton);
        buttonPanel.add(subtractButton);
        buttonPanel.add(multiplyButton);
        buttonPanel.add(divideButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(sqrtButton);
        buttonPanel.add(changeSignButton);
        buttonPanel.add(percentButton);
        buttonPanel.add(memoryClearButton);
        buttonPanel.add(memoryRecallButton);
        buttonPanel.add(memoryAddButton);
        buttonPanel.add(exponentButton);
        buttonPanel.add(leftParenButton);
        buttonPanel.add(rightParenButton);

        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultLabel.setForeground(Color.WHITE);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(inputField, BorderLayout.CENTER);
        northPanel.add(resultLabel, BorderLayout.SOUTH);
        northPanel.setBackground(backgroundColor);

        add(northPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        pack();
    }

    private void initializeButtons() {
        // Number buttons 0-9
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createButton(String.valueOf(i), e -> inputField.setText(inputField.getText() + e.getActionCommand()));
        }

        decimalButton = createButton(".", e -> inputField.setText(inputField.getText() + "."));
        equalsButton = createButton("=", e -> calculateResult());
        addButton = createButton("+", e -> inputField.setText(inputField.getText() + " + "));
        subtractButton = createButton("-", e -> inputField.setText(inputField.getText() + " - "));
        multiplyButton = createButton("*", e -> inputField.setText(inputField.getText() + " * "));
        divideButton = createButton("/", e -> inputField.setText(inputField.getText() + " / "));
        clearButton = createButton("C", e -> inputField.setText(""));
        sqrtButton = createButton("√", e -> calculateSquareRoot());
        changeSignButton = createButton("±", e -> changeSign());
        percentButton = createButton("%", e -> inputField.setText(inputField.getText() + " % "));
        memoryClearButton = createButton("MC", e -> memory = 0);
        memoryRecallButton = createButton("MR", e -> inputField.setText(String.valueOf(memory)));
        memoryAddButton = createButton("M+", e -> memory += Double.parseDouble(inputField.getText()));
        exponentButton = createButton("^", e -> inputField.setText(inputField.getText() + " ^ "));
        leftParenButton = createButton("(", e -> inputField.setText(inputField.getText() + "("));
        rightParenButton = createButton(")", e -> inputField.setText(inputField.getText() + ")"));
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 22));
        button.addActionListener(listener);
        button.setBackground(new Color(148, 0, 211)); // Bright purple background
        button.setForeground(Color.WHITE); // White text color
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(186, 85, 211)); // Lighter purple on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(148, 0, 211)); // Original purple color
            }
        });
        return button;
    }

    private void calculateResult() {
        try {
            String expression = inputField.getText();
            double result = evaluateExpression(expression);
            resultLabel.setText("Result: " + result);
            inputField.setText(String.valueOf(result));
        } catch (Exception ex) {
            resultLabel.setText("Error: Invalid Expression");
        }
    }

    private void calculateSquareRoot() {
        try {
            double value = Double.parseDouble(inputField.getText());
            double result = Math.sqrt(value);
            resultLabel.setText("Result: " + result);
            inputField.setText(String.valueOf(result));
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Invalid Input");
        }
    }

    private void changeSign() {
        try {
            double value = Double.parseDouble(inputField.getText());
            value = -value;
            inputField.setText(String.valueOf(value));
        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Invalid Input");
        }
    }

    private double evaluateExpression(String expression) {
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        StringBuilder numBuilder = new StringBuilder();
        boolean hasNum = false;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch) || ch == '.') {
                numBuilder.append(ch);
                hasNum = true;
            } else {
                if (hasNum) {
                    operandStack.push(Double.parseDouble(numBuilder.toString()));
                    numBuilder.setLength(0);
                    hasNum = false;
                }
                if (ch == '(') {
                    operatorStack.push(ch);
                } else if (ch == ')') {
                    while (operatorStack.peek() != '(') {
                        processOperator(operandStack, operatorStack.pop());
                    }
                    operatorStack.pop(); // Remove '('
                } else if (isOperator(ch)) {
                    while (!operatorStack.isEmpty() && precedence(ch) <= precedence(operatorStack.peek())) {
                        processOperator(operandStack, operatorStack.pop());
                    }
                    operatorStack.push(ch);
                }
            }
        }

        if (hasNum) operandStack.push(Double.parseDouble(numBuilder.toString()));

        while (!operatorStack.isEmpty()) {
            processOperator(operandStack, operatorStack.pop());
        }

        return operandStack.pop();
    }

    private void processOperator(Stack<Double> operandStack, char operator) {
        double b = operandStack.pop();
        double a = operandStack.pop();
        switch (operator) {
            case '+': operandStack.push(a + b); break;
            case '-': operandStack.push(a - b); break;
            case '*': operandStack.push(a * b); break;
            case '/': operandStack.push(a / b); break;
            case '^': operandStack.push(Math.pow(a, b)); break;
        }
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
        }
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI calculator = new CalculatorGUI();
            calculator.setVisible(true);
        });
    }
}
