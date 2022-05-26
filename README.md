# LEGO

### 一、概述    
LEGO：谐音 LET IT GO，同时将启动任务分成单独的个体，类似于积木一样组装成适用于不同启动阶段，不同进程的任务集合。  

在LEGO中，只需要定义具体的执行action，并使用注解声明，在apt过程中，会根据声明的注解进行排列组合，自动生成对应的XxxTask。

各注解的作用：
- `@Task`：标识启动阶段执行的任务
- `@DependOn`：标识当前任务依赖的任务
- `@WorkerThread`：标识当前任务在哪个线程执行，如果是在主线程，则无需添加，默认主线程
- `@BlockMainThread`：标识当前任务是否阻塞主线程，如果是，则添加，默认不阻塞

### 二、如何使用   
1、定义具体的执行逻辑action
```kotlin
// name：任务名称(自动生成)；typeVariable：该Action的返回值类型
@Task(name = "FirstGenerateTask", typeVariable = TYPE_BOOLEAN)
class FirstAction: Action<Boolean> {

    override fun performAction(): Boolean {
        Log.e("mars", "FirstAction performAction start")
        Thread.sleep(1000)
        Log.e("mars", "FirstAction performAction end")
        return true
    }
}

// 依赖ThirdAction、FirstAction任务的执行；在子线程执行
@Task(name = "SecondGenerateTask", typeVariable = TYPE_UNIT)
@DependOn(dependencies = 
[
    "com.mars.infra.lego.test.action2.ThirdAction", 
    "com.mars.infra.lego.test.action2.FirstAction"
    ])
@WorkerThread(false)
class SecondAction: Action<Unit> {

    override fun performAction() {
        Log.e("mars", "SecondAction performAction start")
        Thread.sleep(5000)
        Log.e("mars", "SecondAction performAction end")
    }
}

// 阻塞主线程，主线程需要等待该任务执行完成后，才能继续执行
@Task(name = "ThirdGenerateTask", typeVariable = TYPE_INT)
@BlockMainThread
class ThirdAction: Action<Int> {

    override fun performAction(): Int {
        Log.e("mars", "ThirdAction performAction start")
        Thread.sleep(2000)
        Log.e("mars", "ThirdAction performAction end")
        return -1
    }
}
```   
2、调用点逻辑
```kotlin
TaskManager.Builder()
    .addTask(SecondGenerateTask())
    .addTask(ThirdGenerateTask())
    .addTask(FirstGenerateTask())
    .addTasks(
        arrayListOf(FourGenerateTask(), FiveGenerateTask())
    )
    .build()
    .start()
    .awaitMainThread()
```
### version 1.0  
该版本主要任务：启动框架简易版
- [x] 搭建基本的启动框架
- [x] 解决任务依赖问题
- [x] 异步任务与异步任务、异步任务与同步任务通信问题

### version 2.0
主要任务：Startup和Task解耦

### version 3.0   
主要任务：apt实现Startup生成
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
- [x] 注解定义依赖关系

### TODO     
- [ ] 注解定义任务执行的生命周期
- [ ] 多进程