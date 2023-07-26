package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView
    private var currentNumber = StringBuilder()
    private var currentOperator = ""
    private var previousValue = 0.0

    private lateinit var imageView: ImageView
    private val imageList = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        // Adicione aqui os IDs das outras imagens (image4, image5, etc.)
    )
    private var currentImageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.displayTextView)
        imageView = findViewById(R.id.imageView)

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
            currentOperator = ""
            previousValue = 0.0
            updateDisplay()
        }

        // Listener para o botão de igual (=)
        btnEquals.setOnClickListener {
            performCalculation()
            currentOperator = ""
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

        // Iniciar o runnable para trocar as imagens a cada 2 segundos
        val handler = Handler()
        val imageChangeRunnable = object : Runnable {
            override fun run() {
                currentImageIndex = (currentImageIndex + 1) % imageList.size
                imageView.setImageResource(imageList[currentImageIndex])
                handler.postDelayed(this, 5000) // Trocar a imagem a cada 2 segundos (2000 milissegundos)
            }
        }
        handler.postDelayed(imageChangeRunnable, 5000) // Iniciar a troca das imagens após 2 segundos
    }

    private fun onOperatorButtonClick(operatorButton: Button) {
        if (currentNumber.isNotEmpty()) {
            performCalculation()
        }
        currentOperator = operatorButton.text.toString()
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
            // Formatar o resultado para exibir apenas duas casas decimais (ou sem casas decimais se for um número inteiro)
            val formattedResult = if (previousValue.isWholeNumber()) {
                previousValue.toInt().toString()
            } else {
                String.format("%.2f", previousValue)
            }
            currentNumber.append(formattedResult)
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        displayTextView.text = currentNumber.toString()
    }

    // Função de extensão para verificar se um Double é um número inteiro
    private fun Double.isWholeNumber() = this % 1.0 == 0.0
}
