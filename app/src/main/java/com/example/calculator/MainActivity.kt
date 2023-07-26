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
    private var currentOperator = ""
    private var previousValue: Double? = null

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
                if (currentNumber.isNotEmpty()) {
                    onOperatorButtonClick(button)
                }
            }
        }

        // Listener para o botão de limpar (Clear)
        btnClear.setOnClickListener {
            currentNumber.clear()
            currentOperator = ""
            previousValue = null
            updateDisplay()
        }

        // Listener para o botão de igual (=)
        btnEquals.setOnClickListener {
            if (currentNumber.isNotEmpty() && currentOperator.isNotEmpty() && previousValue != null) {
                performCalculation()
                currentOperator = ""
            }
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
        if (previousValue == null) {
            previousValue = currentNumber.toString().toDouble()
        } else {
            performCalculation()
        }
        currentOperator = operatorButton.text.toString()
        currentNumber.clear()
    }

    private fun performCalculation() {
        val currentValue = currentNumber.toString().toDouble()
        when (currentOperator) {
            "+" -> previousValue = previousValue!! + currentValue
            "-" -> previousValue = previousValue!! - currentValue
            "*" -> previousValue = previousValue!! * currentValue
            "/" -> {
                if (currentValue != 0.0) {
                    previousValue = previousValue!! / currentValue
                } else {
                    displayTextView.text = "Error"
                    return
                }
            }
        }
        currentNumber.clear()
        // Formatar o resultado para exibir apenas duas casas decimais (ou sem casas decimais se for um número inteiro)
        val formattedResult = if (previousValue!!.isWholeNumber()) {
            previousValue!!.toInt().toString()
        } else {
            String.format("%.2f", previousValue)
        }
        currentNumber.append(formattedResult)
        updateDisplay()
    }

    private fun updateDisplay() {
        displayTextView.text = currentNumber.toString()
    }
}

// Função de extensão para verificar se um Double é um número inteiro
fun Double.isWholeNumber() = this % 1.0 == 0.0