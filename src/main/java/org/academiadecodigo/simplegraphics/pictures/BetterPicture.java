package org.academiadecodigo.simplegraphics.pictures;

import org.academiadecodigo.simplegraphics.graphics.Canvas;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Movable;
import org.academiadecodigo.simplegraphics.graphics.Shape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class BetterPicture implements Shape, Movable {
    private BufferedImage image;
    private JLabel label = new JLabel();
    private String source;
    private double x;
    private double y;
    private double xGrow;
    private double yGrow;

    public BetterPicture() {
    }

    public BetterPicture(double width, double height) {
        this.image = new BufferedImage((int) Math.round(width), (int) Math.round(height), 1);
        this.label.setIcon(new ImageIcon(this.image));
        this.label.setText("");
    }

    public BetterPicture(double width, double height, String path) {
        this.x = width;
        this.y = height;
        this.load(path);
    }

    public BetterPicture(int[][] pixels) {
        this.image = new BufferedImage(pixels[0].length, pixels.length, 1);

        for (int w = 0; w < this.image.getWidth(); ++w) {
            for (int h = 0; h < this.image.getHeight(); ++h) {
                int pixel = pixels[h][w];
                if (pixel < 0) {
                    pixel = 0;
                }

                if (pixel > 255) {
                    pixel = 255;
                }

                int rgb = pixel * 65793;
                this.image.setRGB(w, h, rgb);
            }
        }

        this.label.setIcon(new ImageIcon(this.image));
        this.label.setText("");
    }

    public void load(String path) {
        try {
            this.source = path;
            if (path.startsWith("http://")) {
                this.image = ImageIO.read((new URL(path)).openStream());
            } else {
                URL url = getClass().getClassLoader().getResource(path);
                if (url != null) {
                    this.image = ImageIO.read(url.openStream());
                } else {
                    this.image = ImageIO.read(new File(path));
                }
            }

            this.label.setIcon(new ImageIcon(this.image));
            this.label.setText("");
        } catch (Exception e) {
            this.image = null;
            this.label.setIcon((Icon) null);
            e.printStackTrace();
        }

        Canvas.getInstance().repaint();
    }

    public int getX() {
        return (int) Math.round(this.x - this.xGrow);
    }

    public int getY() {
        return (int) Math.round(this.y - this.yGrow);
    }

    public int getMaxX() {
        return this.getX() + this.getWidth();
    }

    public int getMaxY() {
        return this.getY() + this.getHeight();
    }

    public int getWidth() {
        return (int) Math.round((double) (this.image == null ? 0 : this.image.getWidth()) + 2.0D * this.xGrow);
    }

    public int getHeight() {
        return (int) Math.round((double) (this.image == null ? 0 : this.image.getHeight()) + 2.0D * this.yGrow);
    }

    public int pixels() {
        return this.image == null ? 0 : this.image.getWidth() * this.image.getHeight();
    }

    public int[][] getGrayLevels() {
        if (this.image == null) {
            return new int[0][0];
        } else {
            int[][] pixels = new int[this.getHeight()][this.getWidth()];

            for (int w = 0; w < pixels.length; ++w) {
                for (int h = 0; h < pixels[w].length; ++h) {
                    int rgb = this.image.getRGB(h, w);
                    pixels[w][h] = (int) (0.2989D * (double) (rgb >> 16 & 255) + 0.5866D * (double) (rgb >> 8 & 255) + 0.1144D * (double) (rgb & 255));
                }
            }

            return pixels;
        }
    }

    public String toString() {
        return "Picture[x=" + this.getX() + ",y=" + this.getY() + ",width=" + this.getWidth() + ",height=" + this.getHeight() + ",source=" + this.source + "]";
    }

    public Color getColorAt(int pos) {
        if (this.image != null && pos >= 0 && pos < this.pixels()) {
            return this.getColorAt(pos % this.image.getWidth(), pos / this.image.getWidth());
        } else {
            throw new IndexOutOfBoundsException(pos + "");
        }
    }

    public void setColorAt(int pos, Color color) {
        if (this.image != null && pos >= 0 && pos < this.pixels()) {
            this.setColorAt(pos % this.image.getWidth(), pos / this.image.getWidth(), color);
        } else {
            throw new IndexOutOfBoundsException(pos + "");
        }
    }

    public Color getColorAt(int x, int y) {
        if (this.image != null && x >= 0 && x < this.image.getWidth() && y >= 0 && y < this.image.getHeight()) {
            int rgb = this.image.getRGB(x, y) & 16777215;
            return new Color(rgb / 65536, rgb / 256 % 256, rgb % 256);
        } else {
            throw new IndexOutOfBoundsException("(" + x + "," + y + ")");
        }
    }

    public void setColorAt(int x, int y, Color color) {
        if (this.image != null && x >= 0 && x < this.image.getWidth() && y >= 0 && y < this.image.getHeight()) {
            this.image.setRGB(x, y, color.getRed() * 65536 + color.getGreen() * 256 + color.getBlue());
            Canvas.getInstance().repaint();
        } else {
            throw new IndexOutOfBoundsException("(" + x + "," + y + ")");
        }
    }

    public void translate(double x, double y) {
        this.x += x;
        this.y += y;
        Canvas.getInstance().repaint();
    }

    public void grow(double x, double y) {
        this.xGrow += x;
        this.yGrow += y;
        Canvas.getInstance().repaint();
    }

    public void draw() {
        Canvas.getInstance().show(this);
    }

    public void delete() {
        Canvas.getInstance().hide(this);
    }

    public void paintShape(Graphics2D shape) {
        if (this.image != null) {
            Dimension dim = this.label.getPreferredSize();
            if (dim.width > 0 && dim.height > 0) {
                this.label.setBounds(0, 0, dim.width, dim.height);
                shape.translate(this.getX(), this.getY());
                shape.scale(((double) this.image.getWidth() + 2.0D * this.xGrow) / (double) dim.width, ((double) this.image.getHeight() + 2.0D * this.yGrow) / (double) dim.height);
                this.label.paint(shape);
            }
        }

    }
}
