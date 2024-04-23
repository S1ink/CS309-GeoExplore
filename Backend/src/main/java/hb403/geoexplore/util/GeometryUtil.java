package hb403.geoexplore.util;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.util.GeometricShapeFactory;


public final class GeometryUtil {

	public static final int DEFAULT_CIRCLE_KEYPOINTS = 32;
	
	public static final WKTReader wkt_reader = new WKTReader();
	public static final GeometryFactory geom_factory = new GeometryFactory();
	public static final GeometricShapeFactory shape_factory = new GeometricShapeFactory(geom_factory);

	/** Converts a 'Well Known Text' string encoding geometry into a java object */
	public static Geometry getGeometry(String wkt) throws ParseException {
		return wkt_reader.read(wkt);
	}
	/** */
	public static Point makePoint(double x, double y) {
		return makePoint(new Coordinate(x, y));
	}
	/**  */
	public static Point makePoint(Coordinate c) {
		return geom_factory.createPoint(c);
	}
	/** */
	public static Polygon makeCircle(double x, double y, double rad) {
		return makeCircle(x, y, rad, DEFAULT_CIRCLE_KEYPOINTS);
	}
	/** */
	public static Polygon makeCircle(double x, double y, double rad, int keypoints) {
		return makeCircle(new Coordinate(x, y), rad, keypoints);
	}
	/** */
	public static Polygon makeCircle(Coordinate c, double rad, int keypoints) {
		shape_factory.setNumPoints(keypoints);
		shape_factory.setCentre(c);
		shape_factory.setSize(rad * 2);
		return shape_factory.createCircle();
	}
	/** */
	public static Polygon makeRectangle(double x1, double y1, double x2, double y2) {
		final double
			xmin = Math.min(x1, x2),
			xmax = Math.max(x1, x2),
			ymin = Math.min(y1, y2),
			ymax = Math.max(y1, y2);

		return geom_factory.createPolygon(
			new Coordinate[]{
				new Coordinate(xmin, ymin),
				new Coordinate(xmin, ymax),
				new Coordinate(xmax, ymax),
				new Coordinate(xmax, ymin),
				new Coordinate(xmin, ymin)
			}
		);
	}


}
