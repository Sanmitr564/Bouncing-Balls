import com.badlogic.gdx.ApplicationAdapter; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle; 
import com.badlogic.gdx.math.Circle; 
import com.badlogic.gdx.Input.Keys; 
import com.badlogic.gdx.math.Vector2; 
import com.badlogic.gdx.math.MathUtils; 
import com.badlogic.gdx.math.Intersector; 
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.*; 

//NOTE: Always reset the JVM before compiling (it is the small loop arrow in the
//bottom right corner of the project window)!! 

public class FirstDrawing extends ApplicationAdapter 
{
    private OrthographicCamera camera; //the camera to our world
    private Viewport viewport; //maintains the ratios of your world
    private ShapeRenderer renderer; //used to draw textures and fonts 
    private BitmapFont font; //used to draw fonts (text)
    private SpriteBatch batch; //also needed to draw fonts (text)

    @Override//called once when we start the game
    public void create(){

        camera = new OrthographicCamera(); 
        viewport = new FitViewport(GLOBAL.WORLD_WIDTH, GLOBAL.WORLD_HEIGHT, camera); 
        renderer = new ShapeRenderer(); 
        font = new BitmapFont(); 
        batch = new SpriteBatch();//if you want to use images instead of using ShapeRenderer 

        GLOBAL.balls.add(new Ball(GLOBAL.WORLD_WIDTH / 4, GLOBAL.WORLD_HEIGHT / 2 - GLOBAL.RADIUS, GLOBAL.RADIUS, 0));
        GLOBAL.balls.add(new Ball(GLOBAL.WORLD_WIDTH * 3/4 , GLOBAL.WORLD_HEIGHT / 2 - GLOBAL.RADIUS, GLOBAL.RADIUS, 180));
        //GLOBAL.balls.add(new Ball(GLOBAL.WORLD_WIDTH * 3/4 , GLOBAL.WORLD_HEIGHT / 2 - GLOBAL.RADIUS, GLOBAL.RADIUS, -45));
    }

    @Override//called 60 times a second
    public void render(){
        beginRender();
        addBall();
        updateBallPos();
        renderBalls();
        calculateCollision();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true); 
    }

    @Override
    public void dispose(){
        renderer.dispose(); 
        batch.dispose(); 
    }

    private void beginRender(){
        viewport.apply(); 

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float delta = Gdx.graphics.getDeltaTime();//1/60 

        //draw everything on the screen
        renderer.setProjectionMatrix(viewport.getCamera().combined);
    }

    private void updateBallPos(){
        for(Ball ball: GLOBAL.balls){
            if(ball.y + GLOBAL.RADIUS > GLOBAL.WORLD_HEIGHT){
                ball.multiplyAngle(-1);
                ball.setLastCollided(null);
            }
            if(ball.y - GLOBAL.RADIUS < 0){
                ball.multiplyAngle(-1); 
                ball.setLastCollided(null);
            }
            if(ball.x - GLOBAL.RADIUS<0){
                ball.addAngle(180);
                ball.setLastCollided(null);
            }
            if(ball.x + GLOBAL.RADIUS>GLOBAL.WORLD_WIDTH){
                ball.addAngle(180);
                ball.setLastCollided(null);
            }
            ball.x += GLOBAL.BALL_SPEED * MathUtils.cosDeg(ball.getAngle());
            ball.y += GLOBAL.BALL_SPEED * MathUtils.sinDeg(ball.getAngle());
        }
    }

    private void renderBalls(){
        renderer.begin(ShapeType.Filled);
        for(Ball ball: GLOBAL.balls){
            renderer.setColor(ball.getColor());
            renderer.circle(ball.x, ball.y, ball.radius);
        }
        renderer.end();
    }

    private void addBall(){
        if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
            GLOBAL.balls.add(new Ball(GLOBAL.WORLD_WIDTH / 2 - GLOBAL.RADIUS, GLOBAL.WORLD_HEIGHT / 2 - GLOBAL.RADIUS, GLOBAL.RADIUS, (int)(Math.random()*360)));
        }
    }

    private void calculateCollision(){
        for(int i = 0; i < GLOBAL.balls.size()-1; i++){
            for(int n = i+1; n < GLOBAL.balls.size(); n++){
                if(Intersector.overlaps(GLOBAL.balls.get(i), GLOBAL.balls.get(n)) && !GLOBAL.balls.get(i).getLastCollided().equals(GLOBAL.balls.get(n))){
                    Ball temp1 = GLOBAL.balls.get(i);
                    Ball temp2 = GLOBAL.balls.get(n);
                    
                    float t1Angle = temp1.getAngle();
                    float t2Angle = temp2.getAngle();
                    
                    float xDiff = temp1.x - temp2.x;
                    float yDiff = temp1.y - temp2.y;
                    
                    float contactAngle = 90 + (float)Math.toDegrees(Math.atan(yDiff/xDiff));
                    
                    float v1X = MathUtils.cosDeg(t2Angle - contactAngle) * MathUtils.cosDeg(contactAngle) + MathUtils.sinDeg(t1Angle - contactAngle) * MathUtils.cosDeg(contactAngle + 90);
                    float v1Y = MathUtils.cosDeg(t2Angle - contactAngle) * MathUtils.sinDeg(contactAngle) + MathUtils.sinDeg(t1Angle - contactAngle) * MathUtils.sinDeg(contactAngle + 90);
                    
                    float v2X = MathUtils.cosDeg(t1Angle - contactAngle) * MathUtils.cosDeg(contactAngle) + MathUtils.sinDeg(t2Angle - contactAngle) * MathUtils.cosDeg(contactAngle + 90);
                    float v2Y = MathUtils.cosDeg(t1Angle - contactAngle) * MathUtils.sinDeg(contactAngle) + MathUtils.sinDeg(t2Angle - contactAngle) * MathUtils.sinDeg(contactAngle + 90);
                    
                    float newAngle1 = (float)Math.toDegrees(Math.atan(v1Y/v1X));
                    float newAngle2 = (float)Math.toDegrees(Math.atan(v2Y/v2X));
                    
                    temp1.setAngle(newAngle1);
                    temp2.setAngle(newAngle2);
                    
                    temp1.setLastCollided(temp2);
                    temp2.setLastCollided(temp1);
                }
            }
        }
    }
}