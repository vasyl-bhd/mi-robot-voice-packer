package com.vasylb

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val input by cssclass()
        val button by cssclass()
        val row by cssclass()
        val inputContainer by cssclass()
        val scrollPane by cssclass()
    }

    init {
        inputContainer {
            padding = box(5.px, 0.px)
        }
        row {
            padding = box(0.px, 15.px, 0.px, 5.px)
        }
        input {
            prefWidth = 1500.px
            maxWidth = 1500.px
        }
        button {
            prefWidth = 100.px
            minWidth = 100.px
        }
        scrollPane {
            maxWidth = 1500.px
        }
    }
}