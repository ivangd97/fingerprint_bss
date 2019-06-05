package mainClasses;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Menu {

    // Imagen fuente
    private static BufferedImage imgSrc;

    // Imagen de salida
    private static BufferedImage imgOutput;

    // Imagen en blanco y negro
    private static BufferedImage imgBW;

    // Imagen transformada a gris
    private static int[][] auxGrey;

    // Transformación de la imagen gris con el algoritmo del histograma
    private static int[][] auxHistogram;

    // Matriz auxHistogram pasada a blanco y negro
    private static int[][] auxBW;

    // Matriz en blanco y negro pasada por el algoritmo de ZhangSuen
    private static int[][] auxZS;

    // Matriz con minutias
    private static int[][] auxMinutias;

    private static int[][] workMatrix;
    private static int[][] lastworkMatrix;

    // Valor umbral
    private static int limitValue;

    // Lista de minutias
    private static ArrayList <Minutia>CrossingNumbersResult = new ArrayList<Minutia>();

    /**
     * Método de inicialización
     */
    public static void initialize(){
        auxGrey = new int[imgSrc.getWidth()][imgSrc.getHeight()];
        auxHistogram = new int[imgSrc.getWidth()][imgSrc.getHeight()];
        auxBW = new int[imgSrc.getWidth()][imgSrc.getHeight()];
        auxZS = new int[imgSrc.getWidth()][imgSrc.getHeight()];
        auxMinutias = new int[imgSrc.getWidth()][imgSrc.getHeight()];
        imgOutput = new BufferedImage(imgSrc.getWidth(),imgSrc.getHeight(),imgSrc.getType());
        imgBW = new BufferedImage(imgSrc.getWidth(),imgSrc.getHeight(),imgSrc.getType());

        //TODO: utilizar estas dos variables para el boton de deshacer
        workMatrix = new int[imgSrc.getWidth()][imgSrc.getHeight()];
        lastworkMatrix = new int[imgSrc.getWidth()][imgSrc.getHeight()];
        CrossingNumbersResult.clear();
    }

    /**
     * Método que convierte imgSrc a gris
     * imgSrc -> auxGrey
     */
    public static void convertToGrey(){
        for (int x = 0; x < imgSrc.getWidth(); ++x){
            for (int y = 0; y < imgSrc.getHeight(); ++y){
                int rgb = imgSrc.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                int nivelGris = (r + g + b) / 3;
                auxGrey[x][y] = nivelGris;
            }
        }
        convertToRGB(auxGrey);
    }

    /**
     * Método que hace el histograma de la imagen fuente
     */
    public static void doHistogram(){
        int width = imgSrc.getWidth();
        int height = imgSrc.getHeight();
        int tampixel = width * height;
        int[] histograma = new int[256];
        float[] lut = new float[256];
        int sum = 0;
        int i;

        //Calculamos frecuencia relativa de ocurrencia
        // de los distintos niveles de gris en la imagen
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int valor = auxGrey[x][y];
                histograma[valor]++;
            }
        }

        // Construimos la Lookup table LUT
        for ( i = 0; i < 256; ++i ){
            sum += histograma[i];
            lut[i] = sum * 255 / tampixel;
        }
        // Se transforma la imagen utilizando la tabla LUT
        i = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int valor = auxGrey[x][y];
                int valorNuevo = (int) lut[valor];
                auxHistogram[x][y] =  valorNuevo;
                i = i + 1;
            }
        }
        convertToRGB(auxHistogram);
    }

    /**
     * Convierte la imagen fuente a blanco y negro
     * @param limit valor del umbral
     */
    public static void convertToBW(int limit) {
        for (int x = 0; x < imgSrc.getWidth(); ++x) {
            for (int y = 0; y < imgSrc.getHeight(); ++y) {
                int valor = auxGrey[x][y];
                if (valor < limit) {
                    auxBW[x][y] = 1;
                }
                else{
                    auxBW[x][y] = 0;
                }
            }
        }
        convertBlackAndWhiteToRGB(auxBW);
    }

    /**
     * Método que pasa la imagen en blanco y negro por dos filtros binarios
     */
    public static void doFilters(){
        binaryFilter1(auxBW);
        binaryFilter2(auxBW);
        System.out.println(auxBW);
        convertBlackAndWhiteToRGB(auxBW);
    }

    /**
     * Método que pasa una imagen en escala de grises a RGB
     * @param workImage matriz que representa la imagen en escala de grises
     */
    public static void convertToRGB(int[][] workImage){
        for (int x = 0; x < imgSrc.getWidth(); ++x) {
            for (int y = 0; y < imgSrc.getHeight(); ++y) {
                int valor = workImage[x][y];
                int pixelRGB = (255 << 24 | valor << 16 | valor << 8 | valor);
                imgOutput.setRGB(x, y, pixelRGB);
            }
        }
    }

    /**
     * Método que pasa una imagen en blanco y negro a rgb
     * @param workImage matriz que contiene la imagen en blanco y negro
     */
    public static void convertBlackAndWhiteToRGB(int[][] workImage){
        for (int x = 0; x < imgSrc.getWidth(); ++x){
            for (int y = 0; y < imgSrc.getHeight(); ++y){
                int valor = workImage[x][y] * 255;
                int pixelRGB=(255<<24 | valor << 16 | valor << 8 | valor);
                imgBW.setRGB(x, y,pixelRGB);
            }
        }
    }

    /*Almacena el último valor del filtro cuando ha sido convertida la imagen para realizar el resto de algoritmos
     sobre el filtro que hemos creido optimo.
     */
    public static void setLastLimitValue(int newValue){
        limitValue = newValue;
    }

    /**
     * Método que aplica el filtro binario 1
     * @param imageToFilter imagen a filtrar guardada en una matriz
     */
    public static void binaryFilter1(int[][] imageToFilter){
        boolean[][] aux = new boolean[imageToFilter.length][imageToFilter[0].length];
        boolean valueOfPixel;
        for(int i = 0; i < imageToFilter.length; i++){
            for (int j = 0; j < imageToFilter[i].length; j++){
                if(imageToFilter[i][j] == 0)
                    aux[i][j] = false;
                else
                    aux[i][j] = true;
            }
        }

        for(int i = 1; i < aux.length-1; i++) {
            for (int j = 1; j < aux[i].length-1; j++) {
                valueOfPixel = aux[i][j] || aux[i][j-1] && aux[i][j+1] && (aux[i-1][j] || aux[i+1][j]) ||
                        aux[i-1][j] && aux[i+1][j] && (aux[i][j-1] || aux[i][j+1]);
                aux[i][j] = valueOfPixel;
            }
        }

        for(int i = 0; i < aux.length; i++){
            for (int j = 0; j < aux[i].length; j++){
                if(aux[i][j] == false)
                    imageToFilter[i][j] = 0;
                else
                    imageToFilter[i][j] = 1;
            }
        }

    }

    /**
     * Imagen que realiza el filtro binario 2 a una imagen
     * @param imageToFilter imagen guardada en una matriz
     */
    public static void binaryFilter2(int[][] imageToFilter){
        boolean[][] aux = new boolean[imageToFilter.length][imageToFilter[0].length];
        boolean valueOfPixel;
        for(int i = 0; i < imageToFilter.length; i++){
            for (int j = 0; j < imageToFilter[i].length; j++){
                aux[i][j] = (imageToFilter[i][j] != 0);
            }
        }

        for(int i = 1; i < aux.length-1; i++) {
            for (int j = 1; j < aux[i].length-1; j++) {
                valueOfPixel = aux[i][j] &&
                        ((aux[i-1][j-1] || aux[i][j-1] || aux[i-1][j]) &&
                        (aux[i+1][j] || aux[i][j+1] || aux[i+1][j+1]) ||
                        (aux[i][j-1] || aux[i+1][j-1] || aux[i+1][j]) &&
                        (aux[i-1][j] || aux[i-1][j+1] || aux[i][j+1]));
                aux[i][j] = valueOfPixel;
            }
        }

        for(int i = 0; i < aux.length; i++){
            for (int j = 0; j < aux[i].length; j++){
                if(aux[i][j] == false) {
                    imageToFilter[i][j] = 0;
                } else {
                    imageToFilter[i][j] = 1;
                }
            }
        }
    }

    /*
     *Realiza el algoritmo crossing numbers para encontrar las minutias en la huella
     */
    public static void CrossingNumbers(){
        ArrayList <Integer> auxlist = new ArrayList<Integer>();
        int acumulador = 0;
        for(int i = 1; i < auxZS.length-1; i++) {
            for (int j = 1; j < auxZS[i].length - 1; j++) {
                if(auxZS[i][j] == 1){
                    Integer p1 = auxZS[i+1][j];
                    auxlist.add(p1);
                    Integer p2 = auxZS[i+1][j-1];
                    auxlist.add(p2);
                    Integer p3 = auxZS[i][j-1];
                    auxlist.add(p3);
                    Integer p4 = auxZS[i-1][j-1];
                    auxlist.add(p4);
                    Integer p5 = auxZS[i-1][j];
                    auxlist.add(p5);
                    Integer p6 = auxZS[i-1][j+1];
                    auxlist.add(p6);
                    Integer p7 = auxZS[i][j+1];
                    auxlist.add(p7);
                    Integer p8 = auxZS[i+1][j+1];
                    auxlist.add(p8);
                    Integer p9 = auxZS[i+1][j];
                    auxlist.add(p9);
                    for(int k = 0; k < auxlist.size()-1; k++){
                        acumulador = acumulador + Math.abs(auxlist.get(k) - auxlist.get(k+1));
                    }
                    int resultado = acumulador/2;
                    if(resultado == 1 || resultado == 3) {
                        Minutia newMinutia = new Minutia(resultado,i,j);
                        CrossingNumbersResult.add(newMinutia);
                    }
                    auxlist.clear();
                    acumulador = 0;
                }
            }
        }
        System.out.println("Se han encontrado: " + CrossingNumbersResult.size() + " minutias.");
        System.out.println("MINUTIAS ENCONTRADAS (SIN ANGULOS)");
        for(int z = 0; z < CrossingNumbersResult.size(); z++)
            CrossingNumbersResult.get(z).showMinutia();
    }

    //Aplica el algoritmo de Zhang Suen a la imagen en blanco y negro para adelgazarla
    public static void doZS(){
        for(int i = 0; i < auxBW.length; i++){
            for(int j = 0; j < auxBW[i].length; j++){
                auxZS[i][j] = auxBW[i][j];
            }
        }
        ZhangSuen.thinImage(auxZS);
        convertBlackAndWhiteToRGB(auxZS);
    }

    //Muestra los puntos donde se encuentran las minucias encontradas
    public static void showMinutias(){
        int x, y;
        for(int i = 0; i < auxZS.length; i++){
            for(int j = 0; j < auxZS[i].length; j++){
                auxMinutias[i][j] = auxZS[i][j];
            }
        }

        for(int j = 0; j < CrossingNumbersResult.size(); j++){
            x = CrossingNumbersResult.get(j).getMinutiaX();
            y = CrossingNumbersResult.get(j).getMinutiaY();
            auxMinutias[x][y] = 0xFF0000;
            auxMinutias[x - 1][y] = 0xFF0000;
            auxMinutias[x + 1][y] = 0xFF0000;
            auxMinutias[x][y - 1] = 0xFF0000;
            auxMinutias[x][y + 1] = 0xFF0000;
        }
        convertToRGB(auxMinutias);
    }

    //Calcula el angulo de las minutias encontradas
    public static void doAngles(){
        double angle = 0;
        int antX, antY;
        boolean found;
        for(int i = 0; i < CrossingNumbersResult.size(); i++){
            antX = CrossingNumbersResult.get(i).getMinutiaX();
            antY = CrossingNumbersResult.get(i).getMinutiaY();
            if(CrossingNumbersResult.get(i).getType() == 1){
                for(int j = 0; j < 6; j++){
                    found = false;
                    for(int ix = antX - 1; ix <= antX + 1 && !found; ix++){
                        for(int iy = antY - 1; iy <= antY + 1 && !found; iy++){
                               //AQUI COMPRUEBO QUE IX, IY NO SON IGUALES QUE LAS COORDENADAS INICIALES NI QUE LAS ANTERIORES
                               //GUARDADAS
                               if(auxZS[ix][iy] == 1 && !(ix == CrossingNumbersResult.get(i).getMinutiaX() &&
                                       iy == CrossingNumbersResult.get(i).getMinutiaY()) && !(ix == antX && iy == antY)){
                                antX = ix;
                                antY = iy;
                                found = true;
                            }
                        }
                    }
                }
                angle = Math.toDegrees(Math.atan2((double)(antY - CrossingNumbersResult.get(i).getMinutiaY()),
                        (double)(antX - CrossingNumbersResult.get(i).getMinutiaX())));
                CrossingNumbersResult.get(i).setAngle(angle);
            }
            else{
                int tri = 0;
                angle = 0;
                for(int ix = antX - 1; ix <= antX + 1 && tri < 3; ix++) {
                    for (int iy = antY - 1; iy <= antY + 1 && tri < 3; iy++) {
                        if(auxZS[ix][iy] == 1 && !(ix == CrossingNumbersResult.get(i).getMinutiaX() &&
                                iy == CrossingNumbersResult.get(i).getMinutiaY()) && !(ix == antX && iy == antY)){
                            tri++;
                            angle += doTypeThreeAngle(CrossingNumbersResult.get(i).getMinutiaX(),CrossingNumbersResult.get(i).getMinutiaY(),
                                    ix,iy);
                        }
                    }
                }
                CrossingNumbersResult.get(i).setAngle(angle/3);
            }
        }
        System.out.println("MINUTIAS TIPO 1 CON ANGULOS");
        for(int j = 0; j < CrossingNumbersResult.size(); j++){
            if(CrossingNumbersResult.get(j).getType() == 1)
                CrossingNumbersResult.get(j).showMinutia();
        }

        System.out.println("MINUTIAS TIPO 3 CON ANGULOS");
        for(int j = 0; j < CrossingNumbersResult.size(); j++){
            if(CrossingNumbersResult.get(j).getType() == 3)
                CrossingNumbersResult.get(j).showMinutia();
        }
    }

    public static double doTypeThreeAngle(int x, int y, int secondX, int secondY){
        boolean found;
        for(int j = 0; j < 6; j++){
            found = false;
            for(int ix = secondX - 1; ix <= secondX + 1 && !found; ix++){
                for(int iy = secondY - 1; iy <= secondY + 1 && !found; iy++){
                    if(auxZS[ix][iy] == 1 && !(ix == x && iy == y) && !(ix == secondX && iy == secondY)){
                        secondX = ix;
                        secondY = iy;
                        found = true;
                    }
                }
            }
        }
        return(Math.toDegrees(Math.atan2((double)(secondY - y), (double)(secondX - x))));
    }

    public static BufferedImage getImgSrc() {
        return imgSrc;
    }


    public static void setImgSrc(String imgSrc) {
        try {
            Menu.imgSrc = ImageIO.read(new File(imgSrc));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static BufferedImage getImgBW() {
        return imgBW;
    }

    public static BufferedImage getImgOutput() {
        return imgOutput;
    }


    public static int[][] getAuxZS() {
        return auxZS;
    }
}
