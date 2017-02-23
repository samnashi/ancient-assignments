

/*
 * (C) 2004 - Geotechnical Software Services
 * 
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program; if not, write to the Free 
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA  02111-1307, USA.
 */
import java.awt.*;
import java.util.*;

import javax.swing.*;

import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;

public class BarChart extends GObject
  {
    private int         x_, y_, width_, height_;
    private Collection  bars_;
    
    
    BarChart (int x, int y, int width, int height)
    {
        x_      = x;
        y_      = y;
        width_  = width;
        height_ = height;
        
        bars_ = new ArrayList();
    }


    
    void addBar (String label, int value, Color color){
        bars_.add (new Bar (label, value, color));
    }
    
    public void draw()
    {
        final int BAR_WIDTH = 25;
        final int SPACING   = 15;
        final int DEPTH     = 15;
        
        removeSegments();
        
        int x0 = x_ + 10;
        
        double angle0 = 0.0;
        for (Iterator i = bars_.iterator(); i.hasNext(); ) {
        Bar bar = (Bar) i.next();
        
        int y0 = y_ - bar.value;
        
        GSegment main = new GSegment();
        addSegment (main);
        GStyle style = new GStyle();
        style.setForegroundColor (bar.color);
        style.setBackgroundColor (bar.color);        
        main.setStyle (style);
        main.setGeometry (Geometry.createRectangle (x0, y0,
        BAR_WIDTH, bar.value));
        
        GSegment label = new GSegment();
        addSegment (label);
        label.setGeometry (x0 + BAR_WIDTH / 2, y_ + 10);
        GText text = new GText (bar.label);
        label.setText (text);
        
        GSegment top = new GSegment();
        addSegment (top);
        style = new GStyle();
        style.setForegroundColor (bar.color.brighter());
        style.setBackgroundColor (bar.color.brighter());        
        top.setStyle (style);
        int topXy[] = {x0, y0,
        x0 + BAR_WIDTH, y0,
        x0 + BAR_WIDTH + DEPTH, y0 - DEPTH,
        x0 + DEPTH, y0 - DEPTH,
        x0, y0};
        top.setGeometry (topXy);
        
        GSegment side = new GSegment();
        addSegment (side);
        style = new GStyle();
        style.setForegroundColor (bar.color.darker());
        style.setBackgroundColor (bar.color.darker());        
        side.setStyle (style);
        int[] sideXy = {x0 + BAR_WIDTH, y0,
        x0 + BAR_WIDTH + DEPTH, y0 - DEPTH,
        x0 + BAR_WIDTH + DEPTH, y_ - DEPTH,
        x0 + BAR_WIDTH, y_,
        x0 + BAR_WIDTH, y0};
        side.setGeometry (sideXy);
        
        x0 += BAR_WIDTH + SPACING;
        }
        }
    }

  

