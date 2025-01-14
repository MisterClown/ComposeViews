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

package com.lt.compose_views.compose_pager

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.lt.compose_views.util.clipScrollableContainer
import com.lt.compose_views.util.midOf

/**
 * creator: lt  2022/6/25  lt.dygzs@qq.com
 * effect : 类似于xml中的ViewPager
 * warning:
 * [pageCount]一共有多少页
 * [modifier]修饰
 * [composePagerState]ComposePager的状态
 * [orientation]滑动的方向
 * [userEnable]用户是否可以滑动,等于false时用户滑动无反应,但代码可以执行翻页
 * [content]compose内容区域
 */
@Composable
fun ComposePager(
    pageCount: Int,
    modifier: Modifier = Modifier,
    composePagerState: ComposePagerState = rememberComposePagerState(),
    orientation: Orientation = Orientation.Horizontal,
    userEnable: Boolean = true,
    content: @Composable ComposePagerScope.() -> Unit
) {
    //检查索引是否在页数内
    LaunchedEffect(key1 = pageCount, block = {
        if (pageCount <= 0)
            return@LaunchedEffect
        else if (pageCount <= composePagerState.getCurrSelectIndex()) {
            composePagerState.currSelectIndex.value = pageCount - 1
            composePagerState.setPageIndex(pageCount - 1)
        }
    })
    if (pageCount <= composePagerState.getCurrSelectIndex())
        return

    //滑动监听
    val draggableState = rememberDraggableState {
        //停止之前的动画
        composePagerState.pageChangeAnimFlag = null
        val min = if (composePagerState.currSelectIndex.value + 1 >= pageCount)
            0f else -composePagerState.mainAxisSize.toFloat()
        val max = if (composePagerState.currSelectIndex.value <= 0)
            0f else composePagerState.mainAxisSize.toFloat()
        composePagerState.mOffset = midOf(min, (composePagerState.mOffset ?: 0f) + it, max)
    }
    //放置的三个compose元素的scope,分别是0,1,2
    val composePagerScope0 = remember(composePagerState.currSelectIndex.value) {
        ComposePagerScope(composePagerState.currSelectIndex.value - 1)
    }
    val composePagerScope1 = remember(composePagerState.currSelectIndex.value) {
        ComposePagerScope(composePagerState.currSelectIndex.value)
    }
    val composePagerScope2 = remember(composePagerState.currSelectIndex.value) {
        ComposePagerScope(composePagerState.currSelectIndex.value + 1)
    }

    //处理offset
    LaunchedEffect(
        key1 = composePagerState.mOffset,
        key2 = orientation,
        block = {
            val offset = composePagerState.mOffset ?: return@LaunchedEffect
            composePagerState.offsetAnim.snapTo(
                offset - composePagerState.currSelectIndex.value * composePagerState.mainAxisSize
            )
        })
    //处理翻页动画
    LaunchedEffect(
        key1 = composePagerState.pageChangeAnimFlag,
        block = {
            val flag = composePagerState.pageChangeAnimFlag
            if (flag == null) {
                if (composePagerState.offsetAnim.isRunning)
                    composePagerState.offsetAnim.stop()
                return@LaunchedEffect
            }
            try {
                val index = composePagerState.currSelectIndex.value
                when (flag) {
                    PageChangeAnimFlag.Prev -> {
                        if (index <= 0)
                            return@LaunchedEffect
                        composePagerState.mOffset = null
                        try {
                            composePagerState.offsetAnim.animateTo(-(index - 1) * composePagerState.mainAxisSize.toFloat())
                        } finally {
                            composePagerState.currSelectIndex.value = index - 1
                            composePagerState.mOffset = 0f
                        }
                    }
                    PageChangeAnimFlag.Next -> {
                        if (index + 1 >= pageCount)
                            return@LaunchedEffect
                        composePagerState.mOffset = null
                        try {
                            composePagerState.offsetAnim.animateTo(-(index + 1) * composePagerState.mainAxisSize.toFloat())
                        } finally {
                            composePagerState.currSelectIndex.value = index + 1
                            composePagerState.mOffset = 0f
                        }
                    }
                    PageChangeAnimFlag.Reduction -> {
                        composePagerState.offsetAnim.animateTo(-index * composePagerState.mainAxisSize.toFloat())
                    }
                    is PageChangeAnimFlag.GoToPageNotAnim -> {
                        composePagerState.currSelectIndex.value = flag.index
                        composePagerState.offsetAnim.snapTo(-flag.index * composePagerState.mainAxisSize.toFloat())
                    }
                }
            } finally {
                composePagerState.pageChangeAnimFlag = null
            }
        })

    //测量和放置compose元素
    Layout(
        content = {
            if (composePagerScope0.index < 0)
                Box(modifier = Modifier)
            else
                composePagerScope0.content()
            composePagerScope1.content()
            if (composePagerScope2.index >= pageCount)
                Box(modifier = Modifier)
            else
                composePagerScope2.content()
        },
        modifier = modifier
            .draggable(draggableState, orientation, enabled = userEnable, onDragStarted = {
                composePagerState.mOffset = 0f
                composePagerState.onUserDragStarted?.invoke(this, it)
            }, onDragStopped = {
                val index = composePagerState.currSelectIndex.value
                if (composePagerState.offsetAnim.value + it > -(index * composePagerState.mainAxisSize - composePagerState.mainAxisSize / 2)) {
                    composePagerState.pageChangeAnimFlag = PageChangeAnimFlag.Prev
                } else if (composePagerState.offsetAnim.value + it < -(index * composePagerState.mainAxisSize + composePagerState.mainAxisSize / 2)) {
                    composePagerState.pageChangeAnimFlag = PageChangeAnimFlag.Next
                } else {
                    composePagerState.pageChangeAnimFlag = PageChangeAnimFlag.Reduction
                }
                composePagerState.onUserDragStopped?.invoke(this, it)
            })
            .clipScrollableContainer(orientation == Orientation.Vertical)
    ) { measurables/* 可测量的(子控件) */, constraints/* 约束条件 */ ->
        var width = 0
        var height = 0
        //测量子元素,并算出他们的最大宽度
        val placeables = measurables.map {
            val placeable = it.measure(constraints)
            width = maxOf(width, placeable.width)
            height = maxOf(height, placeable.height)
            placeable
        }
        composePagerState.mainAxisSize =
            if (orientation == Orientation.Horizontal) width else height
        //设置自身大小,并布局子元素
        layout(width, height) {
            val animValue = composePagerState.offsetAnim.value.toInt()
            val selectIndex = composePagerState.currSelectIndex.value
            placeables.forEachIndexed { index, placeable ->
                val offset = (index + selectIndex - 1) * composePagerState.mainAxisSize + animValue
                //遍历放置子元素
                if (orientation == Orientation.Horizontal)
                    placeable.placeRelative(
                        x = offset,
                        y = 0
                    )//placeRelative可以适配从右到左布局的放置子元素,place只适用于从左到右的布局
                else
                    placeable.placeRelative(
                        x = 0,
                        y = offset
                    )
            }
        }
    }
}