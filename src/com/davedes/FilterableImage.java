package com.davedes;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.Log;

public class FilterableImage extends Image {
    
    protected int filter = Image.FILTER_LINEAR;
    
    /** 
     * Loads the image with the given filtering. If getGraphics() is used,
     * NEAREST_NEIGHBOUR filtering will be lost.
     */
    public FilterableImage(String ref, int filter) throws SlickException {
        super(ref, false, filter);
        this.filter = filter;
    }
    
    public FilterableImage(int width, int height, int filter) throws SlickException {
        super(width, height);
        this.filter = filter;
    }
    
    public void setFilter(int f) {
        this.filter = f;
    }
    
    /** 
     * Returns the OpenGL filter (GL_LINEAR or GL_NEAREST).
     * @return the filter, as an OpenGL constant
     */
    public int getFilter() {
        return getFilterType() == FILTER_LINEAR ? SGL.GL_LINEAR : SGL.GL_NEAREST;
    }
    
    /**
     * Returns the filter type as one of FILTER_NEAREST or FILTER_LINEAR.
     * @return the filter type, as a Slick constant
     */
    public int getFilterType() {
        return filter;
    }
    
    /**
     * Filters the image at it's current texture state. If you are using
     * a ref (i.e. image loaded from a resource) and you do not need to
     * filter any changes made through getGraphics, you should be using
     * updateFilterFromSource() which will cache the image.
     * 
     * @throws SlickException if there was a problem loading the texture
     */
    public void updateFilter() throws SlickException {
        int glFilter = getFilter();
        try {
            int w = getWidth();
            int h = getHeight();
            ImageBuffer buf = new ImageBuffer(w, h);
            //not the most efficient, but easy...
            for (int x=0; x<w; x++) {
                for (int y=0; y<h; y++) {
                    Color col = getColor(x, y);
                    buf.setRGBA(x, y, col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
                }
            }
            //Experiment with this; destroying the old texture seems
            //logical but it may cause problems with updateFilterFromSource
            //texture.release();
            //GraphicsFactory.releaseGraphicsForImage(this);
            
            texture = InternalTextureLoader.get().getTexture(buf, glFilter);
        } catch (IOException e) {
            Log.error(e);
            throw new SlickException("Failed to load the new texture", e);
        }
    }
    
    /** 
     * Filters the source image. This method should be used when the image
     * has a ref defined and has no changes made through getGraphics().
     * 
     * @param transparent the color to treat as transparent when loading the
     *      image from source
     * @throws SlickException if there was a problem loading the texture
     */
    public void updateFilterFromSource(Color transparent) throws SlickException {
        super.setFilter(getFilterType());
    }
}