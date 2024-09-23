****关于事件分发的总结****
事件分发里面重要的三个方法

> `dispatchTouchEvent()`
>
在 Activity 、 ViewGroup、 View 中都是有这个方法的，事件接收是从 Activity 的 dispatchTouchEvent(） 开始的。

> `onInterceptTouchEvent()`
>
这个方法是 ViewGroup 独有的，在 ViewGroup 中可以通过这个方法确定是否拦截该事件，如果拦截，那么就由
ViewGroup 的 onTouchEvent（） 方法接管事件序列。这个方法默认是返回 false 的，也就是默认不拦截事件

> `onTouchEvent()`
>
这个方法也是在 Activity 、 ViewGroup 、 View 中都有的，如果如果确定在 Activity、ViewGroup、View
中处理事件，一般是在这个方法处理的。
View 的 onTouchListener 优先级高于 onTouchEvent 的优先级，onTouchEvent 的优先级高于 onClickListener
onClick 的优先级。


> 1 事件分发里面的事件通常指 ACTION_DOWN、ACTION_MOVE、ACTION_UP、ACTION_CANCEL 这四种，他们连在一起就构成了一个时间序列。比如
> ACTION_DOWN、ACTION_MOVE、ACTION_MOVE…ACTION_MOVE、ACTION_UP，这就构成了一个事件序列。

> 2 事件序列的传递是从 Activity 开始，依次经过、PhoneWindow、DecorView、ViewGroup、View。如果是最终的 View
> 也没有处理的话，就依次向上移交，最终会在 Activity 的 onTouchEvent 方法中处理。

> 3 如果事件从 ViewGroup 中传递给 View 去处理的时候，如果 View 没有处理掉，在 onTouchEvent 方法中返回了
> false，那么该事件就重新交给 ViewGroup 处理，并且后续的事件都不会再传递给该 View。

> 4 onInterceptTouchEvent 方法只有在 ViewGroup 中存在，并且默认返回 false，代表 ViewGroup 不拦截事件。

> 5 正常情况下，一个事件序列只能由一个 View 处理。如果一个 View 接管了事件，不管是具体的子 View还是
> ViewGroup，后续的事件都会让这个 View 处理，除非人为干预事件的分发过程。

> 6 子 View 可以通过调用 requestDisallowInterceptTouchEvent(true) ，干预父元素的除了 ACTION_DOWN
> 事件以外的事件走向。 一般用于处理滑动冲突中，子控件请求父控件不拦截ACTION_DOWN以外的其他事件，ACTION_DOWN事件不受影响。

> 7 View 的 onTouchEvent 方法默认是返回 true 的，也就是会默认拦截事件。除非 它是不可点击的，(
> clickable、longClickable 都为 false)。View 的 longClickable 默认均为 false，Button、ImageButton 的
> clickable 默认为 true，TextView clickable 默认为false

> 8 View 的 enable 属性不会影响 onTouchEvent 的返回值，只要 clickable、longClickable 有一个为
> true，那么onTouchEvent就默认会返回 true

> 9 View 的点击事件是在 ACTION_UP 事件处理的时候执行的，所以要执行，必须要有 ACTION_DOWN 和 ACTION_UP
> 两个事件。
