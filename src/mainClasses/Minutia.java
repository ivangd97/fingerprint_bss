package mainClasses;

public class Minutia {
    private int type;
    private int minutiaX;
    private int minutiaY;
    private double angle;

    public Minutia(){
        type = 0;
        minutiaX = 0;
        minutiaY = 0;
        angle = 0;
    }

    public Minutia(int tipo, int x, int y){
        type = tipo;
        minutiaX = x;
        minutiaY = y;
        angle = 0;
    }

    public void showMinutia(){
        System.out.println(minutiaX + "," + minutiaY + " de tipo: " + type + " y ángulo: " + angle);
    }

    public void setTipoMinutia(int tipoMinutia) {
        this.type = tipoMinutia;
    }

    public void setMinutiaX(int minutiaX) {
        this.minutiaX = minutiaX;
    }

    public void setMinutiaY(int minutiaY) {
        this.minutiaY = minutiaY;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getType() {
        return type;
    }

    public int getMinutiaX() {
        return minutiaX;
    }

    public int getMinutiaY() {
        return minutiaY;
    }
}
