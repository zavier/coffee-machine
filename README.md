《敏捷软件开发：原则、模式与实践》
> 咖啡的启示

## 规格说明书

Mark IV 型专用咖啡机一次可以产出12杯咖啡。使用者把过滤器放置在支架上，在其中装入研磨好的咖啡，然后把支架推入到盛放它的容器中。
接着，使用者向滤水器中倒入12杯水并按下冲煮按钮。水一直加热到沸腾。不断产出的水蒸气压力使水喷洒在咖啡粉末上，形成的水滴通过过滤器流入到
咖啡壶中。咖啡壶由一个保温盘长期保温，仅当壶中有咖啡时，保温盘才工作。如果在水还在向咖啡粉喷洒时从保温盘上拿走咖啡壶，水流就会停止，
这样煮好的咖啡就不会溅在保温盘上。

以下是需要监控的硬件设备。
- 加热器的加热元件。可以开启和关闭。
- 保温盘的加热元件。可以开启和关闭。
- 保温盘传感器。它有三个状态：warmerEmpty, potEmpty和potNotEmpty
- 加热传感器，用来判断是否有水。它有两个状态：boilerEmpty和boilerNotEmpty
- 冲煮按钮。这个瞬时按钮启动冲煮流程。它有一个指示灯，当冲煮流程结束时亮，表示咖啡已经煮好
- 减压阀门，在开启时可以降低加热器中的压力。压力的降低会阻止水流向过滤器。改阀门可以开启和关闭。


## 分析
根本的问题：如何煮咖啡？

如何煮咖啡？最简单、最常见的方法是把热水倒在研磨好的咖啡上，并把冲泡好的咖啡液体收集在某种器皿中。
- 热水从那里来？从HotWaterSource来
- 把咖啡存放在什么地方？存放在ContainmentVessel中

加热器、阀门以及加热传感器在充当HotWaterSource的角色，
HotWaterSource负责把水加热并喷洒在研磨好的咖啡上，形成溶液流入ContainmentVessel中

保温盘及其传感器在充当ContainmentVessel的角色，它负责保持所存放咖啡的温度，并让我们知道容器中是否留有咖啡


**还需要添加一个UserInterface类**

主要的三个类如下：
1. UserInterface
2. HotWaterSource
3. ContainmentVessel


## 用例

### 用例1：使用者按下了冲煮按钮
使用UserInterface检测使用者按下冲煮按钮，这时我们要启动热水流，
但是在此之前，我们需要确保ContainmentVessel已经做好了接收咖啡的准备。
同时，最好也确保HotWaterSource已经就绪了，即：
- 热水器中已经加满水
- 咖啡壶是空的并且已经放在了保温盘上

因此，UserInterface对象首先要向HotWaterSource和ContainmentVessel发送消息询问它们是否已经准备好。
只要有一个结果为false，就拒绝冲煮咖啡。
如果询问结果都为true，那么就需要启动热水流。UserInterface对象应该向HotWaterSource发送start消息。
接着HotWaterSource就需要开始启动产生热水流所需的工作：关闭阀门，开启热水器


### 用例2：接收器皿没有准备好
当在主咖啡时，使用者可以把咖啡壶从保温盘上取走。ContainmentVessel负责检测咖啡壶是否已经被拿走。
当发生这种情况时，必须得中断咖啡流。因此，ContainmentVessel必须能够告诉HotWaterSource停止传送热水。
同样，当咖啡壶被重新放回后，它必须能够告诉HotWaterSource在再次开启热水流


### 用例3：冲煮完成
每个对象都可以告诉其他对象冲煮结束了，冲煮结束处理
- UserInterface 点亮指示灯
- HotWaterSource 停止热水流，关闭加热器，打开阀门

### 用例4：咖啡喝完了
当冲煮结束并且一个空的咖啡壶被放在保温盘上时，ContainmentVessel检测到这个事件，向UserInterface发送Complete消息，关掉指示灯


*高层的咖啡制作策略不能依赖于底层的实现（Mark IV咖啡机），所以这三个类都绝对不能知道关于 Mark IV的任何信息*

