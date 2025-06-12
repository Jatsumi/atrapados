package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.some_example_name.niveles.Nivel1;
import io.github.some_example_name.screen.MenuScreen;

public class Main extends Game {
    private BitmapFont font;
    private int score;                  // <— campo de puntuación

    public BitmapFont getFont() {
        return font;
    }
    public int getScore() {             // <— lee la puntuación actual
        return score;
    }
    public void addScore(int delta) {   // <— suma o resta puntos
        score += delta;
    }
    public void resetScore() {         // <— reinicia para una nueva partida
        score = 0;
    }

    @Override
    public void create() {
        font = new BitmapFont();
        resetScore();                  // <— asegurar que empiece en 0
        setScreen(new Nivel1(this));
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        if (font != null) font.dispose();
    }
}
