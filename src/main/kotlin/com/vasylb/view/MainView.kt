package com.vasylb.view

import com.vasylb.Styles.Companion.button
import com.vasylb.Styles.Companion.input
import com.vasylb.Styles.Companion.inputContainer
import com.vasylb.Styles.Companion.row
import com.vasylb.Styles.Companion.scrollPane
import com.vasylb.controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    private val mainController: MainController by inject()


    override val root = gridpane {
        scrollpane(true) {
            gridpaneConstraints {
                columnRowIndex(0, 0)
            }
            gridpaneColumnConstraints {
                percentWidth = 80.0
            }
            addClass(scrollPane)

            content = vbox(10) {
                addClass(inputContainer)
                children.addAll(renderInputs())
            }
        }

        vbox(10, Pos.TOP_CENTER) {
            gridpaneConstraints { columnRowIndex(1, 0) }
            gridpaneColumnConstraints {
                percentWidth = 20.0
            }
            addClass(inputContainer)

            button("Open Folder") {
                addClass(button)
                setOnAction { mainController.openFolder() }
            }
            button("Package") {
                addClass(button)
                setOnAction {mainController.createPkg()}
            }
        }
    }

    private fun renderInputs(): List<HBox> {
        return mainController.observableAudioFileNames
            .map { this.toView(it) }
            .toList()

    }

    private fun toView(entry: Map.Entry<String, SimpleStringProperty>): HBox {
        return hbox(5, Pos.BOTTOM_RIGHT) {
            addClass(row)
            vbox {
                label(entry.key) {
                    addClass(input)
                }

                textfield(entry.value) {
                    addClass(input)
                    isDisable = true
                }
            }
            button("...") {
                addClass(button)
                setOnAction { mainController.openFile() }
            }
//            button("Record") {
//                addClass(button)
//            }

        }
    }
}
