package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color


class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView
    private var currentNumber = StringBuilder()
    private var operatorList = mutableListOf<Pair<String, Double>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.displayTextView)

        val numberButtons = listOf<Button>(
            findViewById(R.id.btn0),
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4),
            findViewById(R.id.btn5),
            findViewById(R.id.btn6),
            findViewById(R.id.btn7),
            findViewById(R.id.btn8),
            findViewById(R.id.btn9)
        )

        val operatorButtons = listOf<Button>(
            findViewById(R.id.btnAdd),
            findViewById(R.id.btnSubtract),
            findViewById(R.id.btnMultiply),
            findViewById(R.id.btnDivide)
        )

        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnEquals = findViewById<Button>(R.id.btnEquals)
        val btnDecimal = findViewById<Button>(R.id.btnDecimal)

        // Definir os listeners para os botões numéricos
        numberButtons.forEach { button ->
            button.setOnClickListener {
                val digit = button.text.toString()
                currentNumber.append(digit)
                updateDisplay()
            }
        }

        // Definir os listeners para os botões de operadores
        operatorButtons.forEach { button ->
            button.setOnClickListener {
                onOperatorButtonClick(button)
            }
        }

        // Listener para o botão de limpar (Clear)
        btnClear.setOnClickListener {
            currentNumber.clear()
            operatorList.clear()
            updateDisplay()
        }

        // Listener para o botão de igual (=)
        btnEquals.setOnClickListener {
            calculateResult()
        }

        // Listener para o botão de ponto decimal (.)
        btnDecimal.setOnClickListener {
            if (!currentNumber.contains(".")) {
                if (currentNumber.isEmpty()) {
                    currentNumber.append("0.")
                } else {
                    currentNumber.append(".")
                }
                updateDisplay()
            }
        }
    }

    private fun onOperatorButtonClick(operatorButton: Button) {
        if (currentNumber.isNotEmpty()) {
            val operator = operatorButton.text.toString()
            val number = currentNumber.toString().toDouble()
            operatorList.add(Pair(operator, number))
            currentNumber.clear()
        }
    }

    private fun calculateResult() {
        if (currentNumber.isNotEmpty()) {
            operatorList.add(Pair("", currentNumber.toString().toDouble()))
            currentNumber.clear()
        }
        if (operatorList.isNotEmpty()) {
            var result = operatorList[0].second
            for (i in 1 until operatorList.size) {
                val operator = operatorList[i].first
                val number = operatorList[i].second
                when (operator) {
                    "+" -> result += number
                    "-" -> result -= number
                    "*" -> result *= number
                    "/" -> {
                        if (number != 0.0) {
                            result /= number
                        } else {
                            displayTextView.text = "Error"
                            return
                        }
                    }
                }
            }
            // Format the result to remove decimal if it's an integer value
            val formattedResult = if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }
            displayTextView.text = formattedResult
            operatorList.clear()
        }
    }

    private fun updateDisplay() {
        displayTextView.text = currentNumber.toString()
    }
}
