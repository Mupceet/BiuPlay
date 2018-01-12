阅读以下文章，完成 Dagger 2 完全解析（伪）
* [Dagger 2 完全解析（一），Dagger 2 的基本使用与原理](http://johnnyshieh.me/posts/dagger-basic/)
* [Dagger 2 完全解析（二），进阶使用 Lazy、Qualifier、Scope 等](http://johnnyshieh.me/posts/dagger-advance/)
* [Dagger 2 完全解析（三），Component 的组织关系与 SubComponent](http://johnnyshieh.me/posts/dagger-subcomponent/)
* [Dagger 2 完全解析（四），Android 中使用 Dagger 2](http://johnnyshieh.me/posts/dagger-use-in-android/)
* [Dagger 2 完全解析（五），Kotlin 中使用 Dagger 2](http://johnnyshieh.me/posts/dagger-use-in-kotlin/)

通过以上文章中的简单的例子可以了解了 Dagger2 的基本使用、概念及基本原理，实际项目中的应用肯定是比例子复杂的。

关于 Dagger2 在实际项目中的应用，我找到一个开源项目 [MinimalistWeather](https://github.com/BaronZ88/MinimalistWeather)。这个库应该是安居客的一名工程师开源的 Demo 工程，以下是它的自我介绍：

> MinimalistWeather 是 Android 平台上一款开源天气 App ，目前还在开发中。项目基于 MVP 架构，采用各主流开源库实现。开发此项目主要是为展示各种开源库的使用方式以及 Android 项目的设计方案，并作为团队项目开发规范的一部分。
>
>采用的开源库包括：
>
>* RxJava
>* Retrofit2
>* OKHttp3
>* ORMLite
>* Dagger2
>* ButterKnife
>* RetroLambda
>* Stetho
>
>本项目还展示了：
>
>* MVP+RxJava在实际项目中的应用，MVP中RxJava生命周期的管理...；
>* 上述罗列的各种开源框架的使用方法；
>* Java8 Lambda表达式和Stream API的用法；
>* 怎样适配Material Design；
>* ToolBar、RecycleView、CardView、CoordinatorLayout等新控件的用法；
>* Gradle的基本配置（包括签名打包、项目依赖等等）；
>* 如何更好的管理Gradle依赖库的版本；
>* 代码混淆配置；
>* 如何快速开发一款结构清晰、可扩展性强的Android Application。
