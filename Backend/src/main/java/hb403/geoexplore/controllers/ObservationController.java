package hb403.geoexplore.controllers;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.datatype.MarkerTag;
import hb403.geoexplore.datatype.marker.AlertMarker;
import hb403.geoexplore.datatype.marker.ObservationMarker;
import hb403.geoexplore.datatype.marker.ReportMarker;
import hb403.geoexplore.datatype.marker.repository.MarkerTagRepository;
import hb403.geoexplore.datatype.marker.repository.ObservationRepository;
import hb403.geoexplore.util.GeometryUtil;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ObservationController {

    @Autowired
    protected ObservationRepository obs_repo;
    @Autowired
    protected MarkerTagRepository tags_repo;
    @Autowired
    protected UserRepository users_repo;


    // C of Crudl, adds observation to repo
    @Operation(summary = "Add a new observation to the database")
    @PostMapping(path = "geomap/observations")
    public @ResponseBody ObservationMarker saveObs(@RequestBody ObservationMarker observation) {
        if (observation != null) {
            observation.nullifyId();
            observation.enforceLocationIO();
            final ObservationMarker obs = this.obs_repo.save(observation);
            obs.enforceLocationTable();
            return obs;
        }
        return null;
    }

    // R of Crudl gets observation from repo
    @Operation(summary = "Get an observation from the database from its id")
    @GetMapping(path = "geomap/observations/{id}")
    public @ResponseBody ObservationMarker getObs(@PathVariable Long id) {
        if (id != null) {
            try {
                final ObservationMarker o = this.obs_repo.findById(id).get();
                o.enforceLocationTable();
                return o;
            } catch(Exception e) {
                // continue >>> (return null)
            }
        }
        return null;
    }

    // U of Crudl
    @Operation(summary = "Update an observation already in the database by its id")
    @PutMapping(path = "geomap/observations/{id}")
    public @ResponseBody ObservationMarker updateObs(@PathVariable Long id, @RequestBody ObservationMarker observation) {
        if (id != null && observation != null){
            observation.setId(id);
            observation.enforceLocationIO();
            final ObservationMarker obs = this.obs_repo.save(observation);
            obs.enforceLocationTable();
            return obs;
        }
        return null;
    }

    // D of Crudl
    @Operation(summary = "Delete an observation in the database by its id")
    @DeleteMapping(path = "geomap/observations/{id}")
    public @ResponseBody ObservationMarker deleteObs(@PathVariable Long id){
        if (id != null) {
            try {
                final ObservationMarker ref = this.getObs(id);
                this.obs_repo.deleteById(id);
                ref.enforceLocationTable();
                return ref;
            } catch(Exception e) {

            }
        }
        return null;
    }

    // L of Crudl
    @Operation(summary = "Get a list of all the observations in the database")
    @GetMapping(path = "geomap/observations")
    public List<ObservationMarker> getAllObs() {
        final List<ObservationMarker> obs = this.obs_repo.findAll();
        for (ObservationMarker o : obs){
            o.enforceLocationTable();
        }
        return obs;
    }


    /** Returns the list of events within the bounds generated by the provided WKT geometry string */
    @Operation(summary = "Get a list of the observations whose locations are bounded by the provided WKT geometry string")
	@GetMapping(path = "geomap/observations/within")
	public @ResponseBody List<ObservationMarker> getObservationsWithinBounds(@RequestBody String wkt_bounds_geom) {	// takes in 'well known text' for the bounding geometry --> may define special json formats for predefined bounds later
		try {
			final List<ObservationMarker> bounded = this.obs_repo.findWithin( GeometryUtil.getGeometry(wkt_bounds_geom) );
			// System.out.println("Recieved " + bounded.size() + " bounded events");
			for(ObservationMarker o : bounded) {
				o.enforceLocationTable();
			}
			return bounded;
		} catch(Exception e) {
			System.out.println("ObservationMarker.getObservationsWithinBounds(): Encountered exception! -- " + e.getMessage());
			// continue >>> (return null)
		}
		return null;
	}



    @Operation(summary = "Add a prexisting tag by its id to a marker by its id")
	@PostMapping(path = "geomap/observations/{id}/tags")
	public @ResponseBody ObservationMarker addTagToMarkerById(@PathVariable Long id, @RequestBody Long tag_id) {
		if(id != null && tag_id != null) {
			try {
				final MarkerTag t = this.tags_repo.findById(tag_id).get();
				final ObservationMarker m = this.obs_repo.findById(id).get();
				if(m.getTags().add(t)) {
					this.obs_repo.save(m);
					return m;
				}
			} catch(Exception e) {

			}
		}
		return null;
	}

    @Operation(summary = "Add a prexisting user by id as a confirmation to a marker by its id")
	@PostMapping(path = "geomap/observations/{id}/confirmations")
	public @ResponseBody ObservationMarker addUserToConfirmedById(@PathVariable Long id, @RequestBody Long user_id) {
		if(id != null && user_id != null) {
			try {
				final ObservationMarker m = this.obs_repo.findById(id).get();
				final User u = this.users_repo.findById(user_id).get();
				if(m.getConfirmed_by().add(u)) {
					this.obs_repo.save(m);
					return m;
				}
			} catch(Exception e) {

			}
		}
		return null;
	}



    /** TODO:
     * - get marker creator (User) by marker id
     * - get marker tags (MarkerTag[]) by marker id
     * - append [NEW] marker tag to list, accessed by marker id
     * - append [EXISTING] marker tag to list, accessed by marker id and tag id
     * - append User to marker "listed users" by marker id and user id
     */


}
