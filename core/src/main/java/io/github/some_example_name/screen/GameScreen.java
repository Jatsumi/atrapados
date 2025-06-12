package io.github.some_example_name.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.entidades.BolaEnemiga;
import io.github.some_example_name.entidades.Jugador;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture fondo;
    private BitmapFont font;

    private Array<BolaEnemiga> bolas;
    private Jugador jugador;
    private Rectangle meta;

    private float anchoPantalla, altoPantalla;
    private boolean jugadorMuerto = false;
    private boolean jugadorGano = false;
    private float tiempoReinicio = 2f;

    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        fondo = new Texture("cancha.png");
        font = new BitmapFont(); // Usa fuente predeterminada

        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();

        reiniciarJuego();
    }

    private void reiniciarJuego() {
        jugador = new Jugador(anchoPantalla, altoPantalla);
        bolas = new Array<>();
        for (int i = 0; i < 5; i++) {
            bolas.add(new BolaEnemiga(anchoPantalla, altoPantalla));
        }
        meta = new Rectangle(anchoPantalla / 2f - 50, altoPantalla - 40, 100, 30);
        jugadorMuerto = false;
        jugadorGano = false;
        tiempoReinicio = 2f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Fondo negro por si la imagen no cubre todo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!jugadorMuerto && !jugadorGano) {
            jugador.actualizar(delta);
            for (BolaEnemiga bola : bolas) {
                bola.actualizar(delta);
                if (jugador.getHitbox().overlaps(bola.getHitbox())) {
                    jugadorMuerto = true;
                }
            }

            if (jugador.getHitbox().overlaps(meta)) {
                jugadorGano = true;
            }
        } else {
            tiempoReinicio -= delta;
            if (tiempoReinicio <= 0) {
                reiniciarJuego();
            }
        }

        batch.begin();

        // Dibuja el fondo
        batch.draw(fondo, 0, 0, anchoPantalla, altoPantalla);

        for (BolaEnemiga bola : bolas) {
            bola.renderizar(batch);
        }

        jugador.renderizar(batch);

        if (jugadorMuerto) {
            font.draw(batch, "¡Perdiste!", anchoPantalla / 2f - 40, altoPantalla / 2f);
        } else if (jugadorGano) {
            font.draw(batch, "¡Ganaste!", anchoPantalla / 2f - 40, altoPantalla / 2f);
        }

        batch.end();

        // Dibuja la meta como rectángulo verde
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(meta.x, meta.y, meta.width, meta.height);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        for (BolaEnemiga bola : bolas) bola.dispose();
        jugador.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        fondo.dispose();
        font.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
