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

                    if (numTransitions(r, c, image) != 1 && numTransitions(r, c, image) != 2)
                        continue;

                    if (!atLeastOneIsWhite(r, c, firstStep ? 0 : 1, image))
                        continue;

                        if(numTransitions(r, c, image) == 2){
                            if(!doExtraComprobations(r, c, image, firstStep ? 0 : 1))
                                continue;
                        }

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

    static boolean doExtraComprobations(int r, int c, int [][] image, int step){
        if(step == 0){
            if(((image[r+1][c] * image[r][c+1]) == 1 && image[r-1][c-1] == 0) ||
                    ((image[r+1][c] * image[r][c-1]) ==1 && (neg(image[r+1][c-1]) * neg(image[r-1][c+1]) * neg(image[r-1][c])) == 1))
                return true;
            else
                return false;
        }
        else{
            if(((image[r][c-1] * image[r-1][c]) == 1 && image[r+1][c+1] == 0) ||
                    ((image[r][c+1] * image[r-1][c]) == 1 && (neg(image[r+1][c-1]) * neg(image[r+1][c]) * neg(image[r-1][c])) == 1))
                return true;
            else
                return false;
        }
    }

    static int neg(int num){
        if(num == 1)
            return 0;
        else
            return 1;
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
