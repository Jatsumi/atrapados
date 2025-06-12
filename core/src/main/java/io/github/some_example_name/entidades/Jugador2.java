package io.github.some_example_name.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Jugador2 {
    private Texture textura;
    private float x, y;
    private float velocidad;
    private static final float ANCHO = 32;
    private static final float ALTO = 58;
    private float pantallaAncho, pantallaAlto;
    private Rectangle hitbox;
    private float prevX, prevY;

    public Jugador2(float pantallaAncho, float pantallaAlto) {
        this.textura = new Texture("cr7.png");
        this.pantallaAncho = pantallaAncho;
        this.pantallaAlto = pantallaAlto;
        this.x = 0;
        this.y = (pantallaAlto - ALTO)/2f;
        this.velocidad = 200;
        this.hitbox = new Rectangle(x, y, ANCHO, ALTO);
    }

    public void actualizar(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= velocidad * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += velocidad * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += velocidad * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= velocidad * delta;
        }

        x = Math.max(0, Math.min(pantallaAncho - ANCHO, x));
        y = Math.max(0, Math.min(pantallaAlto - ALTO, y));

        hitbox.setPosition(x, y);
    }

    public void guardarPosicionAnterior(){
        prevX = x;
        prevY = y;
    }
    public void retrocederPosicionAnterior(){
        x = prevX;
        y = prevY;
        hitbox.setPosition(x,y);
    }

    public void renderizar(SpriteBatch batch) {
        batch.draw(textura, x, y, ANCHO, ALTO);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void dispose() {
        textura.dispose();
    }
}
