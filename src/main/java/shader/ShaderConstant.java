package shader;

import structure.Vertex;
import transforms.Col;

public class ShaderConstant implements Shader{
    
    private Col col;
    public ShaderConstant() {}
    
    @Override
    public Col getColor(Vertex v) {
        return col;
    }

    public Col getCol() {
        return col;
    }

    public void setCol(Col col) {
        this.col = col;
    }
}
