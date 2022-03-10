# LEGO

### 概述    
LEGO：谐音 LET IT GO，同时将启动任务分成单独的个体，类似于积木一样组装成适用于不同启动阶段，不同进程的任务集合。 

### 需要解决的问题   
1. 任务的依赖关系 
2. 异步任务的通信，例如：任务A需要等待任务B执行完成后，才能执行，但任务A是在线程A中执行，任务B是在线程B中执行，怎么通信？
3. 异步任务与同步任务的通信，同上
4. 如果处理同步任务阻塞异步任务的情况
5. 多阶段？app启动过程中可以分成多个阶段，例如：attachBaseContext阶段、Application#onCreate阶段、MainActivity#onCreate阶段以及首刷完成
6. 多进程？
7. 最好是可以通过注解的方式，自动生成启动任务，添加到不同阶段，不同进程

### VERSION 1.0  
该版本主要任务：启动框架简易版
- [x] 搭建基本的启动框架
- [x] 解决任务依赖问题
- [x] 异步任务与异步任务、异步任务与同步任务通信问题

### VERSION 2.0
主要任务：Startup和Task解耦

### VERSION 3.0   
主要任务：apt实现Startup生成、分组、分阶段
- [x] 利用APT自动生成启动Task
定义任务接口，业务方只需要定义具体的Task，其他的操作交给框架实现
```Kotlin
interface Action<T> {

    fun performAction(): T?
}

class TestAction: Action<String?> {

    override fun performAction(): String? {
        return null
    }
}
```
- [ ] 注解定义任务执行的生命周期
- [x] 注解定义依赖关系
- [ ] 注解定义分组

### USAGE   
定义具体的任务逻辑
```kotlin
@Task(name = "FirstGenerateTask", typeVariable = TYPE_BOOLEAN)
class FirstAction: Action<Boolean> {

    override fun performAction(): Boolean {
        Log.e("mars", "FirstAction performAction start")
        Thread.sleep(1000)
        Log.e("mars", "FirstAction performAction end")
        return true
    }
}
```  
调用点
```kotlin
TaskManager.Builder()
    .addTask(FirstGenerateTask())
    .build()
    .start()
    .awaitMainThread()
```  
- @Task：标识启动执行的任务
- @DependOn：标识当前任务依赖的任务
- @WorkerThread：标识当前任务在哪个线程执行，如果是在主线程，则无需添加，默认主线程
- @BlockMainThread：标识当前任务是否阻塞主线程，如果是，则添加，默认不阻塞