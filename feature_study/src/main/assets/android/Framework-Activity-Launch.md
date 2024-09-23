### Activity的启动流程 ###
![启动流程图](https://upload-images.jianshu.io/upload_images/11218161-48daeb099bb68e98.jpg?imageMogr2/auto-orient/strip|imageView2/2/format/webp)

点击桌面App图标，Activity的启动流程

> 1、发起startActivity请求:
> 
> Launcher进程-->>Binder>>--SystemServer进程

发起startActivity请求,通过AIDL方式使用Binder机制告诉AMS要启动应用的需求;

> 2、发起新建App进程请求:
> 
> SystemServer进程-->>Socket>>---Zygote进程 

AMS收到需求后，反馈Launcher，让Launcher进入Paused状态 .Launcher进入Paused状态，AMS通过socket与Zygote通信，告知Zygote需要新建进程。

> 3、Fork出新的子进程:  
> 
> Zygote进程--->>Socket>>---App进程 

Zygote进程fork出新的子进程，即App进程；并调用ActivityThread的main方法，也就是app的入口。还会创建Binder 线程池（ProcessState.startThreadPool()

> 4、AttachApplication请求:
> 
> App进程--->>Binder>>---SystemServer进程

SystemServer进程在收到Attach Application请求后，进行一系列准备工作后，新建了ActivityThread实例，进入ActivityThread.main()方法，开始创建Application，Provider，并调用Application的attach，onCreate方法，并新建了Looper实例，开始loop循环。

> 5、scheduleLaunchActivity请求:
> 
> SystemServer进程--->>Binder>>---App进程

AMS处理后，向APP进程发送scheduleLaunchActivity请求，于是乎，activity真正启动了。是scheduleLaunchActivity

> 6、App进程-->>Handler>>---主线程

App进程的binder线程（ApplicationThread）在收到请求后，通过handler向主线程发送消息 (Activity)

> 7、主线程-->>发射机制>>---创建Activity

主线程在收到Message后，创建上下文，通过类加载器加载Activity，并回调Activity.onCreate()等方法


