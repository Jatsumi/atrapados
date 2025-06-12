package io.github.some_example_name.niveles;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.entidades.BolaEnemiga;
import io.github.some_example_name.entidades.Jugador;
import io.github.some_example_name.Main;


public class Nivel2 implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture fondo;
    private Array<BolaEnemiga> bolas;
    private Jugador jugador;
    private float anchoPantalla, altoPantalla;
    private boolean jugadorMuerto;
    private boolean mostrandoMensaje;
    private boolean juegoPausado;


    public Nivel2(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        juegoPausado = false;
        batch = new SpriteBatch();
        fondo = new Texture("cancha.png");
        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();
        jugador = new Jugador(anchoPantalla, altoPantalla);
        jugadorMuerto = false;
        bolas = new Array<>();
        for (int i = 0; i < 5; i++) {
            BolaEnemiga bola = new BolaEnemiga(anchoPantalla, altoPantalla);
            bola.setVelocidadExtra(1.45f);
            bolas.add(bola);
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!juegoPausado) {
            jugador.actualizar(delta);
            for (BolaEnemiga bola : bolas) {
                bola.actualizar(delta);
                if (jugador.getHitbox().overlaps(bola.getHitbox())) {
                    jugadorMuerto = true;
                    juegoPausado = true;
                }
            }

            if ((jugador.getHitbox().x > anchoPantalla - 40) && ((jugador.getHitbox().y > (altoPantalla/2)-50 && jugador.getHitbox().y < (altoPantalla/2)+20))) {
                mostrandoMensaje = true;
                juegoPausado = true;
            }
        }

        batch.begin();

        batch.draw(fondo, 0, 0, anchoPantalla, altoPantalla);
        for (BolaEnemiga bola : bolas) {
            bola.renderizar(batch);
        }

        jugador.renderizar(batch);

        if (mostrandoMensaje) {
            game.getFont().setColor(Color.BLACK);
            game.getFont().draw(batch,
                "Pasaste al nivel 3! Presiona ENTER",
                anchoPantalla/2 - 100, altoPantalla/2
            );
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.addScore(100);                         // +100 al pasar de nivel
                game.setScreen(new Nivel2(game));
            }
        }

        if (jugadorMuerto) {
            game.getFont().setColor(Color.BLACK);
            game.getFont().draw(batch,
                "Perdiste, regresa al nivel 1! Presiona ENTER",
                anchoPantalla/2 - 150, altoPantalla/2 + 25
            );
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.addScore(-50);                         // −50 al morir
                game.setScreen(new Nivel1(game));
            }
        }

        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch,
            "Score: " + game.getScore(),
            10, altoPantalla - 10
        );


        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        fondo.dispose();
        for (BolaEnemiga bola : bolas) bola.dispose();
        jugador.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
