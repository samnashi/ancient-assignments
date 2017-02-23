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
import java.awt.*;
import java.util.*;
import javax.swing.*;
import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;

public class Sector
{
    public double  fraction;
    public String  label;
    public GStyle  style;

    public Sector (double fraction, String label, GStyle style)
    {
      this.fraction = fraction;
      this.label    = label;
      this.style    = style;
    }
}