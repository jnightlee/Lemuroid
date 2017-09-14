/*
 * MainActivity.kt
 *
 * Copyright (C) 2017 Odyssey Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codebutler.odyssey.feature.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.codebutler.odyssey.feature.game.GameActivity
import com.codebutler.odyssey.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_launch)
        button.setOnClickListener {
            startActivity(GameActivity.newIntent(
                    context = this,
                    coreFileName = "snes9x_libretro_android.so",
                    gameFileName = "Super Mario All-Stars (U) [!].smc"))
        }
    }
}