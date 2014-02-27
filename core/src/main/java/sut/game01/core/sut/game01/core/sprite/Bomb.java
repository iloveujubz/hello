package sut.game01.core.sut.game01.core.sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.TestScreen;

public class Bomb {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private Body body;

    public enum State {
        IDEL, DIE
    };
    private State state = State.IDEL;
    private int e = 0;
    private int offset = 0;
    private int t = 0;
    private int hp = 100;

    public Bomb(final World world, final float x, final float y){
        sprite = SpriteLoader.getSprite("images/bomb.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() /2f,
                sprite.height() /2f);
                sprite.layer().setTranslation(x, y + 13f);

                body = initPhysicsBody(world,
                                       TestScreen.M_PER_PIXEL * x,
                                       TestScreen.M_PER_PIXEL * y);
                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
        PlayN.keyboard().setListener(new Keyboard.Listener() {
            @Override
            public void onKeyDown(Keyboard.Event event) {

            }

            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {

            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                body.applyLinearImpulse(new Vec2(100f, 0f), body.getPosition());
            }
        });
    }
    public Layer layer(){
        return sprite.layer();
    }
    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0, 0);
        Body body =  world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(56 * TestScreen.M_PER_PIXEL / 2,
                sprite.layer().height() * TestScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);
    return body;
    }

    
   public void update(int delta){
        if (!hasLoaded)return;
        e += delta;
        t += delta;
        if(e > 150){
            switch (state){
                case IDEL: offset = 0;
                    break;
                case DIE: offset = 4;
                    break;
            }
            spriteIndex = offset + ((spriteIndex + 1) %4);
            sprite.setSprite(spriteIndex);
        }
        if(t>1000){
          hp -=50;
            t=0;
        }
        if(hp<=0){
            state = State.DIE;
            sprite.setSprite(7);
        }
    }
    public void paint(Clock clock){
        if (!hasLoaded) return;
        sprite.layer().setTranslation((body.getPosition().x / TestScreen.M_PER_PIXEL) - 10,
                                       body.getPosition().y / TestScreen.M_PER_PIXEL);

    }
}
