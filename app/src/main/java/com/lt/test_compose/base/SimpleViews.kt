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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import com.lt.test_compose.base.h
import com.lt.test_compose.base.w

/**
 * creator: lt  2021/4/13  lt.dygzs@qq.com
 * effect : 简单的常用的控件
 * warning:
 */

/**
 * 间隔view
 */
@Composable
fun HorizontalSpace(dp: Int) {
    Spacer(w(dp))
}

@Composable
fun VerticalSpace(dp: Int) {
    Spacer(h(dp))
}