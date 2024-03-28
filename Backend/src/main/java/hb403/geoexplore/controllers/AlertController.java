package hb403.geoexplore.controllers;

import hb403.geoexplore.datatype.marker.AlertMarker;
import hb403.geoexplore.datatype.marker.repository.AlertRepository;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class AlertController {
	
	@Autowired
	AlertRepository alert_repo;


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



	/** TODO:
     * - get marker creator (User) by marker id
     * - get marker tags (MarkerTag[]) by marker id
     * - append [NEW] marker tag to list, accessed by marker id
     * - append [EXISTING] marker tag to list, accessed by marker id and tag id
     * - append User to marker "listed users" by marker id and user id
     */


}
