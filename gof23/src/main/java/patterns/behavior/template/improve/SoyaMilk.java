package patterns.behavior.template.improve;

/**
 * 模板方法模式的钩子方法
 * 1）在模板方法模式的父类中，我们可以定义一个方法，它默认不做任何事，子类可以视情况要不要覆盖它，该方法称为“钩子”
 * 2）还是用上面做豆浆的例子来讲解，比如，我们还希望制作纯豆浆，不添加任何的配料，请使用钩子方法对前面的模板方法进行改造
 * 3）看老师代码演示：
 */
//抽象类，表示豆浆
public abstract class SoyaMilk {

    //模板方法, make , 模板方法可以做成final , 不让子类去覆盖.
    final void make() {

        select();
        if (customerWantCondiments()) {
            addCondiments();
        }
        soak();
        beat();

    }

    //选材料
    void select() {
        System.out.println("第一步：选择好的新鲜黄豆  ");
    }

    //添加不同的配料， 抽象方法, 子类具体实现
    abstract void addCondiments();

    //浸泡
    void soak() {
        System.out.println("第三步， 黄豆和配料开始浸泡， 需要3小时 ");
    }

    void beat() {
        System.out.println("第四步：黄豆和配料放到豆浆机去打碎  ");
    }

    //钩子方法，决定是否需要添加配料
    boolean customerWantCondiments() {
        return true;
    }
}
