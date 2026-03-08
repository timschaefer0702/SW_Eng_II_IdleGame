import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class GUIManager {
    private Screen screen;
    private final Game game;
    private TextGraphics tg;

    private String currentUserInput = "";

    public GUIManager(Game game) throws IOException {
        this.game = game;
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.screen.startScreen();
        this.screen.setCursorPosition(null);
        this.tg = screen.newTextGraphics();
    }

    public enum GUIState {
        DEFAULT, MACHINES, STARTSCREEN, ENDSCREEN
    }
    private GUIState currentState = GUIState.STARTSCREEN;

    public void setState(GUIState state) {
        this.currentState = state;
    }
    public GUIState getState() {
        return this.currentState;
    }


    public synchronized void renderUI() {
        try {
            screen.clear();

            switch (currentState) {
                case DEFAULT:
                    renderDashboard();
                    break;
                case MACHINES:
                    renderMachines();
                    break;
                case STARTSCREEN:
                    renderStartScreen();
                    break;
                case ENDSCREEN:
                    renderEndScreen();
                    break;
                default:
                    renderDashboard();
                    break;
            }

            // --- Trenner ---
            tg.setForegroundColor(TextColor.ANSI.DEFAULT);
            tg.putString(2, 15, "--------------------------------------------------------------");

            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(2, 19, "> " + currentUserInput + "_");

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

    public void handleInput() throws IOException {
        KeyStroke keyStroke = screen.pollInput(); // Nicht-blockierend
        if (keyStroke == null) return;

        if (this.currentState == GUIState.ENDSCREEN) {
            this.game.stopGame();
            return;
        }
        if (keyStroke.getKeyType() == KeyType.Character) {
            this.currentUserInput += keyStroke.getCharacter();
        } else if (keyStroke.getKeyType() == KeyType.Backspace) {
            if (this.currentUserInput.length() > 0) {
                this.currentUserInput = this.currentUserInput.substring(0, this.currentUserInput.length() - 1);
            }
        } else if (keyStroke.getKeyType() == KeyType.Enter) {
            this.publishCommand(this.currentUserInput);
            System.out.println("Befehl erkannt");
            this.currentUserInput = "";            // Input-Feld leeren
        }
    }

    public Screen getScreen() {
        return screen;
    }

    public void publishCommand(String command) {
        if(this.game.getRunning())
        {
            this.game.inputHandler.handleInput(command);
        }


    }

    private void renderDashboard()
    {
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(2, 1, "┌────────────────────────────────────────────────────────────┐");
        tg.putString(2, 2, "│  FABRIK-MANAGER 2026 - DASHBOARD                           │");
        tg.putString(2, 3, "└────────────────────────────────────────────────────────────┘");

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(4,4,"Übrige Zeit:"+ game.getRemainingTime());
        tg.putString(4, 5,  "Guthaben:    " + game.getCash() + " €");
        tg.putString(4, 6,  "Produzierte "+ Sock.type+"s:    " + game.seeSockID());
        tg.putString(4, 7,  "Produzierte "+ Lobe.type+"s:    " + game.seeLobeID());
        tg.putString(4, 8,  "Maschinen:   " + game.global_machines.size());
    }

    private void renderMachines()
    {
        tg.setForegroundColor(TextColor.ANSI.MAGENTA);
        tg.putString(2, 1, "┌────────────────────────────────────────────────────────────┐");
        tg.putString(2, 2, "│  FABRIK-MANAGER 2026 - MASCHINENÜBERSICHT                  │");
        tg.putString(2, 3, "└────────────────────────────────────────────────────────────┘");
        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        List<Machine> list = game.global_machines;

        if (list.isEmpty()) {
            tg.putString(4, 6, "Keine Maschinen vorhanden. Kauf dir welche!");
        } else {
            for (int i = 0; i < list.size(); i++) {
                Machine m = list.get(i);
                String info = String.format("%d. %s (%s) LVL: %d", (i+1), m.getName(), m.getType(), m.getLevel());
                tg.putString(4, 5 + i, info);
            }
        }
    }

    private void renderStartScreen()
    {
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(2, 1, "┌────────────────────────────────────────────────────────────┐");
        tg.putString(2, 2, "│  FABRIK-MANAGER 2026 - START                               │");
        tg.putString(2, 3, "└────────────────────────────────────────────────────────────┘");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(4, 4, "      (  )    (  )    (  )    (  )");
        tg.putString(4, 5, "       ||      ||      ||      || ");

        tg.setForegroundColor(TextColor.ANSI.YELLOW); // Fabrikgebäude in Gelb
        tg.putString(4, 6, "   ____||______||______||______||__________");
        tg.putString(4, 7, "  /  ____________________________________  \\");
        tg.putString(4, 8, " |  / [][][][]  [][][][]  [][][][]  [][][][\\ |");
        tg.putString(4, 9, " | |  [][][][]  [][][][]  [][][][]  [][][][] |");
        tg.putString(4, 10, " | |           /--------------\\              | |");
        tg.putString(4, 11," | |          |    [^ _ ^]     |             | |");
        tg.putString(4, 12," | |           \\--------------/              | |");
        tg.putString(4, 13," | |________________________________________| |");
        tg.putString(4, 14," |/__________________________________________\\|");
    }
    private void renderEndScreen()
    {
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(2, 1, "┌────────────────────────────────────────────────────────────┐");
        tg.putString(2, 2, "│  FABRIK-MANAGER 2026 - ENDE                                │");
        tg.putString(2, 3, "└────────────────────────────────────────────────────────────┘");
        tg.putString(4, 6, "OIIAIOIIAIIAIAIAIAIIAIAIAIAIAIAI");
    }

}