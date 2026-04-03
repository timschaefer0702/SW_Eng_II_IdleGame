import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    private Screen screen;
    private final Game game;
    private TextGraphics tg;

    private String currentUserInput = "";
    private final List<String> formerUserInput = new ArrayList<>();
    private int listPointer = -1;
    private String currentCommandReturn = "";

    public void setCommandReturn(String commandReturn) {
        currentCommandReturn = commandReturn;
    }
    public void refreshCommandReturn()
    {
        currentCommandReturn = "";
    }

    public GUIManager(Game game) throws IOException {
        this.game = game;
        Terminal terminal = new DefaultTerminalFactory()
                .setForceTextTerminal(true)
                .setPreferTerminalEmulator(false)
                .createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.screen.startScreen();
        this.screen.setCursorPosition(null);
        this.tg = screen.newTextGraphics();
    }

    public enum GUIState {
        DEFAULT,
        MACHINES,
        SALES,
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
                case SALES:
                    renderSales();
                    break;
                default:
                    renderDashboard();
                    break;
            }
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(2,21,currentCommandReturn);
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
            if(!this.currentUserInput.isEmpty()) {
                this.formerUserInput.add(0,this.currentUserInput);
            }
            if (this.formerUserInput.size() > 50){
                this.formerUserInput.remove(formerUserInput.size() - 1);
            }
            this.refreshCommandReturn();
            this.publishCommand(this.currentUserInput);
            this.currentUserInput = "";
            this.listPointer = -1;
        } else if(keyStroke.getKeyType() == KeyType.ArrowUp)
        {
            if (!formerUserInput.isEmpty() && listPointer < formerUserInput.size() - 1) {
                listPointer++;
                this.currentUserInput = formerUserInput.get(listPointer);
            }
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            if (listPointer > 0) {
                listPointer--;
                this.currentUserInput = formerUserInput.get(listPointer);
            } else if (listPointer == 0) {
                listPointer = -1;
                this.currentUserInput = "";
            }
        }
    }

    public void publishCommand(String command) {
        if(this.game.getRunning())
        {
            this.game.inputHandler.handleInput(command);
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

    private void renderDashboard()
    {
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(2, 0, " __        __        __   __        __   __ ");
        tg.putString(2, 1, "|  \\  /\\  /__` |__| |__) /  \\  /\\  |__) |  \\");
        tg.putString(2, 2, "|__/ /~~\\ .__/ |  | |__) \\__/ /~~\\ |  \\ |__/");

        int y = 5;
        tg.setForegroundColor(TextColor.ANSI.WHITE);

        tg.putString(4, y++, "Übrige Zeit:      " + game.getRemainingTime()); y++;
        tg.putString(4, y++, "Guthaben:         " + game.getCash() + "€"); y++;

        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(4, y++, "LAGERBESTAND (Kapazität " + game.getWarehouse().getCapacity() + " Produkte)");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(4, y++, Sock.type + "s im Lager:   " + game.getWarehouse().getSockStock() + " Auslastung von "+ game.getWarehouse().getSockUtilization()+ "%");
        tg.putString(4, y++, Lobe.type + "s im Lager:   " + game.getWarehouse().getLobeStock() + " Auslastung von "+ game.getWarehouse().getLobeUtilization()+ "%");
        y++;

        tg.putString(4, y++, "Maschinen:          " + game.global_machines.size());
        tg.putString(4, y++, "Mitarbeiter:        " + game.global_salesAgents.size());
        y++;

        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(4, y++, "STATISTIK (Gesamt)");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(4, y++, "Produzierte " + Sock.type + "s:  " + game.seeSockID());
        tg.putString(4, y++, "Produzierte " + Lobe.type + "s:  " + game.seeLobeID());
    }

    private void renderMachines()
    {
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(2, 0, "           __   __               ___     ");
        tg.putString(2, 1, "|\\/|  /\\  /__` /  ` |__| | |\\ | |__  |\\ |");
        tg.putString(2, 2, "|  | /~~\\ .__/ \\__, |  | | | \\| |___ | \\|");
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

    public void renderSales()
    {
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(2, 0, " __             ___  __ ");
        tg.putString(2, 1, "/__`  /\\  |    |__  /__`");
        tg.putString(2, 2, ".__/ /~~\\ |___ |___ .__/");

        tg.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
        List<SalesAgent> list = game.global_salesAgents;

        if (list.isEmpty()) {
            tg.putString(4, 6, "Keine Verkäufer vorhanden. Nutze 'hire'!");
        } else {
            for (int i = 0; i < list.size(); i++) {
                SalesAgent sa = list.get(i);
                // Einfacher Info-String wie bei den Maschinen
                String info = (i + 1) + ". " + sa.getName() +
                        " | LVL: " + sa.getLevel() +
                        " | Fokus: " + sa.getFokus() +
                        " | Sales: " + sa.getNumberofSoldProducts() +
                        " | Cash: " + sa.getGeneratedCash() + "€";

                tg.putString(4, 5 + i, info);
            }
        }


    }

    private void renderEndScreen()
    {
        tg.setForegroundColor(TextColor.ANSI.RED);
        tg.putString(2, 0, " __   __          __       ___  ___       __   ___");
        tg.putString(2, 1, "/__` /  ` |__| | /  ` |__|  |  |__  |\\ | |  \\ |__ ");
        tg.putString(2, 2, ".__/ \\__, |  | | \\__, |  |  |  |___ | \\| |__/ |___");

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
        tg.putString(2, 0, "             ___  ___        ___          ");
        tg.putString(2, 1, "|__| | |    |__  |__   |\\/| |__  |\\ | |  |");
        tg.putString(2, 2, "|  | | |___ |    |___  |  | |___ | \\| \\__/");
        tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

        int y = 3;

        // --- Navigation ---
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(4, y++, "NAVIGATION:");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(6, y++, "dashboard            - Zurück zur Statusübersicht");
        tg.putString(6, y++, "machines             - Liste deiner Maschinen");
        tg.putString(6, y++, "sales                - Liste deiner Verkäufer");
        tg.putString(6, y++, "help                 - Diese Hilfeseite öffnen");

        // --- Maschinen-Management ---
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(4, y++, "MASCHINEN:");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(6, y++, "buy <typ> <name>     - Typ: sockmachine/lobemachine");
        tg.putString(6, y++, "upgrade <typ> <name> - Maschine verbessern");
        tg.putString(6, y++, "sell <name>/all      - Maschine(n) verkaufen");

        // --- Personal-Management ---
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(4, y++, "PERSONAL:");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(6, y++, "hire <name> <fokus>  - Verkäufer einstellen (Fokus: socke/lobe)");
        tg.putString(6, y++, "promote <name>       - Verkäufer befördern (schneller verkaufen)");
        tg.putString(6, y++, "fire <name>/all      - Verkäufer entlassen");
        tg.putString(6, y++, "expand               - Lager erweitern");


        // --- System ---
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(4, y++, "SYSTEM:");
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(6, y++, "start <min>          - Spiel starten");
        tg.putString(6, y++, "finish               - Schicht sofort beenden");
        tg.putString(6, y  , "stop                 - Not-Aus (Beenden ohne Speichern)");
    }
}