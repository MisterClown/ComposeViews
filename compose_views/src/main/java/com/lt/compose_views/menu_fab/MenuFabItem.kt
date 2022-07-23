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

package com.lt.compose_views.menu_fab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/23 15:22
// Description: 
// Documentation:

/**
 * Fab弹出菜单项
 *
 * @param icon 菜单图标
 * @param label 菜单提示文本
 * @param srcIconColor 图标颜色
 * @param labelTextColor 提示文本内容颜色
 * @param labelBackgroundColor 提示文本内容区域背景色
 * @param fabBackgroundColor Fab按钮背景色
 */
class MenuFabItem(
    val icon: @Composable ()->Unit,
    val label: String,
    val srcIconColor: Color = Color.White,
    val labelTextColor: Color = Color.White,
    val labelBackgroundColor: Color = Color.Black.copy(alpha = 0.6F),
    val fabBackgroundColor: Color = Color.Unspecified,
)