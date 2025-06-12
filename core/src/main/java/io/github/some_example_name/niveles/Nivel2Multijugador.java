package io.github.some_example_name.niveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.Main;
import io.github.some_example_name.entidades.BolaEnemiga;
import io.github.some_example_name.entidades.Jugador;
import io.github.some_example_name.entidades.Jugador2;
import io.github.some_example_name.screen.MenuScreen;

public class Nivel2Multijugador implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture fondo;
    private Array<BolaEnemiga> bolas;
    private Jugador jugador;
    private Jugador2 jugador2;
    private float anchoPantalla, altoPantalla;
    private boolean jugador1Muerto;
    private boolean jugador2Muerto;
    private long tiempoMuerteJugador1;
    private long tiempoMuerteJugador2;
    private boolean mostrandoMensaje;
    private boolean juegoPausado;

    private Stage stage;
    private Skin skin;
    private TextButton menuButton;

    public Nivel2Multijugador(Main game) {
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
        jugador2 = new Jugador2(anchoPantalla, altoPantalla);
        jugador2.getHitbox().x = 100;

        jugador1Muerto = false;
        jugador2Muerto = false;
        tiempoMuerteJugador1 = 0;
        tiempoMuerteJugador2 = 0;
        mostrandoMensaje = false;

        bolas = new Array<>();
        for (int i = 0; i < 6; i++) {
            BolaEnemiga bola = new BolaEnemiga(anchoPantalla, altoPantalla);
            bola.setVelocidadExtra(1.5f);
            bolas.add(bola);
        }

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        menuButton = new TextButton("Menu", skin);
        menuButton.setPosition(525, altoPantalla - 70);
        menuButton.setSize(100, 50);
        stage.addActor(menuButton);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        long ahora = TimeUtils.nanoTime();

        if (!juegoPausado) {
            if (!jugador1Muerto) {
                jugador.actualizar(delta);
            } else if (TimeUtils.timeSinceNanos(tiempoMuerteJugador1) > 5_000_000_000L) {
                jugador1Muerto = false;
                jugador.resetearPosicion();
            }

            if (!jugador2Muerto) {
                jugador2.actualizar(delta);
            } else if (TimeUtils.timeSinceNanos(tiempoMuerteJugador2) > 5_000_000_000L) {
                jugador2Muerto = false;
                jugador2.resetearPosicion();
            }

            for (BolaEnemiga bola : bolas) {
                bola.actualizar(delta);

                if (!jugador1Muerto && jugador.getHitbox().overlaps(bola.getHitbox())) {
                    jugador1Muerto = true;
                    tiempoMuerteJugador1 = TimeUtils.nanoTime();
                }

                if (!jugador2Muerto && jugador2.getHitbox().overlaps(bola.getHitbox())) {
                    jugador2Muerto = true;
                    tiempoMuerteJugador2 = TimeUtils.nanoTime();
                }
            }

            boolean jugador1EnMeta = !jugador1Muerto &&
                jugador.getHitbox().x > anchoPantalla - 40 &&
                jugador.getHitbox().y > (altoPantalla / 2) - 50 &&
                jugador.getHitbox().y < (altoPantalla / 2) + 20;

            boolean jugador2EnMeta = !jugador2Muerto &&
                jugador2.getHitbox().x > anchoPantalla - 40 &&
                jugador2.getHitbox().y > (altoPantalla / 2) - 50 &&
                jugador2.getHitbox().y < (altoPantalla / 2) + 20;

            if (jugador1EnMeta && jugador2EnMeta) {
                mostrandoMensaje = true;
                juegoPausado = true;
            }
        }

        batch.begin();
        batch.draw(fondo, 0, 0, anchoPantalla, altoPantalla);

        for (BolaEnemiga bola : bolas) bola.renderizar(batch);
        if (!jugador1Muerto) jugador.renderizar(batch);
        if (!jugador2Muerto) jugador2.renderizar(batch);

        if (mostrandoMensaje) {
            game.getFont().setColor(Color.BLACK);
            game.getFont().draw(batch, "¡Ganaron el Nivel 2! Presionen ENTER", anchoPantalla / 2 - 130, altoPantalla / 2 + 20);
            game.getFont().draw(batch, "¡Has completado el Nivel 2! ¡Te queda poco!", anchoPantalla / 2 - 150, altoPantalla / 2 - 10);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.addScore(200);
                game.setScreen(new MenuScreen(game));
            }
        }

        if (jugador1Muerto && jugador2Muerto &&
            TimeUtils.timeSinceNanos(tiempoMuerteJugador1) > 5_000_000_000L &&
            TimeUtils.timeSinceNanos(tiempoMuerteJugador2) > 5_000_000_000L) {
            game.getFont().setColor(Color.BLACK);
            game.getFont().draw(batch, "¡Perdieron! Presionen ENTER para reiniciar", anchoPantalla / 2 - 150, altoPantalla / 2 + 25);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.addScore(-75);
                game.setScreen(new Nivel2Multijugador(game));
            }
        }

        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Score: " + game.getScore(), 10, altoPantalla - 10);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        fondo.dispose();
        jugador.dispose();
        jugador2.dispose();
        for (BolaEnemiga bola : bolas) bola.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
