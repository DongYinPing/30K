package com.app.list.item

/**
 * 自定义布局
 */
class ItemLayout<T>(var data: T) {
    /**
     * id
     */
    var id = 0

    /**
     * 布局
     */
    var viewType = 0

    /**
     * 在列表中的位置
     */
    var position = 0

    /**
     * 宽，用于自定义动态改变UI
     */
    var width = 0

    /**
     * 高，用于自定义动态改变UI
     */
    var height = 0

    /**
     * 能否点击(默认true)
     */
    var clickable = true

    /**
     * 扩展的obj
     */
    var extObj: Any? = null

    override fun toString(): String {
        return "ItemLayout(data=$data, id=$id, viewType=$viewType, position=$position, " +
            "width=$width, height=$height, clickable=$clickable, extObj=$extObj)"
    }
}
