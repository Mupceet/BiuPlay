# Android小技巧： 这里涵盖了所有实现 “一键退出 App” 的方法

>* 作者：Carson_Ho
>* 链接：https://www.jianshu.com/p/269873a16937
>* 來源：简书
>* 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

# 前言

*   在 `Android`开发中，会经常存在 “一键退出`App`” 的需求

*   但市面上流传着 **太多不可用的“一键退出`App`”功能实现**

*   本文将全面总结“一键退出`App`”的实现方式，并为你一一实践，希望你们会喜欢。

* * *

# 目录

![](https://upload-images.jianshu.io/upload_images/944365-2c469a33d1195b3b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

* * *

# 1\. 需求本质

一键退出 `App` 其实是 两个需求：

1.  一键结束当前`App`所有的`Activity`

2.  一键结束当前`App`进程

**即 需要2个步骤 才可 完成 一键退出 `App` 需求**。下面，我将根据这两个步骤进行功能实现讲解。

* * *

# 2\. 功能实现

### 2.1 （步骤1）一键结束当前 App 所有 Activity

##### 2.1.1 实现方法类型

*   主要分为2类：通过 `Android`组件 & 自身实现

*   具体如下图：

![](https://upload-images.jianshu.io/upload_images/944365-38816f15415e2726.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

注：上述方法仅仅只是结束当前`App`所有的`Activity` （在用户的角度确实是退出了 `App`），但实际上该`App`的进程还未结束。

#### 2.1.2 具体介绍

##### a. 通过 Android 组件：Activity

**方法1：采用Activity启动模式：SingleTask**

*   原理

    1.  将 `App`的入口 `Activity` 采用 `SingleTask` 启动模式

    > a. 入口 `Activity` 此时处于栈底
    > 
    > b. 关于 `SingleTask`的原理如下：
    > 
    > ![](https://upload-images.jianshu.io/upload_images/944365-a17b0f15616f42b1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

    2.  当需要退出 `App`时启动入口 `Activity`

    > 此时入口 `Activity` 上层的`Activity`实例都将自动关闭移除 & 自身被放置在栈顶（这是`SingleTask`启动模式的特点）

    3.  通过在入口 `Activity` 回调的`onNewIntent()`中关闭自身即可

    > 若在后面的`Activity`启动 任务栈底的`Activity`时，就会调用任务栈底`Activity`的`onNewIntent()`

*   具体实现

步骤1：将 `App`的入口 `Activity` 设置成 `SingleTask` 启动模式

```
// AndroidMainifest.xml中的Activity配置进行设置

<activity

android:launchMode="singleTask"
//属性
//standard：标准模式
//singleTop：栈顶复用模式
//singleTask：栈内复用模式
//singleInstance：单例模式
//如不设置，Activity的启动模式默认为 标准模式（standard）
</activity>

```

步骤2：在入口 `Activity`重写 `onNewIntent()`

```
// 在该方法传入一标志位标识是否要退出App & 关闭自身
  @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            // 是否退出App的标识
            boolean isExitApp = intent.getBooleanExtra("exit", false);
            if (isExitApp) {
                // 关闭自身
                this.finish();
            }
        }
    }

```

步骤3：在需要退出时调用 `exitApp()`

```
private void exitApp() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("exit", true);
        context.startActivity(intent);

         // 结束进程
        // System.exit(0);
    }

```

*   优点

    使用简单 & 方便

*   缺点

    1.  规定 **App的入口Activity采用SingleTask启动模式**

    2.  使用范围局限：只能结束当前任务栈的Activity，若出现多任务栈（即采用`SingleInstance`启动模式）则无法处理

*   应用场景

    Activity单任务栈

* * *

### 方法2：采用Activity启动标记位

*   原理：对入口`Activity`采用 2 标记位：

    1.  `Intent.FLAG_ACTIVITY_CLEAR_TOP`：销毁目标`Activity`和它之上的所有`Activity`，重新创建目标`Activity`

    2.  `Intent.FLAG_ACTIVITY_SINGLE_TOP`：若启动的`Activity`位于任务栈栈顶，那么此`Activity`的实例就不会重建，而是重用栈顶的实例( 调用`onNewIntent()`）

*   具体使用（从`MainActivity`（入口`Activity`） 跳转到 `Activity2` & 一键退出）

步骤1：在`MainActivity` 中设置 重写 `onNewIntent()`

*MainActivity.java*

```
        // 设置 按钮 跳转到Activity2
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Activity2.class));

            }

        });
    }

    // 在onNewIntent（）传入一标识符
    // 作用：标识是否要退出App
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            // 是否退出App的标识
            boolean isExitApp = intent.getBooleanExtra("exit", false);
            if (isExitApp) {
                // 关闭自身
                this.finish();
            }
        }
        // 结束进程
        // System.exit(0);
    }
}

```

步骤2：在需要退出的地方（`Activity2`）启动`MainActivity` & 设置标记位

```
// 当需要退出时，启动入口Activity
                Intent intent = new Intent();
                intent.setClass(Activity2.this, MainActivity.class);

                // 设置标记位
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // 步骤1：该标记位作用：销毁目标Activity和它之上的所有Activity，重新创建目标Activity

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // 步骤2：若启动的Activity位于任务栈栈顶，那么此Activity的实例就不会重建，而是重用栈顶的实例( 调用实例的 onNewIntent() )

                // 在步骤1中：MainActivity的上层的Activity2会被销毁，此时MainActivity位于栈顶；由于步骤2的设置，所以不会新建MainActivity而是重用栈顶的实例&调用实onNewIntent()

                // 传入自己设置的退出App标识
                intent.putExtra("exit", true);

                startActivity(intent);

```

*   优点

    使用简单 & 方便

*   缺点

    使用范围局限：只能结束当前任务栈的Activity，若出现多任务栈（即采用`SingleInstance`启动模式）则无法处理

*   应用场景

    Activity单任务栈

* * *

### 方法3：通过系统任务栈

*   原理：通过 `ActivityManager` 获取当前系统的任务栈 & 把栈内所有`Activity`逐个退出

*   具体使用

```
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

        // 1\. 通过Context获取ActivityManager
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        // 2\. 通过ActivityManager获取任务栈
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();

        // 3\. 逐个关闭Activity
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
        // 4\. 结束进程
        // System.exit(0);

```

*   优点

    使用简单、方便

*   缺点

    1.  使用范围局限：只能结束当前任务栈的Activity，若出现多任务栈（即采用`SingleInstance`启动模式）则无法处理

    2.  对 `Android` 版本要求较高：`Android 5.0`以上

*   应用场景

    `Android 5.0`以上的 `Activity`单任务栈

* * *

#### b. 通过 Android 组件：  BroadcastReceiver

即使用 `BroadcastReceiver` 广播监听

*   原理：在每个 `Activity` 里注册广播接收器（响应动作 = 关闭自身）；当需要退出 `App` 时 发送广播请求即可

*   具体实现

步骤1：自定义广播接收器

```
public class ExitAppReceiver extends BroadcastReceiver {
    private Activity activity;

    public ExitAppReceiver(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.finish();
    }
}

```

步骤2：在每个  `Activity` 里注册广播接收器（响应动作 = 关闭自身）

```
public class Activity extends AppCompatActivity {

private  ExitAppReceiver mExitAppReceiver；

// 1\. 在onCreate（）中注册广播接收器
 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExitAppReceiver = new ExitAppReceiver(this);
        registerReceiver(mExitAppReceiver,new IntentFilter(BaseApplication.EXIT));
    }

// 1\. 在onDestroy（）中注销广播接收器
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mExitAppReceive);
    }

```

步骤3：当需要退出App时 发送广播请求

```
context.sendBroadcast(new Intent(BaseApplication.EXIT));
// 注：此处不能使用：System.exit(0);结束进程
// 原因：发送广播这个方法之后，不会等到广播接收器收到广播，程序就开始执行下一句System.exit(0)，然后就直接变成执行System.exit(0)的效果了。

```

*   优点

    应用场景广泛：兼顾单 / 多任务栈 & 多启动模式的情况

*   缺点

    实现复杂：需要在每个 `Activity` 里注册广播接收器

*   应用场景

    任意情况下的一键退出 App，但无法终止 `App` 进程

> 所以该方法仅仅是在用户的角度来说  **“一键退出App”**

* * *

### c.  自身实现

##### 方法1：创建 链表

*   原理：通过在`Application`子类中建立一个 `Activity`链表：保存正在运行的`Activity`实例；当需要一键退出`App`时把链表内所有`Activity`实例逐个退出即可

*   具体使用

步骤1：在`BaseApplication`类的子类里建立`Activity`链表

*Carson_BaseApplicaiton.java*

```
public class Carson_BaseApplicaiton extends Application {

    // 此处采用 LinkedList作为容器，增删速度快
    public static LinkedList<Activity> activityLinkedList;

    @Override
    public void onCreate() {
        super.onCreate();

        activityLinkedList = new LinkedList<>();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "onActivityCreated: " + activity.getLocalClassName());
                activityLinkedList.add(activity);
                // 在Activity启动时（onCreate()） 写入Activity实例到容器内
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "onActivityDestroyed: " + activity.getLocalClassName());
                activityLinkedList.remove(activity);
                // 在Activity结束时（Destroyed（）） 写出Activity实例
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

        });
    }

    public  void exitApp() {

        Log.d(TAG, "容器内的Activity列表如下 ");
        // 先打印当前容器内的Activity列表
        for (Activity activity : activityLinkedList) {
            Log.d(TAG, activity.getLocalClassName());
        }

        Log.d(TAG, "正逐步退出容器内所有Activity");

        // 逐个退出Activity
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }

        //  结束进程
        // System.exit(0);
    }
}

// 记得在Manifest.xml中添加
<application
        android:name=".Carson_BaseApplicaiton"
        ....
</application>

```

步骤2：需要一键退出 `App` 时，获取该 `Applicaiton`类对象 & 调用`exitApp()`

```
                private Carson_BaseApplicaiton app;

                app = (Carson_BaseApplicaiton)getApplication();
                app.exitApp();

```

*   效果图

![](https://upload-images.jianshu.io/upload_images/944365-566f098580e5a362.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/390)

![](https://upload-images.jianshu.io/upload_images/944365-5097191864eeabc0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

*   优点

    应用场景广泛：兼顾单 / 多任务栈 & 多启动模式的情况

*   缺点

    需要 `Activity` 经历正常的生命周期，即创建时调用`onCreate（）`，结束时调用`onDestroy（）`

> 因为只有这样经历正常的生命周期才能将 `Activity`正确写入 & 写出 容器内

*   应用场景

    任意情况下的一键退出 `App` 实现

* * *

### 方法2：RxBus

*   原理：使用 `RxBus`当作事件总线，在每个 `Activity`里注册`RxBus`订阅（响应动作 = 关闭自身）；当需要退出App时 发送退出事件请求即可。

*   具体使用

步骤1：在每个 `Activity`里注册`RxBus`订阅（响应动作 = 关闭自身）

```
public class Activity extends AppCompatActivity {
  private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        // 注册RxBus订阅
        disposable = RxBus.getInstance().toObservable(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        // 响应动作 = 关闭自身
                        if (s.equals("exit")){
                            finish();
                        }
                    }
                });
    }

// 注意一定要取消订阅
 @Override
    protected void onDestroy() {
        if (!disposable.isDisposed()){
              disposable.dispose();;
        }
}

```

步骤2：当需要退出App时 发送退出事件

```
        RxBus.getInstance().post("exit");
        System.exit(0);

```

*   优点

    可与 `RxJava` & `RxBus` 相结合

*   缺点

    实现复杂：`RxBus`  本身的实现难度 & 需要在每个`Activity`注册和取消订阅 `RxBus` 使用

*   应用场景

    需要与`RxJava` 结合使用时

> 若项目中没有用到`RxJava` & `RxBus` 不建议使用

*   至此，一键结束当前 `App`的所有 `Activity`的 方法 讲解完毕。

*   注：上述方法仅仅只是结束当前`App`所有的`Activity` （在用户的角度确实是退出了 `App`），但实际上该`App`的进程还未结束

* * *

# 2.2 （步骤2）一键结束当前 App 进程

主要采用 `Dalvik VM`本地方法

*   作用

    结束当前 `Activity` & 结束进程

> 即 在 （步骤1）结束当前 `App` 所有的 `Activity` 后，调用该方法即可一键退出 `App`（更多体现在结束进程上）

*   具体使用

```
// 方式1：android.os.Process.killProcess（）
  android.os.Process.killProcess(android.os.Process.myPid()) ；

// 方式2：System.exit()
// System.exit() = Java中结束进程的方法：关闭当前JVM虚拟机
  System.exit(0);  

// System.exit(0)和System.exit(1)的区别
  // 1\. System.exit(0)：正常退出；
  // 2\. System.exit(1)：非正常退出，通常这种退出方式应该放在catch块中。 

```

*   特别注意

    假设场景：当前 `Activity` ≠ 当前任务栈最后1个`Activity`时，调用上述两个方法会出现什么情况呢？（即`Activity1` - `Activity2` -`Activity3`（在`Activity3`调用上述两个方法））

答：

1.  结束`Activity3`（当前 `Activity` ）& 结束进程

2.  再次重新开启进程  & 启动 `Activity1` 、 `Activity2`

![](https://upload-images.jianshu.io/upload_images/944365-8e8a97fde37f86a6.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/390)

即在`Android` 中，调用上述`Dalvik VM`本地方法结果是：

1.  结束当前 `Activity` & 结束进程

2.  之后再重新开启进程 & 启动 之前除当前 `Activity` 外的已启动的 `Activity`

*   原因：** `Android`中的`ActivityManager`时刻监听着进程**。一旦发现进程被非正常结束，它将会试图去重启这个进程。

*   应用场景

    当任务栈只剩下当前`Activity`（即退出了其余 `Activity`后），调用即可退出该进程，即**在（步骤1）结束当前 App 所有的 Activity 后，调用该方法即可一键退出App（更多体现在结束进程上）**

> 注： 与 “在最后一个`Activity`调用 `finish（）`”的区别：`finish（）`不会结束进程，而上述两个方法会

至此，关于 ** 一键退出`App`  ** 的两个步骤讲解完毕。

* * *

# 3\. Demo地址

关于上述说的方法`Demo`都在[Carson_Ho的Github地址：一键退出App](https://link.jianshu.com?t=https://github.com/Carson-Ho/App_1shot_Finsh)

* * *

# 4\. 总结

*   在 需要实现 一键退出 `App` 功能时，实际上是需要完成2个步骤：

    步骤1：一键结束当前`App`所有的`Activity`

    步骤2：一键结束当前`App`进程

*   每个步骤的方法总结如下

![](https://upload-images.jianshu.io/upload_images/944365-f99085f554b0bb97.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)
