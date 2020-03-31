package spring.movieclinic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Category {

        private String id;
        private String name;
        private String description;
        private BufferedImage image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BufferedImage getImage() {
            return image;
        }

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        //    public void setImage(BufferedImage image) throws IOException {
//        this.image = ImageIO.read(new File(name + ".png"));
//    }
    }

