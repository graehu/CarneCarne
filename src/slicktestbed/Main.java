
package slicktestbed;

import org.newdawn.slick.*;
import Game.Game;



public class Main extends BasicGame
{
    public Main()
    {
        super("SlickTestbed");
    }
    public static void main(String[] arguments)
    {
        //setup native libs
        NativeLibLoader nativeLibLoader = new NativeLibLoader("org.lwjgl.librarypath");
        nativeLibLoader.init();
        nativeLibLoader.setupPath();
        try
        {
            AppGameContainer app = new AppGameContainer(new Main());
            app.setDisplayMode(800, 600, false);
            app.start();
        }
        catch(SlickException e)
        {
            e.printStackTrace();
        }
    }

    //member variables
    /*World myWorld;
    private TiledMap tiledMap;
    private SpriteSheet character;
    private Animation characterSpite,char2,char3,char4;
    private Body body1, body2;
    float [] charPos;*/
    Game mGame;
    

    //Override
    public void init(GameContainer container) throws SlickException
    {
        mGame = new Game();
        
        /*myWorld = new World(new AABB(new Vec2(-110,-100), new Vec2(100, 100)),new Vec2(0,9.8f),false);
        PolygonDef shape = new PolygonDef();
        shape.setAsBox(2, 2);
        shape.density = 1;
        BodyDef def = new BodyDef();
        def.massData = new MassData();
        def.massData.mass = 1;
        def.position.x = 50;
        body1 = myWorld.createBody(def);
        body1.createShape(shape);
        
        body1.setMassFromShapes();
        def.position.y = 2;
        shape.setAsBox(30, 30);
        body2 = myWorld.createBody(def);
        body2.createShape(shape);
        
        
        
        
        
        charPos = new float[]{45,45};
        character = new SpriteSheet(new Image("data/sprites.png"), 32, 32);
        int animSpeed = 100;
        int [] frames = {0,1,1,1,2,1,3,1,4,1};
        int [] duration = {animSpeed,animSpeed,animSpeed,animSpeed,animSpeed};
        characterSpite = new Animation(character,frames, duration);
        char2 = new Animation(character,frames, duration);
        char3 = new Animation(character,frames, duration);
        char4 = new Animation(character,frames, duration);
        
        tiledMap = new TiledMap("data/map.tmx");*/
    }

    //Override
    public void update (GameContainer _container, int _delta) throws SlickException
    {
        mGame.update(_container, _delta);
        //handle input
        /*Input input = _container.getInput();
        charPos[0] = body1.getPosition().x;
        charPos[1] = body1.getPosition().y;
        float time = _delta;
        myWorld.step(time/1000.0f, 8);*/
    }

    public void render(GameContainer container, Graphics g) throws SlickException
    {
        mGame.render(container);
        /*tiledMap.render(0, 0);
        character.startUse();
        for(int i= 0; i < 100; i++)
        {
            characterSpite.renderInUse((int)charPos[0],(int)charPos[1]);
            char2.renderInUse((int)charPos[0]+32, (int)charPos[1]+32);
            char3.renderInUse((int)charPos[0]-32, (int)charPos[1]-32);
            char4.renderInUse((int)charPos[0]+64, (int)charPos[1]+64);
            //characterSpite.draw((int)charPos[0],(int)charPos[1]);
       }
        character.endUse();*/
    }

}
