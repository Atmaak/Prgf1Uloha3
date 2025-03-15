package shader;

import structure.Vertex;
import transforms.Col;

public class ShaderInterpolated implements Shader{
    @Override
    public Col getColor(Vertex v) {
        return v.getCol();
    }
}
