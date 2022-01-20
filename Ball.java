import com.badlogic.gdx.math.Circle; 
import java.util.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
class Ball extends Circle{
    private float angle;
    private Color color;
    private Ball lastCollided;
    public Ball(){
        super();
        angle = 0;
        color = GLOBAL.colors[(int)(Math.random()*16)];
    }
    
    public Ball(Circle circle, float angle){
        super(circle);
        this.angle = angle;
        color = GLOBAL.colors[(int)(Math.random()*16)];
    }
    
    public Ball(float x, float y, float radius, float angle){
        super(x,y,radius);
        this.angle = angle;
        color = GLOBAL.colors[(int)(Math.random()*16)];
        lastCollided = GLOBAL.no;
    }
    
    public Ball(Vector2 position, float radius, float angle){
        super(position, radius);
        this.angle = angle;
        color = GLOBAL.colors[(int)(Math.random()*16)];
    }
    
    public Ball(Vector2 center, Vector2 edge, float angle){
        super(center, edge);
        this.angle = angle;
        color = GLOBAL.colors[(int)(Math.random()*16)];
    }
    
    public float getAngle(){
        return angle;
    }
    
    public void setAngle(float n){
        angle = n;
    }
    
    public void addAngle(float n){
        angle += n;
    }
    
    public void multiplyAngle(float n){
        angle *= n;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setLastCollided(Ball b){
        lastCollided = b;
    }
    
    public Ball getLastCollided(){
        return lastCollided;
    }
}