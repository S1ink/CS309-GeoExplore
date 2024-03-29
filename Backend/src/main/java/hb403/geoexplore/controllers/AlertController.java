package hb403.geoexplore.controllers;

import hb403.geoexplore.datatype.MarkerTag;
import hb403.geoexplore.datatype.marker.AlertMarker;
import hb403.geoexplore.datatype.marker.EventMarker;
import hb403.geoexplore.datatype.marker.repository.AlertRepository;
import hb403.geoexplore.datatype.marker.repository.MarkerTagRepository;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class AlertController {
	
	@Autowired
	protected AlertRepository alert_repo;
	@Autowired
	protected MarkerTagRepository tags_repo;


	@Operation(summary = "Get an alert in the database from its id")
	@GetMapping(path = "geomap/alerts/{id}")
	public @ResponseBody AlertMarker getAlertById(@PathVariable Long id) {
		if(id != null) {
			try {
				return this.alert_repo.findById(id).get();
			} catch(Exception e) {

			}
		}
		return null;
	}
	@Operation(summary = "Get all alerts in the database")
	@GetMapping(path = "geomap/alerts")
	public @ResponseBody List<AlertMarker> getAllAlerts() {
		return this.alert_repo.findAll();
	}

	@Operation(summary = "Delete an alert from the database by its id")
	@DeleteMapping(path = "geomap/alerts/{id}")
	public @ResponseBody AlertMarker deleteAlertById(@PathVariable Long id) {
		if(id != null) {
			try {
				final AlertMarker a = this.alert_repo.findById(id).get();
				this.alert_repo.deleteById(id);
				a.enforceLocationTable();
				return a;
			} catch(Exception e) {

			}
		}
		return null;
	}



	@Operation(summary = "Add a prexisting tag by its id to a marker by its id")
	@PostMapping(path = "geomap/alerts/{id}/tags")
	public @ResponseBody AlertMarker addTagToMarkerById(@PathVariable Long id, @RequestBody Long tag_id) {
		if(id != null && tag_id != null) {
			try {
				final MarkerTag t = this.tags_repo.findById(tag_id).get();
				final AlertMarker m = this.alert_repo.findById(id).get();
				if(m.getTags().add(t)) {
					this.alert_repo.save(m);
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
