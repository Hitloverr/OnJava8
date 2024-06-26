多态提供了另一个维度的接口与实现分离，以解耦做什么和怎么做。多态不仅能改
善代码的组织，提高代码的可读性，而且能创建有扩展性的程序

封装通过合并特征和行为来创建新的数据类型。隐藏实现通过将细节私有化把接
口与实现分离。多态是消除类型之间的耦合。在上一章中，继承允许把一个对象视为它本身的类型或
它的基类类型。这样就能把很多派生自一个基类的类型当作同一类型处理，因而一段代
码就可以无差别地运行在所有不同的类型上了。多态方法调用允许一种类型表现出与
相似类型的区别，只要这些类型派生自一个基类。这种区别是当你通过基类调用时，由
方法的不同行为表现出来的。


- 如果只写一个方法以基类作为参数，而不用管是哪个具体派生类，这样会变得更好
  吗？也就是说，如果忘掉派生类，编写的代码只与基类打交道，会不会更好

# 方法调用绑定
将一个方法调用和一个方法主体关联起来称作绑定。若绑定发生在程序运行前（如
果有的话，由编译器和链接器实现），叫做前期绑定。

解决方法就是后期绑定，意味着在运行时根据对象的类型进行绑定。后期绑定也称
为动态绑定或运行时绑定。当一种语言实现了后期绑定，就必须具有某种机制在运行时
能判断对象的类型，从而调用恰当的方法。也就是说，编译器仍然不知道对象的类型，
但是方法调用机制能找到正确的方法体并调用。每种语言的后期绑定机制都不同，但是
可以想到，对象中一定存在某种类型信息。

Java 中除了 static 和 final 方法（private 方法也是隐式的 final）外，其他所有
方法都是后期绑定。它有效地” 关闭了 “动态绑定，或者说告诉编译器不需要对其进行
动态绑定。这可以让编译器为 final 方法生成更高效的代码。

在编译时，编译器不需要知道任何具体信息以进
行正确的调用。所有对方法 draw() 的调用都是通过动态绑定进行的


代码中的修改不会破坏程序中其他不应受到影响的部分。换句
话说，多态是一项 “将改变的事物与不变的事物分离” 的重要技术

只有非 private 方法才能被覆写，只有普通的方法调
用可以是多态的。例如，如果你直接访问一个属性，该访问会在编译时解析。
也就是说字段 和 static的方法是没有多态行为的。静态的方法只与类关联，与单个的对象无关

# 对象的构造器顺序
对象的构造器调用顺序如下：
1. 基类构造器被调用。这个步骤重复递归，直到根基类的构造器被调用，然后是它
   的派生类，以此类推，直到最底层的派生类构造器被调用。
2. 按声明顺序初始化成员。
3. 最终调用派生类的构造器。

当使用继承时，就已经知道了基类的一切，并可以访问
基类中任意 public 和 protected 的成员。这意味着在派生类中可以假定所有的基类成
员都是有效的。在一个标准方法中，构造动作已经发生过，对象其他部分的所有成员都
已经创建好。
在构造器中必须确保所有的成员都已经构建完。唯一能保证这点的方法就是首先
调用基类的构造器。接着，在派生类的构造器中，所有你可以访问的基类成员都已经初
始化。另一个在构造器中能知道所有成员都是有效的理由是：无论何时有可能的话，你
应该在所有成员对象（通过组合将对象置于类中）定义处初始化它们（例如，例子中的
b、c 和 l）。如果遵循这条实践，就可以帮助确保所有的基类成员和当前对象的成员对
象都已经初始化。

# 继承和清理
比如你自己定义了一个清理方法dispose，基类中有个方法dispose方法，子类中要做额外清理动作的话，覆写dispose方法，但你要记得调用super.dispose(),不然基类的清理动作不会发生。


销毁的顺序应该与初始化的顺序相反，以防一个对象依赖另一个对
象。对于属性来说，就意味着与声明的顺序相反（因为属性是按照声明顺序初始化的）。
对于基类（遵循 C++ 析构函数的形式），首先进行派生类的清理工作，然后才是基类
的清理。这是因为派生类的清理可能调用基类的一些方法，所以基类组件这时得存活，
不能过早地被销毁

一旦某个成员对象被其它一个或多个
对象共享时，问题就变得复杂了，不能只是简单地调用 dispose()。这里，也许就必须
使用引用计数来跟踪仍然访问着共享对象的对象数量

```java
class Shared {
private int refcount = 0;
    private static long counter = 0;
    private final long id = counter++;
    Shared() {
        System.out.println("Creating " + this);
    }
    public void addRef() {
        refcount++;
    }
    protected void dispose() {
        if (--refcount == 0) {
            System.out.println("Disposing " + this);
        }
    }
    @Override
    public String toString() {
        return "Shared " + id;
    }
}
class Composing {
    private Shared shared;
    private static long counter = 0;
    private final long id = counter++;
    Composing(Shared shared) {
        System.out.println("Creating " + this);
        this.shared = shared;
        this.shared.addRef();
    }
    protected void dispose() {

        System.out.println("disposing " + this);
        shared.dispose();
    }
    @Override
    public String toString() {
        return "Composing " + id;
    }
}
public class ReferenceCounting {
    public static void main(String[] args) {
        Shared shared = new Shared();
        Composing[] composing = {
                new Composing(shared),
                new Composing(shared),
                new Composing(shared),
                new Composing(shared),
                new Composing(shared),
        };
        for (Composing c: composing) {
            c.dispose();
        }
    }
}
```

static long counter 跟踪所创建的 Shared 实例数量，还提供了 id 的值。counter
的类型是 long 而不是 int，以防溢出（这只是个良好实践，对于本书的所有示例，counter
不会溢出）。id 是 final 的，因为它的值在初始化时确定后不应该变化。
在将一个 shared 对象附着在类上时，必须记住调用 addRef()，而 dispose() 方
法会跟踪引用数，以确定在何时真正地执行清理工作

## 构造器中调用覆写的方法

一个动态绑定的方法调用向外深入到继
承层次结构中，它可以调用派生类的方法。如果你在构造器中这么做，就可能调用一个
方法，该方法操纵的成员可能还没有初始化——这肯定会带来灾难


初始化
的实际过程是：
1. 在所有事发生前，分配给对象的存储空间会被初始化为二进制 0。
2. 如前所述调用基类构造器。此时调用覆写后的 draw() 方法（是的，在调用 RoundGraph 构造器之前调用），由步骤 1 可知，radius 的值为 0。
3. 按声明顺序初始化成员。
4. 最终调用派生类的构造器。

编写构造器有一条良好规范：做尽量少的事让对象进入良好状态。如果有
可能的话，尽量不要调用类中的任何方法。在构造器中唯一能安全调用的只有基类的
final 方法（包括 private 方法，它们自动属于 final）。这些方法不能被覆写，因此不
会产生意想不到的结果。

# 协变返回类型
Java 5 中引入了协变返回类型，这表示派生类的被覆写方法可以返回基类方法返
回类型的派生类型。协变返回类型允许返回更具体的类型。


## 组合
Stage 对象中包含了 Actor 引用，该引用被初始化为指向一个 HappyActor 对
象，这意味着 performPlay() 会产生一个特殊行为。但是既然引用可以在运行时与其
他不同的对象绑定，那么它就可以被替换成对 SadActor 的引用，performPlay() 的
行为随之改变。这样你就获得了运行时的动态灵活性（这被称为状态模式）。与之相反，
我们不能在运行时决定继承不同的对象，那在编译时就完全确定下来了。

使用继承表达行为的差异，使用属性表达状态的变化。在上个例
子中，两者都用到了。通过继承的到的两个不同类在 act() 方法中表达了不同的行为，
Stage 通过组合使自己的状态发生变化。这里状态的改变产生了行为的改变


## 继承与替代
有些派生类有些特殊的专属的接口。

在 Java 中，每次转型都会被检查！所以即使只是进行一次普通的加括号形式的类型转
换，在运行时这个转换仍会被检查，以确保它的确是希望的那种类型。如果不是，就会
得到 ClassCastException （类转型异常）。这种在运行时检查类型的行为称作运行时类
型信息。


