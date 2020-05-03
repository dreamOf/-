## java8新特性

#### 一、简介

**主要内容：**

| 序号 | 内容                       | 描述 |
| ---- | -------------------------- | ---- |
| 1    | Lambda表达式               |      |
| 2    | 函数式接口                 |      |
| 3    | 方法引用和构造器引用       |      |
| 4    | StreamAPI                  |      |
| 5    | 接口中的默认方法于静态方法 |      |
| 6    | 新时间日期API              |      |
| 7    | 其他新特性                 |      |

**主要特性:**

- 速度更快
- 代码更少 （Labbda表达式）
- 强大的Stream API
- 便于并行
- 最大化减少空指针异常 Optional

1. Lambda表达式

   Lambda表达式是一个**匿名函数**，我们可以把Lambda表达式理解为**一段可以传递的代码**（将代码像数据一样传递）。可以写出更简洁、更灵活的代码。作为一种更紧凑的代码风格，使java的语言表达能力得到了提升。


#### 二、lambda表达式的语法

**使用规则：**

```html
(parameters)->{statement;}
```

说明：

- 左侧为参数列表，右侧为主体。
- 可选参数声明：不需要声明参数类型，编译器自动进行判断。
- 可选的参数圆括号：如果只有一个参数，可以不写圆括号。
- 可选的大括号：如果只有一句执行语句，可以不写大括号。
- 可选的return:如果只主体中只有一个语句，编译器自动返回表达式的返回类型。

**函数式接口**：

```
如果一个接口，该接口中只有一个方法，则成为函数式接口。可以在该接口中声明FunctionInterface注解，编译器会判断该类是否为函数式接口。
```

#### 三、四大内置核心函数式接口

| 接口类型 | 接口名        | 参数类型 | 返回类型 | 用途                                                         |
| -------- | ------------- | -------- | -------- | ------------------------------------------------------------ |
| 消费型   | Consumer<T>   | T        | void     | 对类型为T的对象应用操作，包含方法void accept(T t)            |
| 供给型   | Supplier<T>   | 无       | T        | 返回对象为T的方法，包含：T get()                             |
| 函数型   | Function<T,R> | T        | R        | 对类型为T的进行操作，返回R：R apply(T t)                     |
| 判断型   | Predicate<T>  | T        | boolean  | 判断对象T是否满足某约束，并返回boolean值，包含：boolean  test(T t) |

#### 四、方法引用

**定义**：若Lamda体中的内容有方法已经实现了，我们可以使用"方法引用"，可以理解为方法引用是lamda表达式的另外一种表现形式。

**语法格式**：

方法引用：

`1.对象::实例方法名`

`2.类::静态方法名`

`3.类::实例方法名`

Example:

```
 //普通方式
		Consumer<String> con = (x)->System.out.println(x);
		con.accept("普通方式");
//方法引用
		Consumer<String> con2 = System.out::println;
		con2.accept("方法引用");

```

注意：1.参数列表和返回值必须和接口函数一致.

​           2.使用第三种的条件是：参数为两个，且第一个参数是方法的调用者，第二个参数是方法的参数，方可使用。

构造器引用：

 `语法：ClassName::new`

```
 Consumer<String> con = Employee:new;

```

注意：1.构造函数的参数列表和接口函数的参数列表一致.

数组引用：

`语法：`

`1.type::new`

```
Function<int,String[]) fun = String[]:new;
```

**语法规则：**

1. 静态方法引用：ClassName::method
2. 实例方法引用：instance::method
3. 构造方法引用：Class::new
4. 数组构造方法引用：TypeName[]::new

```java
//1.静态方法
Supplier<String> supDep= Employee::getDep;
//2.实例方法
Employee emp = new Employee();
Supplier<String> sup3 = emp::getName;
//3.构造器方法
Consumer<String> con = Employee::new;
//4.数组构造方法
Function<Integer,String[]> fun = String[]::new;
String[] apply = fun.apply(10);
```

#### **五、Stream**

1. 什么是流

   是数据渠道，用于操作数据源（数组、集合等）所生成的元素序列。集合讲 的是数据，流讲的是计算。

   注意：

   - stream 自己不会存储元素。
   - stream不会改变源对象。相反，它会返回一个持有结果的新Stream.
   - stream是延迟执行的，意味着他们会等到需要结果的时候执行。

2. Stream的操作三个步骤

   - 创建Stream.

     一个数据源，获取一个流。

   - 中间操作

     一个中间操作链，对数据源的数据进行处理。

   - 终止操作

     一个终止操作，执行中间操作连（filter,map等），并产生结果。

3. 筛选与切片

