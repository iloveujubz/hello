package sut.game01.core;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import react.UnitSlot;
import sut.game01.core.sut.game01.core.sprite.Bomb;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

//import tripleplay.entity.World;

public class TestScreen extends UIScreen {

    public static float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private World world;
    private Root root;
    private Bomb bomb;
    private Bomb bomb1;
    private final ScreenStack ss;
    private static boolean showDebugDraw = false;
    private DebugDrawBox2D debugDraw;

    public TestScreen(ScreenStack ss){
        this.ss = ss;
    }

    public void wasAdded() {
        super.wasAdded();
        Image bgImage = assets().getImage("images/home.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);
        Image roImage = assets().getImage("images/123.png");
        ImageLayer roLayer = graphics().createImageLayer(roImage);
        graphics().rootLayer().add(roLayer);
        float x = 230.0f ;
        float y = 100.0f ;
        roLayer.setTranslation(x, y);
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });


        Vec2 gravity = new Vec2(0.0f, 10.0f);
        world = new World(gravity, true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        if (showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int) (width / TestScreen.M_PER_PIXEL),
                    (int) (height / TestScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                               DebugDraw.e_jointBit |
                               DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 1f / TestScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }
        Body ground = world.createBody(new BodyDef());
        PolygonShape groundShapeB = new PolygonShape();
        groundShapeB.setAsEdge(new Vec2(2f, height-2),new Vec2(width-2f, height-2));
        ground.createFixture(groundShapeB, 0.0f);
        PolygonShape groundShapeU = new PolygonShape();
        groundShapeU.setAsEdge(new Vec2(2f, height-17),new Vec2(width-2f, height-17));
        ground.createFixture(groundShapeU, 0.0f);
        PolygonShape groundShapeL = new PolygonShape();
        groundShapeL.setAsEdge(new Vec2(2f, height-17),new Vec2(2f, height-2));
        ground.createFixture(groundShapeL, 0.0f);
        PolygonShape groundShapeR = new PolygonShape();
        groundShapeR.setAsEdge(new Vec2(width-2f, height-17),new Vec2(width-2f, height-2));
        ground.createFixture(groundShapeR, 0.0f);

        bomb = new Bomb(world, 300f, 400f);
        layer.add(bomb.layer());
        bomb1 = new Bomb(world, 150f, 400f);
        layer.add(bomb1.layer());


    }

    public void wasShown() {
        super.wasShown();
        root = iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(), layer);
        root.setSize(width(), height());
        root.layer.setTranslation(-230f, 200f);
        root.add(new Button("Back").onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                ss.remove(TestScreen.this);
            }
        }));
    }

    @Override
    public void update(int delta){
        bomb.update(delta);
        bomb1.update(delta);
        world.step(0.033f, 10, 10);
        super.update(delta);
    }
    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
        bomb.paint(clock);
        bomb1.paint(clock);
    }
}
