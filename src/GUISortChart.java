import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class GUISortChart {
    private DrawingPanel panel;
    private Cell[][] chart;
    private Cell[][] chartCopy;

    private String iVar;
    private String dVar;
    private int iVarIncre;
    private int dVarIncre;

    private int afterSort;

    private int usedCornerX;
    private int usedCornerY;

    public GUISortChart() {
        panel = new DrawingPanel(1601, 900);
        chart = new Cell[16][30];
        chartCopy = new Cell[16][30];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 30; j++) {
                chart[i][j] = new Cell();
            }
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 30; j++) {
                chartCopy[i][j] = new Cell();
            }
        }

        iVar = "Time (s)";
        dVar = "Distance Traveled (m)";
        iVarIncre = 5;
        dVarIncre = 1;

        afterSort = 0;

        usedCornerX = 0;
        usedCornerY = 0;
    }

    public void drawLayout() {
        Graphics2D g = panel.getGraphics();
        for (int i = 0; i < 17; i++) {
            g.drawLine(100, i * 50, 1600, i * 50);
        }
        for (int i = 0; i < 31; i++) {
            g.drawLine(i * 50 + 100, 0, i * 50 + 100, 800);
        }

        int dStart = -dVarIncre;
        int iStart = -iVarIncre;

        for (int i = 125; i <= 1575; i += 50) {
            g.drawString(String.valueOf(iStart += iVarIncre), i, 815);
        }

        for (int i = 800; i >= 50; i -= 50) {
            g.drawString(String.valueOf(dStart += dVarIncre), 80, i);
        }

        Font myFont = new Font("Courier New", Font.BOLD, 40);
        g.setFont(myFont);

        g.setColor(Color.GREEN);
        g.fillRect(1400, 830, 150, 50);

        g.setColor(Color.GRAY);
        g.fillRect(1146, 821, 208, 68);

        g.fillRect(46, 826, 158, 58);
        g.setColor(Color.BLACK);
        g.fillRect(1150, 825, 200, 60);

        g.fillRect(50, 830, 150, 50);

        g.drawString("CLEAR", 1415, 865);

        g.drawRect(1400, 830, 149, 49);

        g.setColor(Color.WHITE);
        g.drawString("<-back", 55, 867);

        Font myFont2 = new Font("Courier New", Font.BOLD, 50);
        g.setFont(myFont2);

        g.drawString("SORT", 1185, 870);

        g.setColor(Color.BLACK);
        Font vars = new Font("Times New Roman", Font.BOLD, 45);
        g.setFont(vars);
        g.drawString(iVar, 350, 870);

        AffineTransform def = new AffineTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);

        g.setTransform(at);
        g.drawString(dVar, -650, 50);

        g.setTransform(def);
    }

    public void sortChart() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 30; j++) {
                chartCopy[i][j] = chart[i][j].isAlive() ? new Cell(true) : new Cell();
            }
        }
        Graphics g = panel.getGraphics();

        int firstColumn = 0;

        int barCount = 0;

        int max = 0;
        int maxColumn = 0;

        for (int j = 0; j < 30; j++) {
            if (chart[15][j].isAlive()) {
                firstColumn = j;
                break;
            }
        }

        while (firstColumn < 30) {
            for (int j = firstColumn; j < 30; j++) {
                if (chart[15][j].isAlive()) {
                    barCount = 1;
                    for (int c = 14; c >= 0; c--) {
                        if (chart[c][j].isAlive()) {
                            barCount++;
                        }
                    }
                }
                max = Math.max(max, barCount);
            }
            barCount = 0;
            for (int c = firstColumn; c < 30; c++) {
                if (chart[15][c].isAlive()) {
                    barCount = 1;
                    for (int t = 14; t >= 0; t--) {
                        if (chart[t][c].isAlive()) {
                            barCount++;
                        }
                    }
                }
                if (barCount == max) {
                    maxColumn = c;
                    break;
                }
            }
            for (int y = 15; y >= 0; y--) {
                Cell c = chart[y][firstColumn];
                chart[y][firstColumn] = chart[y][maxColumn];
                chart[y][maxColumn] = c;
            }
            firstColumn++;
            barCount = 0;

            max = 0;
            maxColumn = 0;
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 30; j++) {
                if (chart[i][j].isAlive()) {
                    g.setColor(Color.ORANGE);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * 50 + 101, i * 50 + 1, 49, 49);
            }
        }
        afterSort = 1;
    }

    public void clear() {
        Graphics g = panel.getGraphics();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 30; j++) {
                if ((chart[i][j].isAlive())) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * 50 + 101, i * 50 + 1, 49, 49);
                    chart[i][j].setAlive(false);
                }
            }
        }
        afterSort = 0;
    }

    public void back() {
        Graphics g = panel.getGraphics();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 30; j++) {
                if (chartCopy[i][j].isAlive()) {
                    g.setColor(Color.ORANGE);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * 50 + 101, i * 50 + 1, 49, 49);
            }
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 30; j++) {
                chart[i][j] = chartCopy[i][j].isAlive() ? new Cell(true) : new Cell();
            }
        }
        afterSort = 0;
    }

    public void processClick(DrawingPanel panel, int x, int y) {

        int cornerX = x - (x % 50);
        int cornerY = y - (y % 50);

        Graphics g = panel.getGraphics();

        if (afterSort == 0 && x < 1350 && x > 1149 && y > 824 && y < 885) {
            sortChart();
        } else if (x < 1550 && x > 1399 && y > 829 && y < 880) {
            clear();
        } else if (x < 200 && x > 49 && y > 829 && y < 880) {
            back();
        } else if (afterSort == 0 && x > 99 && x < 1600 && y < 800 &&
                chart[cornerY / 50][(cornerX - 100) / 50].isAlive()) {
            g.setColor(Color.WHITE);
            for (int i = 0; i <= cornerY; i += 50) {
                g.fillRect(cornerX + 1, i + 1, 49, 49);
                chart[i / 50][(cornerX - 100) / 50].setAlive(false);
                chartCopy[i / 50][(cornerX - 100) / 50].setAlive(false);
            }
        } else if (afterSort == 0 && x > 99 && x < 1600 && y < 800) {
            g.setColor(Color.ORANGE);
            for (int i = 750; i >= cornerY; i -= 50) {
                g.fillRect(cornerX + 1, i + 1, 49, 49);
                chart[i / 50][(cornerX - 100) / 50].setAlive(true);
                chartCopy[i / 50][(cornerX - 100) / 50].setAlive(true);
            }
        }
    }

    public void processDrag(DrawingPanel panel, int x, int y) {
        int cornerX = x - (x % 50);
        int cornerY = y - (y % 50);

        Graphics g = panel.getGraphics();

        if (x < this.usedCornerX || x > this.usedCornerX + 49 || y < this.usedCornerY || y > this.usedCornerY + 49) {
            if (afterSort == 0 && x > 99 && x < 1600 && y < 800 && y > -1 &&
                    chart[cornerY / 50][(cornerX - 100) / 50].isAlive()) {
                g.setColor(Color.WHITE);
                for (int i = 0; i <= cornerY; i += 50) {
                    g.fillRect(cornerX + 1, i + 1, 49, 49);
                    chart[i / 50][(cornerX - 100) / 50].setAlive(false);
                    chartCopy[i / 50][(cornerX - 100) / 50].setAlive(false);
                }
                this.usedCornerX = cornerX;
                this.usedCornerY = cornerY;
            } else if (afterSort == 0 && x > 99 && x < 1600 && y < 800 && y > -1) {
                g.setColor(Color.ORANGE);
                for (int i = 750; i >= cornerY; i -= 50) {
                    g.fillRect(cornerX + 1, i + 1, 49, 49);
                    chart[i / 50][(cornerX - 100) / 50].setAlive(true);
                    chartCopy[i / 50][(cornerX - 100) / 50].setAlive(true);
                }
                this.usedCornerX = cornerX;
                this.usedCornerY = cornerY;
            }
        }
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException notInt) {
            return false;
        }
    }

    public void run() {
        iVar = JOptionPane.showInputDialog(null, "Welcome to Chart Sorter!\nWhat is your independent variable?\n" +
                "ex. 'Time (s)'");
        while (isInt(iVar)) {
            iVar = JOptionPane.showInputDialog(null, "Please only enter a String for variable names.\nex. 'Time (s)'");
        }
        String iVarIncre = JOptionPane.showInputDialog(null, "How big are the increments?\nex. '5'");
        while (!isInt(iVarIncre)) {
            iVarIncre = JOptionPane.showInputDialog(null, "Please enter only integers as increments.\nex. '5'");
        }
        this.iVarIncre = Integer.parseInt(iVarIncre);

        dVar = JOptionPane.showInputDialog(null, "What is your dependant variable?\nex. 'Distance Traveled (m)'");
        while (isInt(dVar)) {
            dVar = JOptionPane.showInputDialog(null, "Please only enter a String for variable names.\n" +
                    "ex. 'Distance Traveled (m)'");
        }
        String dVarIncre = JOptionPane.showInputDialog(null, "How big are the increments?\nex. '10'");
        while (!isInt(dVarIncre)) {
            dVarIncre = JOptionPane.showInputDialog(null, "Please enter only integers as increments.\nex. '10'");
        }
        this.dVarIncre = Integer.parseInt(dVarIncre);

        drawLayout();

        panel.onMouseClick((x, y) -> processClick(panel, x, y));
        panel.onMouseDrag((x, y) -> processDrag(panel, x, y));
    }

    public static void main(String[] args) {
        GUISortChart app = new GUISortChart();
        app.run();
    }
}

