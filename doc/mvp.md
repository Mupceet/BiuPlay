# Android MVP 详解（下）

> * 作者：diygreen
> * 链接：https://www.jianshu.com/p/0590f530c617
> * 來源：简书
> * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

# 5\. 最佳实践

好了终于要点讲自己的东西了，有点小激动。下面这些仅表示个人观点，非一定之规，各位看官按需取用，有说的不对的，敬请谅解。关于命名规范可以参考我的另一篇文章“[Android 编码规范](https://www.jianshu.com/p/0a984f999592#)”。老规矩先上图：

![](https://upload-images.jianshu.io/upload_images/1233754-8bd547cf074d0860.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

在参考了 [kenjuwagatsuma](https://link.jianshu.com?t=https://medium.com/@kenjuwagatsuma) 的 [MVP Architecture in Android Development](https://link.jianshu.com?t=https://medium.com/@kenjuwagatsuma/mvp-architecture-in-android-development-3d63cc32707a#.9fyw4pjdg) 和 [Saúl Molinero](https://link.jianshu.com?t=http://saulmm.github.io/) 的 [A useful stack on android #1, architecture](https://link.jianshu.com?t=http://saulmm.github.io/2015/02/02/A-useful-stack-on-android-1,-architecture/) 之后，我决定采用如下的分层方案来构建这个演示Demo，如下：

![](https://upload-images.jianshu.io/upload_images/1233754-1d232d0114a5c19e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

总体架构可以被分成四个部分 ：

**Presentation**：负责展示图形界面，并填充数据，该层囊括了 View 和 Presenter （上图所示的Model我理解为 ViewModel -- 为 View 提供数据的 Model，或称之为 VO -- View Object）。

**Domain**：负责实现app的业务逻辑，该层中由普通的Java对象组成，一般包括 Usecases 和 Business Logic。

**Data**：负责提供数据，这里采用了 Repository 模式，Repository 是仓库管理员，Domain 需要什么东西只需告诉仓库管理员，由仓库管理员把东西拿给它，并不需要知道东西实际放在哪。Android 开发中常见的数据来源有，RestAPI、SQLite数据库、本地缓存等。

**Library**：负责提供各种工具和管理第三方库，现在的开发一般离不开第三方库（当然可以自己实现，但是不要重复造轮子不是吗？），这里建议在统一的地方管理（那就是建一个单独的 module），尽量保证和 Presentation 层分开。

![](https://upload-images.jianshu.io/upload_images/1233754-d744f35227a3cab9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/670)

## 5.1\. 关于包结构划分##

一个项目是否好扩展，灵活性是否够高，包结构的划分方式占了很大比重。很多项目里面喜欢采用按照特性分包（就是Activity、Service等都分别放到一个包下），在模块较少、页面不多的时候这没有任何问题；但是对于模块较多，团队合作开发的项目中，这样做会很不方便。所以，我的建议是按照模块划分包结构。其实这里主要是针对 Presentation 层了，这个演示 Demo 我打算分为四个模块：登录，首页，查询天气和我的（这里仅仅是为了演示需要，具体如何划分模块还得根据具体的项目，具体情况具体分析了）。划分好包之后如下图所示：

![](https://upload-images.jianshu.io/upload_images/1233754-a7259b477e304440.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

## 5.2\. 关于res拆分

功能越来越多，项目越做越大，导致资源文件越来越多，虽然通过命名可以对其有效归类（如：通过添加模块名前缀），但文件多了终究不方便。得益于 Gradle，我们也可以对 res 目录进行拆分，先来看看拆分后的效果：

![](https://upload-images.jianshu.io/upload_images/1233754-bed38e679fd920f3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

**注意**：resource 目录的命名纯粹是个人的命名偏好，该目录的作用是用来存放那些不需要分模块放置的资源。

res 目录的拆分步骤如下：

1.  首先打开 module 的 build.gradle 文件

   ![](https://upload-images.jianshu.io/upload_images/1233754-3456f3b1b4ce69f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/306)

2.  定位到 defaultConfig {} 与 buildTypes {} 之间

   ![](https://upload-images.jianshu.io/upload_images/1233754-226ed3830ecf60b1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

3.  在第二步定位处编辑输入 sourceSets {} 内容，具体内容如下：

```
sourceSets {
    main {
        manifest.srcFile 'src/main/AndroidManifest.xml'
        java.srcDirs = ['src/main/java','.apt_generated']
        aidl.srcDirs = ['src/main/aidl','.apt_generated']
        assets.srcDirs = ['src/main/assets']
        res.srcDirs =
        [
                'src/main/res/home',
                'src/main/res/login',
                'src/main/res/mine',
                'src/main/res/weather',
                'src/main/res/resource',
                'src/main/res/'

        ]
    }
}
```

4) 在 res 目录下按照 sourceSets 中的配置建立相应的文件夹，将原来 res 下的所有文件（夹）都移动到 resource 目录下，并在各模块中建立 layout 等文件夹，并移入相应资源，最后 Sync Project 即可。

## 5.3. 怎么写 Model
这里的 Model 其实贯穿了我们项目中的三个层，Presentation、Domain 和 Data。暂且称之为 Model 吧，这也我将提供 Repository 功能的层称之为 Data Layer 的缘故（有些称这一层为 Model Layer）。

**首先**，谈谈我对于 Model 是怎么理解的。应用都离不开数据，而这些数据来源有很多，如网络、SQLite、文件等等。一个应用对于数据的操作无非就是：获取数据、编辑（修改）数据、提交数据、展示数据这么几类。从分层的思想和 JavaEE 开发中积累的经验来看，我觉得 Model 中的类需要分类。从功能上来划分，可以分出这么几类：
**VO（View Object）**：视图对象，用于展示层，它的作用是把某个指定页面（或组件）的所有数据封装起来。
**DTO（Data Transfer Object）**：数据传输对象，这个概念来源于 JavaEE 的设计模式，原来的目的是为了 EJB 的分布式应用提供粗粒度的数据实体，以减少分布式调用的次数，从而提高分布式调用的性能和降低网络负载，但在这里，我泛指用于展示层与服务层之间的数据传输对象。
**DO（Domain Object）**：领域对象，就是从现实世界中抽象出来的有形或无形的业务实体。
**PO（Persistent Object）**：持久化对象，它跟持久层（通常是关系型数据库）的数据结构形成一一对应的映射关系，如果持久层是关系型数据库，那么，数据表中的每个字段（或若干个）就对应 PO 的一个（或若干个）属性。

**注意**：关于vo、dto、do、po可以参考这篇文章-“[领域驱动设计系列文章——浅析VO、DTO、DO、PO的概念、区别和用处](http://www.cnblogs.com/qixuejia/p/4390086.html)”

当然这些不一定都存在，这里只是列举一下，可以有这么多分类，当然列举的也不全。

**其次**，要搞清楚 Domain 层和 Data 层分别是用来做什么的，然后才知道哪些 Model 该往 Data 层中写，哪些该往 Domain 层中写。
Data 层负责提供数据。
Data 层不会知道任何关于 Domain 和 Presentation 的数据。它可以用来实现和数据源（数据库，REST API或者其他源）的连接或者接口。这个层面同时也实现了整个app所需要的实体类。
Domain 层相对于 Presentation 层完全独立，它会实现应用的业务逻辑，并提供 Usecases。
Presentation 从 Domain 层获取到的数据，我的理解就是 VO 了，VO 应该可以直接使用。

>注意：这里说的直接使用是指不需要经过各种转换，各种判断了，如 Activity 中某个控件的显示隐藏是根据 VO 中的 visibility 字段来决定，那么这个最好将 visibility 作为 int 型，而且，取值为VISIBLE/INVISIBLE/GONE，或者至少是 boolean 型的。

**注意**：这里所谓的业务逻辑可能会于 Presenter 的功能概念上有点混淆。打个比方，假如 usecase 接收到的是一个 json 串，里面包含电影的列表，那么把这个 json 串转换成 json 以及包装成一个 ArrayList，这个应当是由 usecase 来完成。而假如 ArrayList 的 size 为0，即列表为空，需要显示缺省图，这个判断和控制应当是由 Presenter 完成的。（上述观点参考自：[Saúl Molinero](http://saulmm.github.io/)）

**最后**，就是关于 Data 层，采用的 Repository 模式，建议抽象出接口来，Domain 层需要感知数据是从哪里取出来的。

## 5.4. 怎么写 View
先区分一下Android View、View、界面的区别
**Android View**： 指的是继承自android.view.View的Android组件。
**View**：接口和实现类，接口部分用于由 Presenter 向 View 实现类通信，可以在 Android 组件中实现它。一般最好直接使用 Activity，Fragment 或自定义 View。
**界面**：界面是面向用户的概念。比如要在手机上进行界面间切换时，我们在代码中可以通过多种方式实现，如 Activity 到 Activity 或一个 Activity 内部的 Fragment/View 进行切换。所以这个概念基于用户的视觉，包括了所有 View 中能看到的东西。

那么该怎么写 View 呢？

在 MVP 中 View 是很薄的一层，里面不应该有业务逻辑，所以一般只提供一些 getter 和 setter 方法，供 Presenter 操作。关于 View，我有如下建议：
1. 简单的页面中直接使用 Activity/Fragment 作为 View 的实现类，然后抽取相应的接口
2. 在一些有 Tab 的页面中，可以使用 Activity + Fragment ( + ViewPager) 的方式来实现，至于 ViewPager，视具体情况而定，当然也可以直接 Activity + ViewPager 或者其他的组合方式
3. 在一些包含很多控件的复杂页面中，那么建议将界面拆分，抽取自定义 View，也就是一个 Activity/Fragment 包含多个 View（实现多个 View 接口）

## 5.5. 怎么写 Presenter
Presenter 是 Android MVP 实现中争论的焦点，上篇中介绍了多种“MVP 框架”，其实都是围绕着**Presenter应该怎么写**。有一篇专门介绍如何设计 Presenter 的文章（[Modeling my presentation layer](http://panavtec.me/modeling-presentation-layer)），个人感觉写得不错，这里借鉴了里面不少的观点，感兴趣的童鞋可以去看看。下面进入正题。
为什么写 Presenter 会这么纠结，我认为主要有以下几个问题：
1. 我们将 Activity/Fragment 视为 View，那么 View 层的编写是简单了，但是这有一个问题，当手机的状态发生改变时（比如旋转手机）我们应该如何处理Presenter对象，那也就是说 Presenter 也存在生命周期，并且还要“手动维护”（别急，这是引起来的，下面会细说）
2. Presenter 中应该没有 Android Framework 的代码，也就是不需要导 Framework 中的包，那么问题来了，页面跳转，显示对话框这些情况在 Presenter 中该如何完成
3. 上面说 View 的时候提到复杂的页面建议通过抽取自定义 View 的方式，将页面拆分，那么这个时候要怎么建立对应的 Presenter 呢
4. View 接口是可以有多个实现的，那我们的 Presenter 该怎么写呢

好，现在我将针对上面这些问题一一给出建议。
### 5.5.1. 关于 Presenter 生命周期的问题
先看图（更详细讲解可以看看这篇文章[Presenter surviving orientation changes with Loaders](https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf)）
![Presenter生命周期](http://upload-images.jianshu.io/upload_images/1233754-a9a829de0250462f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
如上图所示，方案1和方案2都不够优雅（这也是很多“MVP 框架”采用的实现方案），而且并不完善，只适用于一些场景。而方案3，让人耳目一新，看了之后不禁想说 Loader 就是为 Presenter 准备的啊。这里我们抓住几个关键点就好了：
* Loader 是 **Android 框架**中提供的
* Loader 在手机状态改变时是**不会被销毁**的
* Loader 的生命周期是是由**系统控制**的，会在Activity/Fragment不再被使用后**由系统回收**
* Loader 与 Activity/Fragment 的生命周期绑定，所以**事件会自己分发**
* 每一个 Activity/Fragment 持有**自己的 Loader 对象**的引用
* 具体怎么用，在 [Antonio Gutierrez](https://medium.com/@czyrux) 的文章已经阐述的很明白，我就不再赘述了

> 好吧，我有一点要补充，上面说的方案1和方案2不是说就没有用了，还是视具体情况而定，如果没有那么多复杂的场景，那么用更简单的方案也未尝不可。能解决问题就好，不要拘泥于这些条条框框...（话说，咱这不是为了追求完美吗，哈哈）

### 5.5.2. 关于页面跳转和显示Dialog
首先说说页面跳转，前一阵子忙着重构公司的项目，发现项目中很多地方使用 startActivity() 和使用 Intent 的 putExtra() 显得很乱；更重要的是从 Intent 中取数据的时候需要格外小心——类型要对应，key 要写对，不然轻则取不到数据，重则 Crash。还有一点，就是当前 Activity/Fragment 必须要知道目标 Activity 的类名，这里耦合的很严重，有没有。当时就在想这是不是应该封装一下啊，或者有更好的解决方案。于是，先在网上搜了一下，知乎上有类似的提问，有人建议写一个 Activity Router（Activity 路由表）。嗯，正好和我的思路类似，那就开干。

我的思路很简单，在 util 包中定义一个 NavigationManager 类，在该类中按照模块使用注释先分好区块（为什么要分区块，去看看我的 “[Android 编码规范](http://www.jianshu.com/p/0a984f999592#)”）。然后为每个模块中的 Activity 该如何跳转，定义一个静态方法。

如果不需要传递数据的，那就很简单了，只要传入调用者的 Context，直接 new 出 Intent，调用该 Context 的 startActivity() 方法即可。代码如下：
![导航管理类-跳转系统页面](http://upload-images.jianshu.io/upload_images/1233754-1729b46c1b2709d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![导航管理类-跳转不需要传递数据的页面](http://upload-images.jianshu.io/upload_images/1233754-e958031db7c46841.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果需要传递数据呢？刚才说了，使用 Bundle 或者 putExtra() 这种方式很不优雅，而且容易出错（那好，你个给优雅的来看看，哈哈）。确实，我没想到比较优雅的方案，在这里我提供一个粗糙的方案，仅供大家参考一下，如有你有更好的，那麻烦也和我分享下。

我的方案是这样的，使用序列化对象来传递数据（建议使用 Parcelable，不要偷懒去用 Serializable，这个你懂的）。为需要传递数据的 Activity 新建一个实现了 Parcelable 接口的类，将要传递的字段都定义在该类中。其他页面需要跳转到该 Activity，那么就需要提供这个对象。在目标 Activity 中获取到该对象后，那就方便了，不需要去找对应的 key 来取数据了，反正只要对象中有的，你就能直接使用。

> 注意：这里我建议将序列化对象中的所有成员变量都定义为 public 的，一来，可以减少代码量，主要是为了减少方法数（虽说现在对于方法数超 64K 有比较成熟的 dex 分包方案，但是尽量不超不是更好）；二来，通过对象的 public 属性直接读写比使用 getter/setter 速度要快（听说的，没有验证过）。

> 注意：这里建议在全局常量类（没有，那就定义一个，下面会介绍）中定义一个唯一的 INTENT_EXTRA_KEY，往 Bundle 中存和取得时候都用它，也不用去为命名 key 费神（命名从来不简单，不是吗），取的时候也不用思考是用什么 key 存的，简单又可以避免犯错。

具体如下图所示：
![导航管理类-跳转需要传递数据的页面](http://upload-images.jianshu.io/upload_images/1233754-8e7e60b8e75c0696.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![导航管理类-传递数据](http://upload-images.jianshu.io/upload_images/1233754-cf4de86178b55378.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![导航管理类-获取传递的数据](http://upload-images.jianshu.io/upload_images/1233754-6cae34270d70ff30.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

导航管理类代码如下：
```java
//==========逻辑方法==========
    public static <T> T getParcelableExtra(Activity activity) {
        Parcelable parcelable = activity.getIntent().getParcelableExtra(NavigateManager.PARCELABLE_EXTRA_KEY);
        activity = null;
        return (T)parcelable;
    }

    private static void overlay(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        putParcelableExtra(intent, parcelable);
        context.startActivity(intent);
        context = null;
    }

    private static void overlay(Context context, Class<? extends Activity> targetClazz, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        putParcelableExtra(intent, parcelable);
        context.startActivity(intent);
        context = null;
    }

    private static void overlay(Context context, Class<? extends Activity> targetClazz, Serializable serializable) {
        Intent intent = new Intent(context, targetClazz);
        putSerializableExtra(intent, serializable);
        context.startActivity(intent);
        context = null;
    }

    private static void overlay(Context context, Class<? extends Activity> targetClazz) {
        Intent intent = new Intent(context, targetClazz);
        context.startActivity(intent);
        context = null;
    }

    private static void forward(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        intent.putExtra(PARCELABLE_EXTRA_KEY, parcelable);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity)context).finish();
        context = null;
    }

    private static void forward(Context context, Class<? extends Activity> targetClazz, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        putParcelableExtra(intent, parcelable);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity)context).finish();
        context = null;
    }

    private static void forward(Context context, Class<? extends Activity> targetClazz, Serializable serializable) {
        Intent intent = new Intent(context, targetClazz);
        putSerializableExtra(intent, serializable);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity)context).finish();
        context = null;
    }

    private static void forward(Context context, Class<? extends Activity> targetClazz) {
        Intent intent = new Intent(context, targetClazz);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity)context).finish();
        context = null;
    }

    private static void startForResult(Context context, Class<? extends Activity> targetClazz, int flags) {
        Intent intent = new Intent(context, targetClazz);
        if (isActivity(context)) return;
        ((Activity)context).startActivityForResult(intent, flags);
        context = null;
    }

    private static void startForResult(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        if (isActivity(context)) return;
        putParcelableExtra(intent, parcelable);
        ((Activity)context).startActivityForResult(intent, flags);
        context = null;
    }

    private static void setResult(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        putParcelableExtra(intent, parcelable);
        if (isActivity(context)) return;
        ((Activity)context).setResult(flags, intent);
        ((Activity)context).finish();
    }

    private static boolean isActivity(Context context) {
        if (!(context instanceof Activity)) return true;
        return false;
    }

    private static void setFlags(Intent intent, int flags) {
        if (flags < 0) return;
        intent.setFlags(flags);
    }

    private static void putParcelableExtra(Intent intent, Parcelable parcelable) {
        if (parcelable == null) return;
        intent.putExtra(PARCELABLE_EXTRA_KEY, parcelable);
    }

    private static void putSerializableExtra(Intent intent, Serializable serializable) {
        if (serializable == null) return;
        intent.putExtra(PARCELABLE_EXTRA_KEY, serializable);
    }
```

传递数据用的序列化对象，如下：

```
public class DishesStockVO implements Parcelable {

    public boolean isShowMask;
    public int pageNum;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isShowMask ? (byte) 1 : (byte) 0);
        dest.writeInt(this.pageNum);
    }

    public DishesStockVO() {
    }

    protected DishesStockVO(Parcel in) {
        this.isShowMask = in.readByte() != 0;
        this.pageNum = in.readInt();
    }

    public static final Creator<DishesStockVO> CREATOR = new Creator<DishesStockVO>() {
        public DishesStockVO createFromParcel(Parcel source) {
            return new DishesStockVO(source);
        }

        public DishesStockVO[] newArray(int size) {
            return new DishesStockVO[size];
        }
    };

    @Override
    public String toString() {
        return "DishesStockVO{" +
                "isShowMask=" + isShowMask +
                ", pageNum=" + pageNum +
                '}';
    }
}

```

好像，还没入正题。这里再多说一句，beautifulSoup 写了一篇文章，说的就是 Android 路由表框架的，可以去看看——“[Android路由框架设计与实现](https://link.jianshu.com?t=http://sixwolf.net/blog/2016/03/23/Android%E8%B7%AF%E7%94%B1%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1/)”。

好了，回到主题，在 Presenter 中该如何处理页面跳转的问题。在这里我建议简单处理，在 View Interface 中定义好接口（方法），在 View 的实现类中去处理（本来就是它的责任，不是吗？）。在 View 的实现类中，使用 NavigationManager 工具类跳转，达到解耦的目的。如下图所示：

![](https://upload-images.jianshu.io/upload_images/1233754-4b4d20c19806c2ef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/625)

显示对话框

我在这里采用和页面跳转的处理类似的方案，这也是 View 的责任，所以让 View 自己去完成。这里建议每个模块都定义一个相应的 XxxDialogManager 类，来管理该模块所有的弹窗，当然对于弹窗本来就不多的，那就直接在 util 包中定义一个 DialogManager 类就好了。如下图：

![](https://upload-images.jianshu.io/upload_images/1233754-c7c9c01904953f63.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/625)

### 5.5.3\. 一个页面多个View的问题

对于复杂页面，一般建议拆成多个自定义 View，那么这就引出一个问题，这时候是用一个 Presenter 好，还是定义多个 Presenter 好呢？我的建议是，每个 View Interface 对应一个 Presenter，如下图所示：

![](https://upload-images.jianshu.io/upload_images/1233754-ad86e58ffd1491ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/646)

### 5.5.4\. 一个View有两个实现类的问题

有些时候会遇到这样的问题，只是展示上有差别，两个页面上所有的操作都是一样的，这就意味着 View Interface 是一样的，只是有两个实现类。

这个问题该怎么处理，或许可以继续使用同样的Presenter并在另一个Android组件中实现View接口。不过这个界面似乎有更多的功能，那要不要把这些新功能加进这个Presenter呢？这个视情况而定，有多种方案：一是将Presenter整合负责不同操作，二是写两个Presenter分别负责操作和展示，三是写一个Presenter包含所有操作（在两个View相似时）。记住没有完美的解决方案，编程的过程就是让步的过程。（参考自：[Christian Panadero PaNaVTEC](https://link.jianshu.com?t=https://github.com/PaNaVTEC) 的 [Modeling my presentation layer](https://link.jianshu.com?t=http://panavtec.me/modeling-presentation-layer)）

如下图所示：

![](https://upload-images.jianshu.io/upload_images/1233754-f9733464ef609ad7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

## 5.6\. 关于 RestAPI

一般项目当中会用到很多和服务器端通信用的接口，这里建议在每个模块中都建立一个 api 包，在该包下来统一处理该模块下所有的 RestAPI。

如下图所示：

![](https://upload-images.jianshu.io/upload_images/1233754-29f62cd38726a5aa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)

对于网络请求之类需要异步处理的情况，一般都需要传入一个回调接口，来获取异步处理的结果。对于这种情况，我建议参考 onClick(View v) {} 的写法。那就是为每一个请求编一个号（使用 int 值），我称之为 taskId，可以将该编号定义在各个模块的常量类中。然后在回调接口的实现类中，可以在回调方法中根据 taskId 来统一处理（一般是在这里分发下去，分别调用不同的方法）。

如下图所示：

![](https://upload-images.jianshu.io/upload_images/1233754-5f8af53e854065e6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/604)

![](https://upload-images.jianshu.io/upload_images/1233754-3a726be101359b93.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/625)

## 5.6\. 关于项目中的常量管理

Android 中不推荐使用枚举，推荐使用常量，我想说说项目当中我一般是怎么管理常量的。

灵感来自 R.java 类，这是由项目构建工具自动生成并维护的，可以进去看看，里面是一堆的静态内部类，如下图：

![](https://upload-images.jianshu.io/upload_images/1233754-0c1f241d7b1c69ea.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

看到这，可能大家都猜到了，那就是定义一个类来管理全局的常量数据，我一般喜欢命名为 C.java。这里有一点要注意，我们的项目是按模块划分的包，所以会有一些是该模块单独使用的常量，那么这些最好不要写到全局常量类中，否则会导致 C 类膨胀，不利于管理，最好是将这些常量定义到各个模块下面。如下图所示：

![](https://upload-images.jianshu.io/upload_images/1233754-9c04ce9fc34f257d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

## 5.7\. 关于第三方库

Android 开发中不可避免要导入很多第三方库，这里我想谈谈我对第三方库的一些看法。关于第三方库的推荐我就不做介绍了，很多专门说这方面的文章。

### 5.7.1\. 挑选第三方库的一些建议

1.  项目中确实需要（这不是废话吗？用不着，我要它干嘛？呵呵，建议不要为了解决一个小小的问题导入一个大而全的库）

2.  使用的人要多（大家都在用的一般更新会比较快，出现问题解决方案也多）

3.  效率和体量的权衡（如果效率没有太大影响的情况下，我一般建议选择体量小点的，如，Gson vs Jackson，Gson 胜出；还是 65K 的问题）

### 5.7.2\. 使用第三方库尽量二次封装

为什么要二次封装？

为了方便更换，说得稍微专业点为了降低耦合。

有很多原因可能需要你替换项目中的第三方库，这时候如果你是经过二次封装的，那么很简单，只需要在封装类中修改一下就可以了，完全不需要去全局检索代码。

我就遇到过几个替换第三方库的事情：

1.  替换项目中的统计埋点工具

2.  替换网络框架

3.  替换日志工具

那该怎么封装呢？

一般的，如果是一些第三方的工具类，都会提供一些静态方法，那么这个就简单了，直接写一个工具类，提供类似的静态方法即可（就是用静态工厂模式）。

如下代码所示，这是对系统 Log 的简单封装：

```
/**
 * Description: 企业中通用的Log管理
 * 开发阶段LOGLEVEL = 6
 * 发布阶段LOGLEVEL = -1
 */

public class Logger {

    private static int LOGLEVEL = 6;
    private static int VERBOSE = 1;
    private static int DEBUG = 2;
    private static int INFO = 3;
    private static int WARN = 4;
    private static int ERROR = 5;

    public static void setDevelopMode(boolean flag) {
        if(flag) {
            LOGLEVEL = 6;
        } else {
            LOGLEVEL = -1;
        }
    }

    public static void v(String tag, String msg) {
        if(LOGLEVEL > VERBOSE && !TextUtils.isEmpty(msg)) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if(LOGLEVEL > DEBUG && !TextUtils.isEmpty(msg)) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if(LOGLEVEL > WARN && !TextUtils.isEmpty(msg)) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if(LOGLEVEL > ERROR && !TextUtils.isEmpty(msg)) {
            Log.e(tag, msg);
        }
    }

}

```

现在如果想替换为 [orhanobut](https://link.jianshu.com?t=https://github.com/orhanobut) 的 [Logger](https://link.jianshu.com?t=https://github.com/orhanobut/logger)，那很简单，代码如下：

```
/**
 * Description: 通用的Log管理工具类
 * 开发阶段LOGLEVEL = 6
 * 发布阶段LOGLEVEL = -1
 */

public class Logger {

    public static String mTag = "MVPBestPractice";
    private static int LOGLEVEL = 6;
    private static int VERBOSE = 1;
    private static int DEBUG = 2;
    private static int INFO = 3;
    private static int WARN = 4;
    private static int ERROR = 5;

    static {
        com.orhanobut.logger.Logger
                .init(mTag)                     // default PRETTYLOGGER or use just init()
                .setMethodCount(3)              // default 2
                .hideThreadInfo()               // default shown
                .setLogLevel(LogLevel.FULL);    // default LogLevel.FULL
    }

    public static void setDevelopMode(boolean flag) {
        if(flag) {
            LOGLEVEL = 6;
            com.orhanobut.logger.Logger.init().setLogLevel(LogLevel.FULL);
        } else {
            LOGLEVEL = -1;
            com.orhanobut.logger.Logger.init().setLogLevel(LogLevel.NONE);
        }
    }

    public static void v(@NonNull String tag, String msg) {
        if(LOGLEVEL > VERBOSE && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.v(tag, msg);
            com.orhanobut.logger.Logger.t(tag).v(msg);
        }
    }

    public static void d(@NonNull String tag, String msg) {
        if(LOGLEVEL > DEBUG && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.d(tag, msg);
            com.orhanobut.logger.Logger.t(tag).d(msg);
        }
    }

    public static void i(@NonNull String tag, String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).i(msg);
        }
    }

    public static void w(@NonNull String tag, String msg) {
        if(LOGLEVEL > WARN && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.w(tag, msg);
            com.orhanobut.logger.Logger.t(tag).w(msg);
        }
    }

    public static void e(@NonNull String tag, String msg) {
        if(LOGLEVEL > ERROR && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.e(tag, msg);
            com.orhanobut.logger.Logger.t(tag).e(msg);
        }
    }

    public static void e(@NonNull String tag, Exception e) {
        tag = checkTag(tag);
        if(LOGLEVEL > ERROR) {
//          Log.e(tag, e==null ? "未知错误" : e.getMessage());
            com.orhanobut.logger.Logger.t(tag).e(e == null ? "未知错误" : e.getMessage());
        }
    }

    public static void v(String msg) {
        if(LOGLEVEL > VERBOSE && !TextUtils.isEmpty(msg)) {
//          Log.v(mTag, msg);
            com.orhanobut.logger.Logger.v(msg);
        }
    }

    public static void d(String msg) {
        if(LOGLEVEL > DEBUG && !TextUtils.isEmpty(msg)) {
//          Log.d(mTag, msg);
            com.orhanobut.logger.Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//          Log.i(mTag, msg);
            com.orhanobut.logger.Logger.i(msg);
        }
    }

    public static void w(String msg) {
        if(LOGLEVEL > WARN && !TextUtils.isEmpty(msg)) {
//          Log.w(mTag, msg);
            com.orhanobut.logger.Logger.v(msg);
        }
    }

    public static void e(String msg) {
        if(LOGLEVEL > ERROR && !TextUtils.isEmpty(msg)) {
//          Log.e(mTag, msg);
            com.orhanobut.logger.Logger.e(msg);
        }
    }

    public static void e(Exception e) {
        if(LOGLEVEL > ERROR) {
//          Log.e(mTag, e==null ? "未知错误" : e.getMessage());
            com.orhanobut.logger.Logger.e(e == null ? "未知错误" : e.getMessage());
        }
    }

    public static void wtf(@NonNull String tag, String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).wtf(msg);
        }
    }

    public static void json(@NonNull String tag, String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).json(msg);
        }
    }

    public static void xml(@NonNull String tag, String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//          Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).xml(msg);
        }
    }

    public static void wtf(String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//          Log.i(tag, msg);
            com.orhanobut.logger.Logger.wtf(msg);
        }
    }

    public static void json(String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//          Log.i(tag, msg);
            com.orhanobut.logger.Logger.json(msg);
        }
    }

    public static void xml(String msg) {
        if(LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//          Log.i(tag, msg);
            com.orhanobut.logger.Logger.xml(msg);
        }
    }

    private static String checkTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = mTag;
        }
        return tag;
    }

```

这里是最简单的一些替换，如果是替换网络框架，图片加载框架之类的，可能要多费点心思去封装一下，这里可以参考“门面模式”。（在这里就不展开来讲如何对第三库进行二次封装了，以后有时间专门写个帖子）

### 5.7.3\. 建立单独的 Module 管理所有的第三库

原因前面已经说过了，而且操作也很简单。网上有不少拆分 Gradle 文件的方法，讲的都很不错。那我们就先从最简单的做起，赶快行动起来，把项目中用到的第三方库都集中到 Library Module 中来吧。
