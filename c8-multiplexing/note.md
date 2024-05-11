
- 复用的两种形式(当然可以结合使用)
  - 组合
  - 继承：使用关键字
    extends 后跟基类的名称。当你这样做时，你将自动获得基类中的所有字段和方法

编译器不会为每个引用创建一个默认对象，这是有意义的，因为在许多情况下，这
会导致不必要的开销。初始化引用有四种方法:
1. 当对象被定义时。这意味着它们总是在调用构造函数之前初始化。
2. 在该类的构造函数中。
3. 在实际使用对象之前。这通常称为延迟初始化。在对象创建开销大且不需要每次
   都创建对象的情况下，它可以减少开销。
4. 使用实例初始化。 {语句块中}

如果你试图对未初始化的引用调用方法，则未初始化的引用将产生运行时异常


你可以为每个类创建一
个 main() ; 这允许对每个类进行简单的测试。当你完成测试时，不需要删除 main();
你可以将其留在以后的测试中。即使程序中有很多类都有 main() 方法，惟一运行的
只有在命令行上调用的 main()。这里，当你使用 java Detergent 时候，就调用了
Detergent.main()。。即使类只具有包访问权，也可以访问 public main()。

Java 的super 关键字引用了当前类继承的 “超类”(基类)。

# 初始化基类
构造从基类 “向外” 进行，因此基类在派生类构造函数能够访问它之前进行初始化。
即使不为 Cartoon 创建构造函数，编译器也会为你合成一个无参数构造函数，调用基
类构造函数。

super();

# 委托
将
一个成员对象放在正在构建的类中 (比如组合)，但同时又在新类中公开来自成员对象
的所有方法 (比如继承)

例如，宇宙飞船需要一个控制模块
```java
public class SpaceShipControls {
void up(int velocity) {}
void down(int velocity) {}
void left(int velocity) {}
void right(int velocity) {}
void forward(int velocity) {}
void back(int velocity) {}
void turboBoost() {}
}
```

建造宇宙飞船的一种方法是使用继承
```java
public class
DerivedSpaceShip extends SpaceShipControls {
private String name;
public DerivedSpaceShip(String name) {
this.name = name;
}
@Override
public String toString() { return name; }
public static void main(String[] args) {
DerivedSpaceShip protector =
new DerivedSpaceShip("NSEA Protector");
protector.forward(100);
}
}

```
然而，DerivedSpaceShip 并不是真正的 “一种” SpaceShipControls ，即使你
“告诉” DerivedSpaceShip 调用 forward()。更准确地说，一艘宇宙飞船包含了 SpaceShipControls ，同时 SpaceShipControls 中的所有方法都暴露在宇宙飞船中。委托
解决了这个难题:
```java

public class SpaceShipDelegation {
private String name;
private SpaceShipControls controls =
new SpaceShipControls();
public SpaceShipDelegation(String name) {
this.name = name;
}
    // Delegated methods:
    public void back(int velocity) {
        controls.back(velocity);
    }
    public void down(int velocity) {
        controls.down(velocity);
    }
    public void forward(int velocity) {
        controls.forward(velocity);
    }
    public void left(int velocity) {
        controls.left(velocity);
    }
    public void right(int velocity) {
        controls.right(velocity);
    }
    public void turboBoost() {
        controls.turboBoost();
    }
    public void up(int velocity) {
        controls.up(velocity);
    }
    public static void main(String[] args) {
        SpaceShipDelegation protector =
                new SpaceShipDelegation("NSEA Protector");
        protector.forward(100);
    }
}
```
方法被转发到底层 control 对象，因此接口与继承的接口是相同的。但是，你对委
托有更多的控制，因为你可以选择只在成员对象中提供方法的子集。

## 保证适当的清理

finally

每个类都有自
己的 dispose() 方法来将非内存的内容恢复到对象存在之前的状态。
在清理方法 (在本例中是 dispose() ) 中，还必须注意基类和成员对象清理方法的
调用顺序，以防一个子对象依赖于另一个子对象。首先，按与创建的相反顺序执行特定
于类的所有清理工作。(一般来说，这要求基类元素仍然是可访问的。) 然后调用基类清
理方法

使用自己的清理方法，不要使用finalize（）。

## 怎么选择
当你想在新类中包含一个已有类的功能时，使用组合，而非继承。也就是说，在新
类中嵌入一个对象（通常是私有的），以实现其功能。新类的使用者看到的是你所定义
的新类的接口，而非嵌入对象的接口。

有时让类的用户直接访问到新类中的组合成分是有意义的。只需将成员对象声明
为 public 即可（可以把这当作 “半委托” 的一种）。成员对象隐藏了具体实现，所以这
是安全的。当用户知道你正在组装一组部件时，会使得接口更加容易理解。

当使用继承时，使用一个现有类并开发出它的新版本。通常这意味着使用一个通用
类，并为了某个特殊需求将其特殊化。稍微思考下，你就会发现，用一个交通工具对象
来组成一部车是毫无意义的——车不包含交通工具，它就是交通工具。这种 “是一个”
的关系是用继承来表达的，而 “有一个 “的关系则用组合来表达

尽管可以创建 protected 属性，但是最好的方式是将属性声明为 private 以一直
保留更改底层实现的权利。然后通过 protected 控制类的继承者的访问权限

一种判断使用组合还
是继承的最清晰的方法是问一问自己是否需要把新类向上转型为基类。如果必须向上
转型，那么继承就是必要的，但如果不需要，则要进一步考虑是否该采用继承。“多态”
一章提出了一个使用向上转型的最有力的理由，但是只要记住问一问 “我需要向上转型
吗？”

## 向上转型
从一个更具
体的类转化为一个更一般的类，所以向上转型永远是安全的。也就是说，派生类是基类
的一个超集。它可能比基类包含更多的方法，但它必须至少具有与基类一样的方法。

## final关键字
1. 修饰数据
- 一个永不改变的编译时常量。 
- 一个在运行时初始化就不会改变的值。

一个被 static 和 final 同时修饰的属性只会占用一段不能改变的存储空间。

public 意味着可以在包外访问，static 强调只有一个，final 说明是一个常量。它是 static 的，在加载时已经被初始化，并
不是每次创建新对象时都初始化。

你必须在定义时或在每个构造器中执行 final 变量的赋值操作。这保证了 final 属性在使用前已经被初始化过。

-  final 基本类型参数的使用情况。你只能读取而不能修改
   参数。这个特性主要用于传递数据给匿名内部类

2. 修饰方法
- 第一个原因是给方法上锁，防止子类通过覆写改变方法的行为。这是出于继承的考虑，确保方法的行为不会因继承而改变
- 为了所谓的效率，方法内联，你应该不要想这个事情，因为有时候反而适得其反。
- private （隐式是final的）
- “覆写” 只发生在方法是基类的接口时。也就是说，必须能将一个对象向上转型为基
  类并调用相同的方法（这一点在下一章阐明）。如果一个方法是 private 的，它就不是
  基类接口的一部分。它只是隐藏在类内部的代码，且恰好有相同的命名而已。但是如果
  你在派生类中以相同的命名创建了 public，protected 或包访问权限的方法，这些方法
  与基类中的方法没有联系，你没有覆写方法，只是在创建新的方法而已。由于 private
  方法无法触及且能有效隐藏，除了把它看作类中的一部分，其他任何事物都不需要考虑
  到它。

3. 修饰类。
- 意味着它不能被继承。之所以这么做，是因为类的设计就是永远不需要改动，或者是出于安全考虑不希望它有子
  类。
- 由于 final 类禁止继承，类中所有的方法都被隐式地指定为
  final，所以没有办法覆写它们。你可以在 final 类中的方法加上 final 修饰符，但不会
  增加任何意义。

4. final忠告
   在设计类时将一个方法指明为 final 看上去是明智的。你可能会觉得没人会覆写那
   个方法。有时这是对的。
   但请留意你的假设。通常来说，预见一个类如何被复用是很困难的，特别是通用类。
   如果将一个方法指定为 final，可能会防止其他程序员的项目中通过继承来复用你的类，
   而这仅仅是因为你没有想到它被以那种方式使用。

# 类的加载和初始化

- Java 中万物
皆对象，所以加载活动就容易得多。记住每个类的编译代码都存在于它自己独立的文件
中。该文件只有在使用程序代码时才会被加载。一般可以说 “类的代码在首次使用时加
载 “。这通常是指创建类的第一个对象，或者是访问了类的 static 属性或方法。构造器
也是一个 static 方法尽管它的 static 关键字是隐式的。因此，准确地说，一个类当它
任意一个 static 成员被访问时，就会被加载。

- 首次使用时就是 static 初始化发生时。所有的 static 对象和 static 代码块在加载
  时按照文本的顺序（在类中定义的顺序）依次初始化。static 变量只被初始化一次。

- 继承和初始化
```java
class Insect {
private int i = 9;
protected int j;
Insect() {
System.out.println("i = " + i + ", j = " + j);
j = 39;
}
private static int x1 = printInit("static Insect.x1 initialized");
    static int printInit(String s) {
        System.out.println(s);
        return 47;
    }
}
public class Beetle extends Insect {
    private int k = printInit("Beetle.k.initialized");
    public Beetle() {
        System.out.println("k = " + k);
        System.out.println("j = " + j);
    }
    private static int x2 = printInit("static Beetle.x2 initialized");
    public static void main(String[] args) {
        System.out.println("Beetle constructor");
        Beetle b = new Beetle();
    }
}
输出：
static Insect.x1 initialized
static Beetle.x2 initialized
        Beetle constructor
        i = 9, j = 0
        Beetle.k initialized
        k = 47
        j = 39
```
当执行 java Beetle，首先会试图访问 Beetle 类的 main() 方法（一个静态方法），
加载器启动并找出 Beetle 类的编译代码（在名为 Beetle.class 的文件中）。在加载过
程中，编译器注意到有一个基类，于是继续加载基类。不论是否创建了基类的对象，基
类都会被加载。（可以尝试把创建基类对象的代码注释掉证明这点。）

如果基类还存在自身的基类，那么第二个基类也将被加载，以此类推。接下来，根
基类（例子中根基类是 Insect）的 static 的初始化开始执行，接着是派生类，以此类
推。这点很重要，因为派生类中 static 的初始化可能依赖基类成员是否被正确地初始
化。


至此，必要的类都加载完毕，可以创建对象了。首先，对象中的所有基本类型变量
都被置为默认值，对象引用被设为 null —— 这是通过将对象内存设为二进制零值一举
生成的。接着会调用基类的构造器。本例中是自动调用的，但是你也可以使用 super 调
用指定的基类构造器（在 Beetle 构造器中的第一步操作）。基类构造器和派生类构造
器一样以相同的顺序经历相同的过程。当基类构造器完成后，实例变量按文本顺序初始
化。最终，构造器的剩余部分被执行。


# 总结

在设计一个系统时，目标是发现或创建一系列类，每个类有特定的用途，而且既不
应太大（包括太多功能难以复用），也不应太小（不添加其他功能就无法使用）。如果
设计变得过于复杂，通过将现有类拆分为更小的部分而添加更多的对象，通常是有帮助
的。

当开始设计一个系统时，记住程序开发是一个增量过程，正如人类学习。它依赖实
验，你可以尽可能多做分析，然而在项目开始时仍然无法知道所有的答案。如果把项目
视作一个有机的，进化着的生命去培养，而不是视为像摩天大楼一样快速见效，就能获
得更多的成功和更迅速的反馈。继承和组合正是可以让你执行如此实验的面向对象编
程中最基本的两个工具


