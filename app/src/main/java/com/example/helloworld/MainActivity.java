package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        assignId(R.id.button_c, "C");
        assignId(R.id.button_open_bracket, "(");
        assignId(R.id.button_close_bracket, ")");
        assignId(R.id.button_divide, "/");
        assignId(R.id.button_multiply, "*");
        assignId(R.id.button_plus, "+");
        assignId(R.id.button_minus, "-");
        assignId(R.id.button_equals, "=");
        assignId(R.id.button_0, "0");
        assignId(R.id.button_1, "1");
        assignId(R.id.button_2, "2");
        assignId(R.id.button_3, "3");
        assignId(R.id.button_4, "4");
        assignId(R.id.button_5, "5");
        assignId(R.id.button_6, "6");
        assignId(R.id.button_7, "7");
        assignId(R.id.button_8, "8");
        assignId(R.id.button_9, "9");
        assignId(R.id.button_ac, "AC");
        assignId(R.id.button_dot, ".");

    }

    void assignId(int id, String buttonText) {
        MaterialButton btn = findViewById(id);
        btn.setOnClickListener(this);
        btn.setText(buttonText);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if (buttonText.equals("AC")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }
        if (buttonText.equals("=")) {
            calculateResult(dataToCalculate);
            return;
        }
        if (buttonText.equals("C")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        } else {
            dataToCalculate = dataToCalculate + buttonText;
        }
        solutionTv.setText(dataToCalculate);
    }

    void calculateResult(String expression) {
        try {
            double result = eval(expression);
            resultTv.setText(String.valueOf(result));
        } catch (Exception e) {
            resultTv.setText("Err");
        }
    }

    // Helper method to evaluate a mathematical expression in String format
    private double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }
        }.parse();
    }
}
