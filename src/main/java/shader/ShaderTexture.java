package shader;

import structure.Vertex;
import transforms.Col;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShaderTexture implements Shader{
    
    BufferedImage image;

    public ShaderTexture() {
        try {
            image = ImageIO.read(new File("res/image.jpg"));
        } catch (IOException e) {
            System.out.println("Problém s načtení obrázku.");
        }
    }

    @Override
    public Col getColor(Vertex v) {
        int x = (int) (v.getUv().getX() * image.getWidth());
        int y = (int) (v.getUv().getY() * image.getHeight());

        // Zajistěte, že x a y jsou v rozmezí textury
        x = Math.max(0, Math.min(x, image.getWidth() - 1));
        y = Math.max(0, Math.min(y, image.getHeight() - 1));

        // Získání barvy z textury
        return new Col(image.getRGB(x, y));
    }
}
