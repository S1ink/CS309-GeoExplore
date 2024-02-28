package hb403.geoexplore.util;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;


public final class GeometryUtil {
	
	public static final WKTReader wkt_reader = new WKTReader();
	/** Converts a 'Well Known Text' string encoding geometry into a java object */

	public static Geometry getGeometry(String wkt) throws ParseException {
		return wkt_reader.read(wkt);
	}


}
