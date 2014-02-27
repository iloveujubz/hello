package sut.game01.core;

import playn.core.Font;
import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

public class HomeScreen extends UIScreen {
    private Root root;
    private final ScreenStack ss;

    public HomeScreen(ScreenStack ss){
        this.ss = ss;
    }
    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,24);
    @Override
    public void wasAdded() {
        super.wasAdded();
        Image bgImage = assets().getImage("images/home.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);
        Image startImage = assets().getImage("images/start.png");
        ImageLayer startLayer = graphics().createImageLayer(startImage);
        layer.add(startLayer);
        startLayer.setTranslation(185f, 200f);
        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new TestScreen(ss));
            }
        });
    }
    @Override
    public void wasShown() {
        super.wasShown();
        root = iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(), layer);
        /*root.addStyles(Style.BACKGROUND
                .is(Background.bordered(0xFF00F5FF, 0xFF00F5FF, 5).inset(5, 10)));*/
        root.setSize(width(), height());
        root.add(new Label("Project Test")
            .addStyles(Style.FONT.is(HomeScreen.TITLE_FONT)));
        /*root.add(new Button("Start").onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                ss.push(new TestScreen(ss));
            }
        }));*/
    }
}
