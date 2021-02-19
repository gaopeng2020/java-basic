package patterns.creation.builder.improve;

import java.util.Base64;

/**
 * 建造者模式基本介绍
 * 基本介绍
 * 1）建造者模式（Builder Pattern）又叫生成器模式，是一种对象构建模式。它可以将复杂对象的建造过程抽象出来（抽象类别），
 * 使这个抽象过程的不同实现方法可以构造出不同表现（属性）的对象。
 * 2）建造者模式是一步一步创建一个复杂的对象，它允许用户只通过指定复杂对象的类型和内容就可以构建它们，
 * 用户不需要知道内部的具体构建细节。
 * <p>
 * 建造者模式的四个角色
 * 1）Product（产品角色）：一个具体的产品对象。
 * 2）Builder（抽象建造者）：创建一个Product对象的各个部件指定的接口。
 * 3）ConcreteBuilder（具体建造者）：实现接口，构建和装配各个部件。
 * 4）Director（指挥者）：构建一个使用Builder接口的对象。它主要是用于创建一个复杂的对象。
 * 它主要有两个作用，一是：隔离了客户与对象的生产过程，二是：负责控制产品对象的生产过程。
 * <p>
 * 四个角色关系梳理：
 * Builder是核心，将产品的构建方法抽象出来，它组合了Product对象，
 * Product是对产品属性的描述；
 * ConcreteBuilder是Builder的实现类；
 * Director负责产品对象的构建过程，决定产品构造方法的调用顺序，聚合了Builder对象
 */
public class Client {
    public static void main(String[] args) {
        HighBuildingConcreteBuilder hightBuildingBuilder = new HighBuildingConcreteBuilder();
        HouseDirector houseDirector = new HouseDirector(hightBuildingBuilder);
        House house = houseDirector.constructHouse();
        String baise = house.getBaise();
        System.out.println("baise = " + baise);
    }
}
