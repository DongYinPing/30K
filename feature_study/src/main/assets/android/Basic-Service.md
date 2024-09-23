> 1、非 bind 方式启动

startService
stopService
生命周期
onCreate 只执行一次
onStartCommand ，启动一次执行一次
onStart
onDestroy
> 2、bind 方式启动

bindService
onUnBindService
生命周期
onCreate 只执行一次
onBind
onUnBind
onDestroy

****IntertService****