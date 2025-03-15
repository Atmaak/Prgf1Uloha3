package shader;

import structure.Vertex;
import transforms.Col;

public interface Shader {
    Col getColor(Vertex v);
}
