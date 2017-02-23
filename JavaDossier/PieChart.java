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

public class PieChart extends GObject
  {
    private int         x0_, y0_;
    private int         radius_;
    private Collection  sectors_;
    

    PieChart (int x0, int y0, int radius)
    {
      x0_      = x0;
      y0_      = y0;
      radius_  = radius;
      sectors_ = new ArrayList();
    }


    void addSector (double fraction, String text, GStyle style)
    {
      sectors_.add (new Sector (fraction, text, style));
    }
    
    
    public void draw()
    {
      removeSegments();

      // Loop through the sectors and draw the graphics for each
      double angle0 = 0.0;
      for (Iterator i = sectors_.iterator(); i.hasNext(); ) {
        Sector sector = (Sector) i.next();

        //
        // Geometry for the sector itself
        //
        GSegment segment = new GSegment();
        addSegment (segment);
        segment.setStyle (sector.style);

        double angle1 = angle0 + sector.fraction * Math.PI * 2.0;

        int[] sectorGeometry = Geometry.createSector (x0_, y0_, radius_,
                                                      angle0, angle1);

        segment.setGeometry (sectorGeometry);
        angle0 = angle1;

        //
        // Add annotation. Create an invisible line from the sector center
        // thorugh the center of the arc and out and associated annotation
        // with this line.
        //
        double[] p0 = new double[3];
        double[] p1 = new double[3];

        int nPoints = sectorGeometry.length / 2;
        int pointNo = (nPoints - 2) / 2;
        
        p1[0] = sectorGeometry[pointNo * 2 + 0];
        p1[1] = sectorGeometry[pointNo * 2 + 1];
        p1[2] = 0.0;

        double[] sectorCenter = Geometry.computePointOnLine (x0_, y0_,
                                                             p1[0], p1[1],
                                                             0.5);
        p0[0] = sectorCenter[0];
        p0[1] = sectorCenter[1];
        p0[2] = 0.0;

        // Ensure line extends far out of the sector
        Geometry.extendLine (p0, p1, 1000.0);
        
        GSegment annotationLine = new GSegment();
        addSegment (annotationLine);

        annotationLine.setGeometry ((int) p0[0], (int) p0[1],
                                    (int) p1[0], (int) p1[1]);

        // Add the percentage text
        int percent = (int) Math.round (sector.fraction * 100.0);
        GText text = new GText (percent + "%", GPosition.FIRST);
        annotationLine.addText (text);

        // Add the label to the same geometry point as the percentage;
        // It will be adjusted so it doesn't overlap
        text = new GText (sector.label, GPosition.FIRST);
        annotationLine.addText (text);
      }
    }
  }