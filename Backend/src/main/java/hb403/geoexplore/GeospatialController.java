package hb403.geoexplore;

import hb403.geoexplore.datatype.*;
import hb403.geoexplore.util.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;


@RestController
public class GeospatialController {

	@Autowired
	protected PointRepository geo_repo;


/** CRUDL for PointEntity(s) in the DB */

	@PostMapping(path = "/points/add")
	public @ResponseBody PointEntity savePoint(@RequestBody String wkt_pt) {
		System.out.println(wkt_pt);
		try {
			final PointEntity saved = geo_repo.save(new PointEntity( (Point)GeometryUtil.getGeometry(wkt_pt) ));
			return saved;
		} catch(ParseException e) {
			System.out.println("[PointRepository.savePoint()]: Failed to save point - exception occured!: " + e.getMessage());
			return null;
		}
	}
	@GetMapping(path = "/points/id/{pt_id}")
	public @ResponseBody Optional<PointEntity> getPointById(@PathVariable Long pt_id) {
		if(pt_id == null) return Optional.empty();
		return geo_repo.findById(pt_id);
	}
	@PutMapping(path = "points/id/{pt_id}")		// i don't know how to actually update the backer repository
	public @ResponseBody Optional<PointEntity> updatePointById(@PathVariable Long pt_id, @RequestBody String wkt_pt) {
		if(pt_id == null) return Optional.empty();
		final Optional<PointEntity> val = this.getPointById(pt_id);
		if(val.isEmpty()) return Optional.empty();
		final PointEntity current = val.get();
		try {
			current.updatePoint( (Point)GeometryUtil.getGeometry(wkt_pt) );
			geo_repo.save(current);
			return Optional.of(current);
		} catch(ParseException e) {
			System.out.println("[PointRepository.updatePointById()]: Failed to udpate point - exception occured!: " + e.getMessage());
			return Optional.empty();
		}
	}
	@DeleteMapping(path = "points/id/{pt_id}")
	public @ResponseBody Optional<PointEntity> deletePointById(@PathVariable Long pt_id) {
		if(pt_id == null) return Optional.empty();
		final Optional<PointEntity> val = this.geo_repo.findById(pt_id);
		if(val.isEmpty()) return Optional.empty();
		this.geo_repo.deleteById(pt_id);
		return val;
	}
	@GetMapping(path = "/points")
	public @ResponseBody List<PointEntity> getPoints() {
		return geo_repo.findAll();
	}



	@GetMapping(path = "points/boundedby")
	public @ResponseBody List<PointEntity> getWithinBounds(@RequestBody String wkt_geom) {
		System.out.println(wkt_geom);
		try {
			return this.geo_repo.findWithin( GeometryUtil.getGeometry(wkt_geom) );
		} catch(ParseException e) {
			System.out.println("[PointRepository.getWithinBounds()]: Failed to compute intersections - exception occured!: " + e.getMessage());
			return null;
		}
	}


}