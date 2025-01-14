/*
 * Copyright lt 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lt.test_compose.base

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

/**
 * creator: lt  2021/4/13  lt.dygzs@qq.com
 * effect : 修饰符扩展函数
 * warning:
 */

typealias M = Modifier

const val VIEW_CLICK_INTERVAL_TIME = 800


/**
 * 防止重复点击(有的人可能会手抖连点两次,造成奇怪的bug)
 */
@Composable
inline fun Modifier.click(
    time: Int = VIEW_CLICK_INTERVAL_TIME,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = LocalIndication.current,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableStateOf(value = 0L) }
    return clickable(interactionSource, indication, enabled, onClickLabel, role) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - time >= lastClickTime) {
            onClick()
            lastClickTime = currentTimeMillis
        }
    }
}

/**
 * 防止重复点击,比如用在Button时直接传入onClick函数
 */
@Composable
inline fun composeClick(
    time: Int = VIEW_CLICK_INTERVAL_TIME,
    crossinline onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableStateOf(value = 0L) }
    return {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - time >= lastClickTime) {
            onClick()
            lastClickTime = currentTimeMillis
        }
    }
}