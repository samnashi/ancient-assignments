
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

public class BarChartWindow extends JFrame
{
    int sizeX, sizeY;
    private BarChart barChart;
    public BarChartWindow(int sizeHoriz, int chartSizeVert)
    {
        super ("BarChartWindow");    
        sizeX = sizeHoriz;
        sizeY = chartSizeVert;
        // Create the graphic canvas
        GWindow window = new GWindow();
        getContentPane().add (window.getCanvas());
        
        // Create scane with default viewport and world extent settings
        GScene scene = new GScene (window);
        
        GStyle chartStyle = new GStyle();
        chartStyle.setLineStyle (GStyle.LINESTYLE_INVISIBLE);
        chartStyle.setFont (new Font ("Times", Font.BOLD, 10));
        chartStyle.setForegroundColor (new Color (0, 0, 0));
        
        barChart = new BarChart (5, 900, 400, 400);
        barChart.setStyle (chartStyle); 
        
        scene.add (barChart);
        
        pack();
        setSize (new Dimension (sizeX, 1000));
        setVisible (true);
    }

    public void addBar(String label, int val, Color color) {
        barChart.addBar(label,val,color);
    }
  
    public Color getColor(){
        return new Color (Color.HSBtoRGB ((float)Math.random(), 0.2f, 0.8f));
    }
  
}

