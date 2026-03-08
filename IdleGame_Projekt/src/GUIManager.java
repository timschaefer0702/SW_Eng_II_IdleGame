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
        DEFAULT,
        MACHINES,
        HELP,
        STARTSCREEN,
        ENDSCREEN
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
                case HELP:
                    renderHelp();
                    break;
                default:
                    renderDashboard();
                    break;
            }


            tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
            tg.putString(2, 22, "> " + currentUserInput + "_");

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
        KeyStroke keyStroke = screen.pollInput();
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
            this.currentUserInput = "";            // Input-Feld leeren
        }
    }


    public void publishCommand(String command) {
        if(this.game.getRunning())
        {
            this.game.inputHandler.handleInput(command);
        }


    }

    private void renderDashboard()
    {
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(2, 1, "|~\\ _  _|_ |_  _  _ ._ _|");
        tg.putString(2, 2, "|_/(_|_\\| ||_)(_)(_|| (_|");
        tg.putString(2, 3, "──────────────────────────");

        int y = 5;
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(4, y++,  "Übrige Zeit:    "+ game.getRemainingTime());y++;
        tg.putString(4, y++,  "Guthaben:       " + game.getCash() + "€");y++;
        tg.putString(4, y++,  "Produzierte "+ Sock.type+"s:    " + game.seeSockID());y++;
        tg.putString(4, y++,  "Produzierte "+ Lobe.type+"s:    " + game.seeLobeID());y++;
        tg.putString(4, y++,  "Maschinen:      " + game.global_machines.size());y++;
    }

    private void renderMachines()
    {
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(2, 1, "|\\/| _  _ _|_ o._  _._ |o __|_ _ ");
        tg.putString(2, 2, "|  |(_|_\\(_| ||| |}_| |||_\\ | }_");
        tg.putString(2, 3, "──────────────────────────────────");
        tg.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
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
        int y=0;
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(2, y++, "                  __  . .* ,");
        tg.putString(2, y++, "               ~#@#%(\" .,$ @");
        tg.putString(2, y++, "              .\"^ ';\"");
        tg.putString(4, y++, "             ..");
        tg.putString(4, y++, "            ;. :                                   . .");
        tg.putString(4, y++, "           ;==:                     ,,   ,.@#(&*.;'");
        tg.putString(4, y++, "           ;. :                   .;#$% & ^^&");
        tg.putString(4, y++, "           ;==:                   &  ......");
        tg.putString(4, y++, "           ;. :                   ,,;      :");
        tg.putString(4, y++,"           ;==:  ._______.       ;  ;      :");
        tg.putString(4, y++,"          ;. :  ;    ###:__.    ;  ;      :");
        tg.putString(4, y++,"_________.'  `._;       :  :__.' .'        `.______");
        tg.putString(4, y++,"start <min> für Spielstart oder help für Commands eingeben.");
        tg.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
        tg.putString(4, y++, " _______    _           _ _");
        tg.putString(4, y++, "(_______)  | |         (_) |   ");
        tg.putString(4, y++, " _____ ____| | _   ____ _| |  _ ____   ____ ____   ____  ____  ____  ____ ");
        tg.putString(4, y++, "|  ___) _  | || \\ / ___) | | / )    \\ / _  |  _ \\ / _  |/ _  |/ _  )/ ___)");
        tg.putString(4, y++,"| |  ( ( | | |_) ) |   | | |< (| | | ( ( | | | | ( ( | ( ( | ( (/ /| |  ");
        tg.putString(4, y++,"|_|   \\_||_|____/|_|   |_|_| \\_)_|_|_|\\_||_|_| |_|\\_||_|\\_|| |\\____)_|  ");
        tg.putString(4, y++,"                                                       (_____|         ");
    }
    private void renderEndScreen()
    {
        tg.setForegroundColor(TextColor.ANSI.RED);
        tg.putString(2, 1, "(~ _|_ o _|_ _|_ _._  _| _      [~._(~| _|_ ._ o _");
        tg.putString(2, 2, "_)(_| ||(_| | | }_| |(_|}_  ~~  [_|  _|}_|_)| ||_\\");
        tg.putString(2, 3, "───────────────────────────────────────────────────");

        int y=5;
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(4, y++, "Status:       ZEIT ABGELAUFEN");y++;
        tg.putString(4, y++, "Endguthaben:    " + game.getCash() + " €");y++;
        tg.putString(4, y++, "Gesamt " + Sock.type + "s:   " + game.seeSockID());y++;
        tg.putString(4, y++, "Gesamt " + Lobe.type + "s:   " + game.seeLobeID());y++;
        tg.putString(4, y++, "Maschinenpark:  " + game.global_machines.size());y++;

        tg.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        tg.putString(2, y++, "--------------------------------------");y++;y++;
        tg.putString(4, y, "DRÜCKE EINE TASTE ZUM BEENDEN DES SPIELS");
    }

    private void renderHelp()
    {
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(2, 1, "|_|o| |~ _._ _  _._ o o");
        tg.putString(2, 2, "| |||~|~}_| | |}_| ||_|");
        tg.putString(2, 3, "───────────────────────");
        tg.putString(4, 4, "Gültige Befehle");
        tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

        int y = 5;

        // --- Navigation ---
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(4, y++, "NAVIGATION:");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(6, y++, "dashboard          - Zurück zur Statusübersicht");
        tg.putString(6, y++, "machines           - Liste aller deiner Maschinen anzeigen");
        tg.putString(6, y++, "help               - Diese Hilfeseite öffnen");
        y++;

        // --- Management ---
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(4, y++, "MANAGEMENT:");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(6, y++, "buy <typ> <name>      - Maschine kaufen (Typ: sockmachine/lobemachine)");
        tg.putString(6, y++, "upgrade <typ> <name>  - Maschine verbessern (Level erhöhen)");
        tg.putString(6, y++, "sell <name>           - Eine bestimmte Maschine verkaufen");
        tg.putString(6, y++, "sell all              - Deinen gesamten Maschinenpark auflösen");
        y++;

        // --- System ---
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(4, y++, "SYSTEM:");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(6, y++, "start <min>        - Spiel starten");
        tg.putString(6, y  , "stop               - Not Aus - Spiel beenden ohne Ergebnis");




    }


}