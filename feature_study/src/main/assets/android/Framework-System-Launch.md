### Android系统启动流程 ###

系统进程的启动顺序：前三步做热身, 后面4步启动不同的进程!

Row->BootLoader->Linux内核->init进程-->Zygote进程-->SystemServer进程-->Launcher应用进程

![启动流程图](https://upload-images.jianshu.io/upload_images/11218161-04be1ab30b8fa8aa.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

> 1).启动电源以及系统启动（Rom）

当电源按下时引导芯片代码开始从预定义的地方（固化在ROM）开始执行。加载引导程序Bootloader到RAM，然后执行。

> 2).引导程序BootLoader

引导程序BootLoader是在Android操作系统开始运行前的一个小程序，它的主要作用是把系统OS拉起来并运行。

> 3).Linux内核启动（kernel）

内核启动时，设置缓存、被保护存储器、计划列表、加载驱动。当内核完成系统设置，它首先在系统文件中寻找init.rc文件，并启动init进程。

> 4).init进程启动

初始化和启动属性服务，并且启动Zygote进程。

> 5).Zygote进程启动

创建JavaVM并为JavaVM注册JNI，创建服务端Socket，启动SystemServer进程。

> 6).SystemServer进程启动

启动Binder线程池和SystemServiceManager，并且启动各种系统服务。

> 7).Launcher启动

被SystemServer进程启动的ActivityManagerService会启动Launcher，Launcher启动后会将已安装应用的快捷图标显示到界面上。


**init 进程总结**
> 1).init进程创建: init是最早的进程,通过脚本拉起来

> 2).zygote/serviceManager, SurfaceFinger, mediaService创建: init创建了这些核心的服务

> 3). init 进程守护其他进程

**Zygote 进程总结**
> 1). inti创建Zygote进程, 创建AppRuntime(虚拟机)并调用其start方法，启动Zygote进程

> 2). (java层启动)通过JNI调用ZygoteInit的main函数进入Zygote的Java框架层。(ZygoteInit.java).换句换说Zygote开创了Java框架层

> 3). 预加载资源: openGl/android.jar

> 4). 先启动的systemServer: Zygote 进程 fork 创建systemServer()

**SystemServer 进程总结**
> 1).启动Binder线程池，这样就可以与其他进程进行通信。（通过java调用c启动的）

> 2).创建SystemServiceManager用于对系统的服务进行创建、启动和生命周期管理。

> 3).启动各种系统服务,AMS,PMS, WMS, watchDog, systemUI

