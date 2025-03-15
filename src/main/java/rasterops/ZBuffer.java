package rasterops;


import raster.Raster;

public class ZBuffer {
    private double[][] depthBuffer;

    public ZBuffer(Raster raster) {
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
}