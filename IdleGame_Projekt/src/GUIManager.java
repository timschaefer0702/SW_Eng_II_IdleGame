import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class GUIManager {
    private Screen screen;
    private final Game game;
    private TextGraphics tg;

    public GUIManager(Game game) throws IOException {
        this.game = game;
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.screen.startScreen();
        this.screen.setCursorPosition(null);
        this.tg = screen.newTextGraphics();
    }


    public synchronized void renderUI() {
        try {
            screen.clear();


            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(2, 1, "┌────────────────────────────────────────────────────────────┐");
            tg.putString(2, 2, "│  FABRIK-MANAGER 2026 - STATUSÜBERSICHT                     │");
            tg.putString(2, 3, "└────────────────────────────────────────────────────────────┘");

            // --- Stats Bereich ---
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(4,4,"Übrige Zeit:"+ game.getRemainingTime());
            tg.putString(4, 5,  "Guthaben:    " + game.getCash() + " €");
            tg.putString(4, 6,  "Socken-ID:   " + game.seeSockID());
            tg.putString(4, 7,  "Lobe-ID:     " + game.seeLobeID());
            tg.putString(4, 8,  "Maschinen:   " + game.global_machines.size());

            // --- Trenner ---
            tg.setForegroundColor(TextColor.ANSI.DEFAULT);
            tg.putString(2, 10, "--------------------------------------------------------------");

            // --- Input Bereich (Statisch) ---
            tg.putString(2, 12, "Letzter Befehl: [Warte auf Eingabe...]");
            tg.putString(2, 14, "> ");

            screen.refresh();

        } catch (IOException e) {
            System.err.println("Fehler beim GUI-Rendering: " + e.getMessage());
        }
    }


    public void closeUI() {
        try {
            if (screen != null) {
                screen.stopScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screen getScreen() {
        return screen;
    }


}