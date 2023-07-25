package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView
    private var currentNumber = StringBuilder()
    private var currentOperator = ""
    private var previousValue = 0.0

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
            clearCalculator()
        }

        // Listener para o botão de igual (=)
        btnEquals.setOnClickListener {
            performCalculation()
            currentOperator = ""
        }

        // Listener para o botão de ponto decimal (.)
        btnDecimal.setOnClickListener {
            addDecimalPoint()
        }
    }

    private fun onOperatorButtonClick(operatorButton: Button) {
        if (currentOperator.isNotEmpty()) {
            performCalculation()
        }
        currentOperator = operatorButton.text.toString()
        previousValue = currentNumber.toString().toDouble()
        currentNumber.clear()
    }

    private fun performCalculation() {
        if (currentOperator.isNotEmpty() && currentNumber.isNotEmpty()) {
            val currentValue = currentNumber.toString().toDouble()
            when (currentOperator) {
                "+" -> previousValue += currentValue
                "-" -> previousValue -= currentValue
                "*" -> previousValue *= currentValue
                "/" -> {
                    if (currentValue != 0.0) {
                        previousValue /= currentValue
                    } else {
                        displayTextView.text = "Error"
                        return
                    }
                }
            }
            currentNumber.clear()
            currentNumber.append(previousValue)
            updateDisplay()
        }
    }

    private fun addDecimalPoint() {
        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty()) {
                currentNumber.append("0.")
            } else {
                currentNumber.append(".")
            }
            updateDisplay()
        }
    }

    private fun clearCalculator() {
        currentNumber.clear()
        currentOperator = ""
        previousValue = 0.0
        updateDisplay()
    }

    private fun updateDisplay() {
        displayTextView.text = currentNumber.toString()
    }
}
