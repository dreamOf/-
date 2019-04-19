1.NIO是什么？

java NIO全称java non-blocking IO,是指jdk1.4及以上版本提供的New IO。

2.区别（相对于传统IO）

| IO                        | NIO                           |
| ------------------------- | ----------------------------- |
| 面向流（Stream oriented） | 面向缓冲区（Buffer oriented） |
| 阻塞IO（Blocking IO）     | 非阻塞（Non Blocking IO）     |
| 无                        | 选择器（Selectors）           |

形象的比喻：传统IO就像是自来水管，只能单向的传送。连通之后，一直处于传送的状态，即使没有数据。

NIO就像是火车轨道，轨道本身无法运输旅客，要通过火车才能达到运输的目的。这里的轨道就如同通道，而火车就相当于缓冲区，缓冲区负责数据的存取。

3.核心概念

​	NIO的核心在于：通道和缓冲区。

​	通道：通道标识打开到IO设备，如文件、套接字的连接。

​	缓冲区：一个用于存储特定基本数据类型的容器。所有缓冲区都是Buffer抽象类的子类。

​	若使用NIO系统需要	获取用于IO设备的通道以及用于存储数据的缓冲区，然后操作缓冲区对数据进行处理了。缓冲区负责与通道进行交互，数据从缓冲区写入通道中，从通道中读入缓冲区。

4.缓冲区

4.1缓冲区的类型

| 名称         | 描述 |
| ------------ | ---- |
| ByteBuffer   |      |
| CharBuffer   |      |
| ShortBuffer  |      |
| IntBuffer    |      |
| LongBuffer   |      |
| FloatBuffer  |      |
| DubbleBuffer |      |

*注意：没有布尔类型的buffer.

他们都采用相同的方法管理数据。只是各自管理的数据类型不同。通过如下方式进行获取：

```java
XxxBuffer.allote(int capacity);
```

4.2.缓冲区的重要概念

| 名词                                       | 描述                                                         |
| ------------------------------------------ | ------------------------------------------------------------ |
| 容量（**capacity**）                       | 标识buffer最大数据容量，缓冲区的容量不能为负数，且创建后不能更改。 |
| 限制（**limit**）                          | 第一个不能读取或者写入的数据索引，即位于limit后的数据不可读写。缓冲区的限制不能为负数，且不能大于容量。 |
| 位置（**positon**）                        | 下一个要读取或者写入的索引。缓冲区的位置不能为负数，且不能大于容量。 |
| 标记与重置           (**mark and reset**） | 标记是一个索引，通过Buffer中的mark()方法指定Buffer中的一个特定的positon，之后可以通过reset()恢复到这个positon. |

4.3缓冲区的重要方法

| 方法名       | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| put          | 存入数据                                                     |
| get          | 取数据。                                                     |
| flip         | 翻转缓冲区，切换到读取数据的模式。移动limit的位置到当前的position,把positon的位置设置成0，如果定义了mark,则进行丢弃。 |
| rewind       | 倒退缓冲区。把positon的位置设置成0，并且丢弃mark。           |
| clear        | 清空缓冲区。指针恢复到原始的状态。数据处于被遗忘状态。（数据还存在） |
| hasRemaining | 缓冲区是否还有可取的数据。（在当前position和limit之间）      |
| Remaining    | 缓冲区的可取数据的个数。（在当前position和limit之间）        |

5) 直接缓冲区和非直接缓冲区

非直接缓冲区：通过allocate()方法分配的缓冲区，将缓冲区建立在JVM中。

直接缓冲区：通过allocateDirect()方法分配的缓冲区，缓冲区建立在物理内存中。可以提高效率。

为何建立在物理内存中能提高效率？

1. 字节缓冲区要么是直接的，要么是非直接的。如果为直接字节缓冲区，则java虚拟机会尽最大努力直接在此缓冲区上执行本机I/O操作。也就是说，在每次调用基础操作系统的一个本机I/O操作之前（或之后），虚拟机都会尽量避免将缓冲区的内容复制到中间缓冲区（或从中间缓冲区复制内容）。
2. 直接缓冲区可以调用此类的allocateDirect()方法创建。此方法返回的**缓冲区分配和取消分配所需成本通常高于非缓冲区**。直接缓冲区的内容可以驻留在常规的垃圾回收推之外，因此，他们对应用程序的内存需求造成的影响可能并不明显。所以，建议将直接缓冲区主要分配给哪些易受基础系统的本机I/O操作影响大型、持久的缓冲区。一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好处时分配。
3. 直接字节缓冲区还可以通**过FileChanel的map方法将文件区域直接映射到内存**来创建。该方法返回MappedByteBuffer.JAVA平台的实现有助于通过JNI从本机代码创建直接字节缓冲区。如果以上这些缓冲区中的某个缓冲区实例指的是不可访问的内存区域，则试图访问该区域不会更改缓冲区的内容，并且将会在访问期间或稍后的某个时间导致抛出不确定的异常。
4. 字节缓冲区是直接缓冲区还是非直接缓冲区可通过isDirect()方法来确定，提供此方法是为了能供在性能关键 型代码中执行显示缓冲区管理。

6）通道

`标识IO源与目标打开的连接。Channel类似传统的“流”。只不过它本身无法访问数据，只能与Buffer进行交互。`

主要类别：

| FileChannel         |      |      |
| ------------------- | ---- | ---- |
| socketChannel       |      |      |
| serverSocketChannel |      |      |
| datagramChannel     |      |      |

获取通道：

```java
1.java 针对支持通道的类提供了 getChannel()方法
FileInputStream/FileoutputStream
RandomAccessFile
Socket
serverSocket
DatagramSocket
2.jdk1.7中的NIO.2,针对各个通道提供了静态方法：open()
3.jdk1.7中的NIO.2的NIO.2的Files工具类的newByteChannel()   
```

例1：

```java
//写入数据
			FileOutputStream fos = new FileOutputStream("channel1.txt");
			FileChannel channel = fos.getChannel();
			ByteBuffer allocate1 = ByteBuffer.allocate(1024);
			String u="中国的首都是北京，美国的首都是华盛顿！";
			allocate1.put(u.getBytes());
			allocate1.flip();
 			channel.write(allocate1);
			channel.close();
			//读取数据
			FileInputStream fis = new FileInputStream("channel1.txt");
			FileChannel channel2 = fis.getChannel();
			ByteBuffer allocate = ByteBuffer.allocate(1024);
			while(channel2.read(allocate) != -1) {
				allocate.flip();
				byte[] dst=new byte[1024];
				allocate.get(dst,0,allocate.limit());
				System.out.println(new String(dst));
				allocate.clear();
			}
			channel2.close();
```

例子2：

```java
#读
FileChannel open = FileChannel.open(Paths.get("channel1.txt"),StandardOpenOption.READ);

#写		
FileChannel open = FileChannel.open(Paths.get("channel1.txt"), StandardOpenOption.WRITE);
```

7)阻塞和非阻塞

使用NIO完成网络通信的三个核心。

通道：负责数据传输。

缓存：负责数据传输。

选择器：是selectableChannel的多路复用器。用于监听selectableChannel的IO状况。一个选择器监听多个channel的可读、可写状态，从而实现单线程管理多个channel,也就是管理多个网络连接。

**好处：**使用更好的线程来处理通道，相对于多线程，减少了线程上下文切换带来的开销。

**使用方法：**

```java
1.创建
Selector selector = Selector.open();
2.注册channel到selector
channel.configureBlocking(false);
SelectKey key = channel.reigster(selector,SelectionKey.OP_READ);


    
```

注意:Channel必须是非阻塞的，所以FileChannel不适用Selector,因为FileChannel无法切换成非阻塞模式，更准确的说是FileChannel没有继承SelectableChannel.
​    SelectableChannel抽象类的configureBlocking（） 方法是由 AbstractSelectableChannel抽象类实现的，SocketChannel、ServerSocketChannel、DatagramChannel都是直接继承了 AbstractSelectableChannel抽象类.

reigster的第二个参数用于监听Channel的事件，类型有**Connect,Accept,Read,Write**.

通道出发了一个事件之后，该事件就进入就绪状态。比如一个通道连接到了另一个服务器称为“连接就绪”，一个通道准备接收新进入的连接称为“接受就绪”。一个通道处于读状态称为“读就绪”，一个通道处于写状态称为“写就绪”

该四种状态，用SelectionKey的四个常量表示：

SelectionKey.OP_CONNECT,SelectionKey.OP_ACCEPT,SelectionKey.OP_READ,SelectionKey.OP_WRITE

如果对多个事件进行监听，用“|”进行分隔。

**selectionKey介绍**：

```java
key.attachment():返回selectionKey的attachment,attchment可以在注册channel的时候指定。
key.channel():返回该selectionKey对应的channel.
key.selector():返回该selectionKey对应的selector.
key.readyOps():返回一个bit mask,代表相应的channel上可以进行的IO操作。
```

**key.interestOpts()方法：**对channel的某件事情感兴趣。

```java
int interestSet = selectionKey.interestOps();
boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_CONNECT;
boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_READ;
boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_WRITE;
```

**key.readyOps()方法**：ready集合是通道已经准备就绪的操作的集合.

```java
int readySet = selectionKey.readyOps();
key.isAcceptable();
key.isWritable();
key.isReadable();
```

通过selectionKey访问channel和Selector:

```java
Channel channel = key.channel();
Selector selector = key.selector();
key.attechement();
```

可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道。例如，可以附加 与通道一起使用的Buffer，或是包含聚集数据的某个对象。使用方法如下：

```java
key.attach(theObject);
Object attachedObj = key.attachment();
```

还可以在用register()方法向Selector注册Channel的时候附加对象。如：

```java
SelectionKey key = channel.register(selector, SelectionKey.OP_READ, theObject);
```

**从selector中选择channel** :

selector维护的三种类型的SelectionKey集合：

```java
1.已注册的键集合：所有与选择器关联的通道所生成的键的集合称为已经注册的键的集合.并不是所有注册过的键都仍然有效。这个集合通过 keys() 方法返回，并且可能是空的。这个已注册的键的集合不是可以直接修改的；试图这么做的话将引发java.lang.UnsupportedOperationException。
2.已选择的键集合：通过Selector的select（）方法可以选择已经准备就绪的通道.select()方法返回的int值表示有多少通道已经就绪,是自上次调用select()方法后有多少通道变成就绪状态。之前在select（）调用时进入就绪的通道不会在本次调用中被记入，而在前一次select（）调用进入就绪但现在已经不在处于就绪的通道也不会被记入.
一旦调用select()方法，并且返回值不为0时，则 可以通过调用Selector的selectedKeys()方法来访问已选择键集合 
3.已取消的键集合:已注册的键的集合的子集，这个集合包含了 cancel() 方法被调用过的键(这个键已经被无效化)，但它们还没有被注销。这个集合是选择器对象的私有成员，因而无法直接访问。

```

**停止选择的方法：**

选择器执行选择的过程，系统底层会依次询问每个通道是否已经就绪，这个过程可能会造成调用线程进入阻塞状态,那么我们有以下两种方式可以唤醒在select（）方法中阻塞的线程。

```java
1.wakeup():通过调用Selector对象的wakeup（）方法让处在阻塞状态的select()方法立刻返回
该方法使得选择器上的第一个还没有返回的选择操作立即返回。如果当前没有进行中的选择操作，那么下一次对select()方法的一次调用将立即返回。
2.close():通过close（）方法关闭Selector，该方法使得任何一个在选择操作中阻塞的线程都被唤醒（类似wakeup（）），同时使得注册到该Selector的所有Channel被注销，所有的键将被取消，但是Channel本身并不会关闭。

```

