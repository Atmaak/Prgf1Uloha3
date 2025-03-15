package structure;

import transforms.*;

public class Vertex implements Vectorizable<Vertex> {
    private Vec3D vec3D = new Vec3D();
    private final transforms.Point3D position;
    private final Col col;
    private final Vec2D uv;

    public Vertex(transforms.Point3D position, Col col, Vec2D uv) {
        this.position = position;
        this.col = col;
        this.uv = uv;
    }

    public Vertex(transforms.Point3D position, Col col) {
        this.position = position;
        this.col = col;
        this.uv = new Vec2D();
    }

    public transforms.Point3D getPosition() {
        return position;
    }

    public Col getCol() {
        return col;
    }

    @Override
    public Vertex mul(double k) {
        return new Vertex(position.mul(k), col.mul(k), uv.mul(k));
    }

    public transforms.Point3D mul(Mat4 mat) {
        return position.mul(mat);
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(position.add(v.getPosition()), col.add(v.getCol()), uv.add(v.getUv()));
    }

    public Vec2D getUv() {
        return uv;
    }

    public Vec3D getVec3D() {
        return new Vec3D(vec3D);
    }

    public void setVec3D(Vec3D vec3D) {
        this.vec3D = new Vec3D(vec3D);
    }

    public Vertex dehomog() {
        return mul(1 / getPosition().getW());
    }
}

