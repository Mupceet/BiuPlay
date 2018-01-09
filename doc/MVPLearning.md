说到 MVP，官方架构一定是要看的了：

* [todo-mvp](https://github.com/googlesamples/android-architecture/tree/todo-mvp)
* [todo-mvp-rxjava](https://github.com/googlesamples/android-architecture/tree/todo-mvp-rxjava)
* [todo-mvp-dagger](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger)
* [todo-mvp-clean](https://github.com/googlesamples/android-architecture/tree/todo-mvp-clean)

另外还有一个不错的库：

* [JessYanCoding/MVPArms](https://github.com/JessYanCoding/MVPArms)

一篇整理：
* [Android MVP 详解（上）](http://www.jianshu.com/p/9a6845b26856)
* [Android MVP 详解（下）](http://www.jianshu.com/p/0590f530c617)

下篇是总结，只看这个也可以，原文格式有点问题，我重新整理了一下在这里：
* [Android MVP 详解（下）](./mvp.md)

其中推荐的方式：
* [通过Loader延长Presenter生命周期](http://blog.chengdazhi.com/index.php/131)
该文原文为：
* [Presenter surviving orientation changes with Loaders](https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf)

文章中提到了一个问题：
* [Should we really call getLoaderManager().initLoader in onActivityCreated, which causes onLoadFinished being called twice](https://stackoverflow.com/questions/15515799/should-we-really-call-getloadermanager-initloader-in-onactivitycreated-which)

另外需要更多地理解 Loader：
* [initLoader和restartLoader的区别](http://blog.csdn.net/shuxiangxingkong/article/details/43526315)
