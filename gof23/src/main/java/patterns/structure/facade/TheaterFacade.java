package patterns.structure.facade;

import patterns.structure.facade.theater.DVDPlayer;
import patterns.structure.facade.theater.Popcorn;
import patterns.structure.facade.theater.Projector;
import patterns.structure.facade.theater.Screen;
import patterns.structure.facade.theater.StereoAudio;
import patterns.structure.facade.theater.TheaterLight;

/**
 * 外观模式基本介绍
 * 1）外观模式（Facade），也叫“过程模式：外观模式为子一系列子系统的调用提供一个一致的界面，此模式定义了一个高层接口，
 *    这个接口使得这一子系统更加容易使用
 * 2）外观模式通过定义一个一致的接口，用以屏蔽内部子系统的细节，使得调用端只需跟这个接口发生调用，而无需关心这个子系统的内部细节
 */
public class TheaterFacade {

    //定义各个子系统对象(聚合到Facade)
    private TheaterLight theaterLight;
    private Popcorn popcorn;
    private StereoAudio stereo;
    private Projector projector;
    private Screen screen;
    private DVDPlayer dVDPlayer;


    //构造器
    public TheaterFacade() {
        super();
        this.theaterLight = TheaterLight.getInstance();
        this.popcorn = Popcorn.getInstance();
        this.stereo = StereoAudio.getInstance();
        this.projector = Projector.getInstance();
        this.screen = Screen.getInstance();
        this.dVDPlayer = DVDPlayer.getInstanc();
    }

    //操作分成 4 步

    public void ready() {
        popcorn.on();
        popcorn.pop();
        screen.down();
        projector.on();
        stereo.on();
        dVDPlayer.on();
        theaterLight.dim();
    }

    public void play() {
        dVDPlayer.play();
    }

    public void pause() {
        dVDPlayer.pause();
    }

    public void end() {
        popcorn.off();
        theaterLight.bright();
        screen.up();
        projector.off();
        stereo.off();
        dVDPlayer.off();
    }


}
