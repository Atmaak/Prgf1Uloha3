package objectdata;

import transforms.Cubic;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.util.stream.IntStream;

public class Curve extends Object3D{
    public Curve() {
        super(
                IntStream.rangeClosed(0, 100)
                        .mapToObj(i -> new Cubic(
                                Cubic.BEZIER,
                                new Point3D(-1,-1,-1),
                                new Point3D(-1, 1, -1),
                                new Point3D(-1, -1, 1),
                                new Point3D(1,1,1)
                        ) .compute(i / 100.0)).toList(),
                IntStream.range(0, 100).flatMap(i ->IntStream.of(i, i + 1)).boxed().toList(),
                new Mat4Identity()
        );
    }
}
