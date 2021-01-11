package pt.upskills.projeto.game;

public class Testes {
    // Just for testing options... not used in the game...
    public static void main(String[] args) {
        nextMoves();
    }

    public static void nextMoves() {
        int x0, y0, xf, yf;
        x0 = 2;
        y0 = 1;
        xf = 5;
        yf = 9;

        double x = x0;
        double y = y0;

        double deltaX = (xf - x0);
        double deltaY = (yf - y0);

        int x_increment = 1;
        int y_increment = 1;

        if (deltaX < 0) {
            x_increment = -1;
        }
        if (deltaY < 0) {
            y_increment = -1;
        }


        double slopeM = deltaY / deltaX;
        double intersectB = yf - slopeM * xf;


        for (int i = 0; i < 8; i++) {
            double next_xa = x + x_increment;                                            // distance by increment x = x + 1 or x -1
            double next_ya = Math.round(next_xa * slopeM + intersectB);
            double dx = Math.abs(Math.pow(next_xa - x, 2) + Math.pow(next_ya - y, 2));   // distance by increment x = x + 1 or x -1

            double next_yb = y + y_increment;                                            // distance by increment y = y + 1 or y - 1
            double next_xb = Math.round((next_yb - intersectB) / slopeM);
            double dy = Math.abs(Math.pow(next_xb - x, 2) + Math.pow(next_yb - y, 2));   // distance by increment y = y + 1 or y - 1

            if (dx < dy) {
                x = next_xa;
                y = next_ya;
            } else {
                x = next_xb;
                y = next_yb;
            }
            //System.out.println(slopeM + "  " + intersectB);
            //System.out.println(dx + " " + dy);
            System.out.println(x + "  " + y);

        }


    }

    public static void nextMovesThief() {
        int x0, y0, xf, yf;
        x0 = 1;
        y0 = 6;
        xf = 5;
        yf = 9;

        double x = x0;
        double y = y0;

        double deltaX = (xf - x0);
        double deltaY = (yf - y0);

        int x_increment = 1;
        int y_increment = 1;

        if (deltaX < 0) {
            x_increment = -1;
        }
        if (deltaY < 0) {
            y_increment = -1;
        }


        for (int i = 0; i < 8; i++) {
            double next_xa = x + x_increment;                                            // distance by increment x = x + 1 or x -1
            double next_ya = y + y_increment;

            x = next_xa;
            y = next_ya;
            System.out.println(x + "  " + y);
        }

        //System.out.println(slopeM + "  " + intersectB);
        //System.out.println(dx + " " + dy);
    }

}
