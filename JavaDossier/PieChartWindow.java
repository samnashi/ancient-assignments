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

public class PieChartWindow extends JFrame {
	private PieChart pieChart;

	public PieChartWindow() 
	{
		super("Pie Chart");
    
    		// Create the graphic canvas
    		GWindow window = new GWindow();
    		getContentPane().add (window.getCanvas());
    
    		// Create scane with default viewport and world extent settings
    		GScene scene = new GScene (window);

    		GStyle pieStyle = new GStyle();
    		pieStyle.setFont (new Font ("Dialog", Font.BOLD, 18));
    		pieStyle.setLineStyle (GStyle.LINESTYLE_INVISIBLE);
    		pieStyle.setForegroundColor (new Color (255, 255, 255));
    
    		pieChart = new PieChart (250, 250, 200);
    		pieChart.setStyle (pieStyle);

    		scene.add (pieChart);
    
    		pack();
    		setSize (new Dimension (500, 500));
    		setVisible (true);
	}

	public void addSector(double ratio, String label, GStyle style) {
		pieChart.addSector(ratio,label,style);
	}
	
	public GStyle getStyle()
  	{
		GStyle style = new GStyle();
		style.setBackgroundColor (new Color (Color.HSBtoRGB ((float)Math.random(), 0.4f, 0.8f)));
		return style;
	}
}