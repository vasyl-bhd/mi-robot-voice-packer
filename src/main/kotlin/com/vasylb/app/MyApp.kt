package com.vasylb.app

import com.vasylb.Styles
import com.vasylb.view.MainView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus
import tornadofx.reloadViewsOnFocus

class MyApp: App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        super.start(stage)

        stage.height = 600.0
        stage.width = 800.0
    }
    init {
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()
    }
}