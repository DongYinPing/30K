
### Activity的生命周期 ###
> - 正常：onCreate() -> onStart() -> onResume() -> onPause() -> onStop() -> onDestroy()
> - 暂停：onPause() -> onResume()
> - 停止：onRestart() -> onStart() -> onResume()
> - 被杀：onCreate() -> onStart() -> onResume() -> onPause() -> onStop() -> onRestart() ->
    onStart() -> onResume()
> - 横竖屏切换：onPause() -> onSaveInstanceState() -> onStop() -> onDestroy() -> onCreate() ->
    > onResume()

> - Activity 的 onStop/onDestroy 是依赖
    IdleHandler来回调的，正常情况下当主线程空闲时会调用。但是由于某些特殊场景下的问题，导致主线程迟迟无法空闲，onStop/onDestroy
    也会迟迟得不到调用。
> - onResume10s后会触发兜底机制，会主动触发。可以利用 Looper.getMainLooper.setMessageLogging 排查问题。
> - 内存紧张，不一定会调用onStop() 和 onDestroy()

****Activity的活动状态****

> - 1、Running状态:
    一个新的Activity启动入栈后，它在屏幕的最前端，处于栈的最顶端，此时它处于可见并可和用户交互的激活状态，Android试图尽最大可能保持它活动状态，杀死其它Activity来确保当前活动Activity有足够的资源可使用。当另外一个Activity被激活，这个将会被暂停。
> - 2、Paused状态: 当Activity处于此状态时，此时它依然与窗口管理器保持连接，系统继续维护其内部状态，它仍然可见，但它已经失去了焦点，故不可与用户交互。
> - 3、Stopped状态:
    当Activity不可见时，Activity处于Stopped状态。当Activity处于此状态时，一定要保存当前数据和当前的UI状态，否则一旦Activity退出或关闭时，当前的数据和UI状态就丢失了
> - 4、Killed状态: Activity被杀掉以后或者被启动以前，处于Killed状态。这是Activity已从Activity堆栈中移除，需要重新启动才可以显示和使用。

****Activity的启动模式****

> - standard：默认模式，每次启动Activity都是新的。
> - singleTop：栈顶复用模式，栈顶有则复用，并调用其onNewIntent()方法，反之新建
> - singleTask：栈内复用模式，栈内有则复用，并调用其onNewIntent()方法，反之新建，同时移除它上面的。
> - singleInstance：单实例模式，仅有一个，独占一个任务栈。
