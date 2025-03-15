//package objectdata;
//import structure.Vertex;
//import transforms.Bicubic;
//import transforms.Col;
//import transforms.Mat4;
//
//
//public class BicubicGrid extends Object3D {
//    private Bicubic bc;
//    private Mat4 gridType;
//
//    public BicubicGrid(Mat4 type, int color) {
//        colors.add(new Col(color));
//        gridType = type;
//
//        Vertex[] points;
//        points = new Vertex[]{
//            new Vertex(1, 1, 0),
//            new Vertex(1, 2, 0),
//            new Vertex(1, 3, 2),
//            new Vertex(1, 4, 0),
//            new Vertex(2, 1, 0),
//            new Vertex(2, 2, 3),
//            new Vertex(2, 3, 0),
//            new Vertex(2, 4, 0),
//            new Vertex(3, 1, 4),
//            new Vertex(3, 2, 0),
//            new Vertex(3, 3, 0),
//            new Vertex(3, 4, 0),
//            new Vertex(4, 1, -5),
//            new Vertex(4, 2, 0),
//            new Vertex(4, 3, -7),
//            new Vertex(4, 4, 0),
//        };
//
//
//        bc = new Bicubic(gridType, points);
//        int counter = 0;
//
//        for (float i = 0; i < 1.0; i += 0.05) {
//            counter++;
//            for (float j = 0; j < 1.0; j += 0.05) { // zhuštění
//                vertexBuffer.add(bc.compute(i, j));
//            }
//        }
//
//        for (int i = 0; i < counter; i++) {
//            for (int j = 0; j < counter; j++) {
//                if (j < counter - 1){
//                    indexBuffer.add((i * counter) + j);
//                    indexBuffer.add((i * counter) + j + 1);
//                }
//                if (i < counter - 1){
//                    indexBuffer.add((i * counter) + j);
//                    indexBuffer.add(((i + 1) * counter) + j);
//                }
//            }
//        }
//    }
//}