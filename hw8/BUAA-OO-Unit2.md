# BUAA-OO-Unit2总结

本单元三次作业的主题是使用**多线程**来模拟电梯的运作过程，其中的难点包括了：**电梯运行逻辑的设计、调度算法的设计、线程之间的交互问题**。

## HW5

### 功能要求

第一次作业需要我们实现电梯最基础的运行功能，在本次作业中，乘客已经被分配到了相应的电梯中，我们只需要保证电梯能够正确地将乘客运送到正确的地方即可。

### 架构设计

#### UML类图

![hw5](.\hw5.drawio.svg)

#### UML协作图

![hw5_time](.\hw5_time.svg)

第一次作业中，我主要采用了生产者-消费者架构，**`RequestHandler`是生产者**，将请求放入`RequestTable`，**`Elevator`是消费者**，从各自的`RequestTable`中获取乘客。`RequestTable`在其中作为中介，每一个电梯都有对应的`RequestTable`，由于第一次作业的时候指定了乘客乘坐的电梯，所以不需要调度器的设计，直接将乘客分配给对应电梯就行。

除此之外，我还采用了单例模式，设置了`Strategy`和`Output`这样的类，因为这些类使用的时候只需要初始化一个对象，所以将他们设计成静态类，需要使用的时候可以直接调用。

在第一次作业中，我主要包括了两类线程，主线程（`RequestHandler`）和`Elevator`线程，两类线程刚好充当了生产者和消费者的功能。

### 同步块的设置和锁的选择

在第一次作业中，只有**`RequestTable`**中的内容需要被输入线程和电梯线程同时访问，为了确保线程安全，该类中的几乎所有方法都被加上了锁。

在锁的选择上，我使用了`synchronized`关键字来修饰方法，并在接下来的作业中，都使用了这种方法（~~懒得改成更好用的读写锁了~~）。

### 电梯运行算法

我采用了**LOOK**算法，好像大部分同学都是这样做的，这也是我们日常电梯使用的主要方法，具体如下：

* 首先判断**是否需要开门**，也就是是否有人需要出去，以及是否有人能够进来，需要则执行**开关门**操作。
* 然后判断**电梯是否为空**，如果不为空，则**按照原来方向继续运动**。
* 接着判断**请求队列是否为空**：
  * 如果为空，判断**输入线程有没有结束**：
    * 若结束，电梯**结束**。
    * 若没有结束，电梯**原地等待**。
  * 如果不为空，**查看电梯运行方向上是否还有乘客**：
    * 若有，则**按照原来方向继续运动**。
    * 若没有，这电梯**掉头**。

## HW6

### 新增功能

* 电梯能够重置容量和速度。
* 不再指定乘客乘坐的电梯，需要自己设计调度算法。
* 新增RECEIVE要求。

### 架构设计

#### UML类图

![hw6](.\hw6.drawio.svg)

#### UML协作图

![hw6_time](.\hw6_time.svg)

在第二次作业中，我继续延续了上一次的生产者-消费者模式。为了满足电梯RESET的过程中，不能RECEIVE的要求，我**新建了`RequestBuffer`类**，分配好的乘客会首先进入对应电梯的缓冲池中，当电梯没有在RESET的时候，缓冲池会将乘客加入电梯的请求队列中，请求队列会调用`Output`打印RECEIVE信息。

除此之外，因为本次作业需要自己设计调度策略，我又**新建了`Schedule`类**，该类负责从输入线程接受乘客，调用调度方法，将乘客分配给对应电梯的缓冲池中。

针对本次作业中的RESET要求，我在原先的LOOK算法中为电梯加入了**RESET动作**，当`Strategy`为电梯返回RESET指令后，电梯会将请求队列和电梯中的所有乘客**重新发送给调度器**，重新分配，然后进行重置。

本次作业实际上有三类线程，除了上次的主线程（`RequestHandler`）和`Elevator`线程，又增加了`Schedule`线程和`RequestBuffer`线程作为分别作为调度器和缓冲池。整体架构上有点类似于一个多级的生产者-消费者模型。

### 调度策略：影子电梯

调度策略我选择了影子电梯，该方法的核心思想是，模拟将一个乘客分别加入6个电梯的不同情况，计算哪个电梯能够最先把所有的乘客运送完毕，选取最先完成的电梯。

为了实现上述方法，我新建了一个**`ShadowElevator`类**，这个类只保留了原先电梯类中和模拟运行相关的属性和方法，这算是对深拷贝原先电梯的一种优化，每当要进行调度时，调度器都会为每个电梯创建相应的影子电梯类，然后模拟运行，计算时间消耗。

如果模拟的时候电梯处于**重置**的状态，简便考虑，我会加上600的时间消耗（0-1200ms的期望），并且在构造影子电梯的时候，不考虑请求队列和电梯里面的乘客，但是缓冲池里面的仍然考虑，因为电梯重置的时候会将它们清空。

这个调度方法将电梯实际的运行过程模拟了出来，乘客的楼层，目的地，电梯中的乘客以及电梯的属性等因素都被考虑了进来，因此这个方法还是比较全面的，但是影子电梯没有直接地去考虑耗电量的问题，而是以运行时间作为评价指标，不过一般来说，运行时间越短，耗电量也会倾向于少一些。但是，影子电梯在具体实现的过程中比较复杂，要考虑很多问题，比较容易出现bug，例如一次性将大量乘客分给同一个电梯，最终导致RTLE，同时复杂的实现逻辑也导致了代码的臃肿，对代码结构也造成了一定的破坏。

### 同步块和锁的设置

由于本次作业涉及到了电梯的重置和影子电梯这样的调度算法，所以各个线程之间存在大量的信息共享。在**增加乘客请求**的时候，请求按照`InputHandler -> Schedule -> RequestBuffer -> RequestTable`这样的方向流动。在**电梯RESET**的过程中，电梯会将其中的乘客返回给`Schedule`，电梯的容量和速度属性也会改变。在**进行调度**的时候，构造影子电梯需要电梯的相关属性。综上，上述类中的相应方法必须加上锁。

## HW7

### 新增功能

* 新增双轿厢电梯，单轿厢电梯可以被重置为双轿厢电梯。

### 架构设计

#### UML类图

![hw7](.\hw7.drawio.svg)

#### UML协作图

![hw7_time](.\hw7_time.svg)

本次作业中，为了能够同时管理单双轿厢电梯，我设计了一个接口`ElevatorShaft`，分别让`NormalElevator`和`DoubleCarElevator`来实现这一个接口，普通电梯中有一个`Elevator`对象，而双轿厢电梯中有两个`Elevator`对象，除此之外，还有相应的请求列表和缓冲池。在单轿厢重置为双轿厢的时候，在调度器的调控之下，单轿厢电梯会创建一个双轿厢电梯，同时将自己的电梯线程和缓冲池线程停止。

同时本次作业新增了`Count`类，该类用来统计还有多少一个乘客请求没有完成。该类同样为静态类，输入线程每处理一个乘客，计数器加一，电梯每将一个乘客送到目的地，计数器减一，该类的设置是为了调度器判断什么时候线程结束。

本次作业的线程设置几乎没有什么变化，主要是为了能够同时管理单双轿厢电梯而进行的架构调整，本质上还是主线程（`RequestHandler`）、`Elevator`线程、`Schedule`线程和`RequestBuffer`线程。

### 影子电梯的调整

双轿厢电梯在运送乘客的时候存在乘客换乘的问题，在用影子电梯模拟的时候，换乘过程的模拟比较复杂。因此，我在模拟的时候，允许双轿厢中的电梯跨过换乘楼层，直接把乘客送到目标楼层。这种做法大幅度化简了双轿厢电梯的模拟，而且又不会产生较大的误差，还能让调度器更加倾向于选择双轿厢电梯。

### 双轿厢避碰

在本次作业中，我们需要避免双轿厢电梯中，两个轿厢在换乘楼层碰撞。首先，我**在`DoubleCarElevator`中设置了一个锁**，只有拿到锁的轿厢才能进入换乘楼层，这样就可以避免两个轿厢同时进入换乘楼层。除此之外，我不允许轿厢在换乘楼层等待，如果需要等待，则必须移动一层再等待，这样就能很方便地避免一个轿厢长期占用锁，导致另一个轿厢无法进入换乘楼层。

## Bug分析

### 死锁

在使用评测机测试的时候，程序有可能会因为一些线程安全问题导致死锁。这类bug经常不能够稳定复现，大部分都是因为有些地方没有加锁，或者线程没有被唤醒。在debug的时候往往需要重复运行同一个样例，甚至要比较能正确运行和不能正确运行的样例的一些区别。

### 调度分配问题

在第二次互测的时候，我的程序在所有电梯全部reset的时候，如果一次性调度大量样例，调度器就会将样例分配给同一个电梯。我因此而被hack。究其原因，主要是我的影子电梯在模拟reset过程中的电梯的时候，忘记考虑缓冲池中的乘客了。结果模拟的时候，哪怕第一个电梯缓冲池中已经加了很多乘客，六个电梯模拟的时间仍然相同。考虑缓冲池中的乘客后就没有这个问题了。

### Debug方法

在多线程中，调试几乎是没有什么作用的。最好的debug方法就是print。比如我在第三次作业中查找程序无法结束的bug是，我会在每一个线程结束的时候都打印一条信息，然后根据程序打印的信息就能很轻松地判断到底是什么线程没有结束，然后就可以更有针对性地找出bug了。

## 心得体会

### 线程安全

我认为确保线程安全最关键的一点，就是清楚各个线程之间都共享了哪些数据，然后给这些数据加上锁。在设计的时候，我们其实应该尽可能避免线程之间的复杂共享关系，这一点我做的其实不太好，最后一次作业中，调度器、缓冲池、电梯三个线程之间两两都存在信息共享，这其实是非常容易出现线程安全问题的，而且这也没有符合高内聚、低耦合的设计原则。

### 层次化设计

想要使代码具有一定的层次化，就必须要有一个良好的架构，这一单元作业我主要还是采用了生产者-消费者架构，第一次作业中，输入线程把乘客放入请求列表，电梯从请求列表中取出乘客。在后续的作业中，结构越来越复杂，但是整体上仍然保持了生产者-消费者架构。输入线程、调度线程、缓冲线程、电梯线程，可以说，每一级都是前一级的消费者，又是后一级的生产者。感觉这种架构又比较类似于流水线架构，每一级负责对应的工作，做完之后，将乘客发给下一级。总而言之，本单元作业的整体结构还是具有一定的层次性的，只可惜各个线程之间耦合的内容有点多，感觉仍然有优化空间。

### 总结

这个单元是我第一次接触多线程，在写第一次作业之前，我花了很大的功夫去研究java多线程的编程方式。在作业的迭代中，我也不断对多线程有了更加深刻的认识。