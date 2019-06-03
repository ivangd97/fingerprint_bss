package mainClasses;
import java.awt.Point;
import java.util.*;

public class ZhangSuen {

    final static int[][] nbrs = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1},
            {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};

    final static int[][][] nbrGroups = {{{0, 2, 4}, {2, 4, 6}}, {{0, 2, 6},
            {0, 4, 6}}};

    static List<Point> toWhite = new ArrayList<>();

    public static int [][] thinImage(int [][] image) {
        boolean firstStep = false;
        boolean hasChanged;

        do {
            hasChanged = false;
            firstStep = !firstStep;

            for (int r = 1; r < image.length - 1; r++) {
                for (int c = 1; c < image[0].length - 1; c++) {

                    if (image[r][c] != 1)
                        continue;

                    int nn = numNeighbors(r, c, image);
                    if (nn < 2 || nn > 6)
                        continue;

                    if (numTransitions(r, c, image) != 1)
                        continue;

                    if (!atLeastOneIsWhite(r, c, firstStep ? 0 : 1, image))
                        continue;

                    toWhite.add(new Point(c, r));
                    hasChanged = true;
                }
            }

            for (Point p : toWhite)
                image[p.y][p.x] = 0;
            toWhite.clear();

        } while (firstStep || hasChanged);

        return image;
    }

    static int numNeighbors(int r, int c, int [][] image) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (image[r + nbrs[i][1]][c + nbrs[i][0]] == 1)
                count++;
        return count;
    }

    static int numTransitions(int r, int c, int [][] image) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (image[r + nbrs[i][1]][c + nbrs[i][0]] == 0) {
                if (image[r + nbrs[i + 1][1]][c + nbrs[i + 1][0]] == 1)
                    count++;
            }
        return count;
    }

    static boolean atLeastOneIsWhite(int r, int c, int step, int [][] image) {
        int count = 0;
        int[][] group = nbrGroups[step];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < group[i].length; j++) {
                int[] nbr = nbrs[group[i][j]];
                if (image[r + nbr[1]][c + nbr[0]] == 0) {
                    count++;
                    break;
                }
            }
        return count > 1;
    }
}
