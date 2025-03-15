package raster;


import transforms.Col;

public class ZBuffer {
    private double[][] depthBuffer;
    private Raster raster;
    public ZBuffer(Raster raster) {
        this.raster = raster;
        this.depthBuffer = new double[raster.getWidth()][raster.getHeight()];
        clearZBuffer();
        for (int x = 0; x < raster.getWidth(); x++) {
            for (int y = 0; y < raster.getHeight(); y++) {
                depthBuffer[x][y] = Double.MAX_VALUE;
            }
        }
    }

    public double[][] getZBuffer() {
        return depthBuffer;
    }

    public void clearZBuffer() {
        for (int x = 0; x < depthBuffer.length; x++) {
            for (int y = 0; y < depthBuffer[x].length; y++) {
                depthBuffer[x][y] = Float.MAX_VALUE;
            }
        }
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        // načtu hiodnotu z depth bufferu
        // provonám načtenou hodnotou s hodnotou Z, která vstupuje do metody
        // vyhodnotím podmínku
        // podle podmínky, buď:
        // a) skončit - nic se nestane
        // b) obarvím, upravím hodnotu v depth bufferu

        if (z < this.depthBuffer[x][y]) {
            this.depthBuffer[x][y] = z;
            raster.setRGB(x, y, color.getRGB());
        }
    }
}