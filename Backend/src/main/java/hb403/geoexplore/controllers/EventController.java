package hb403.geoexplore.controllers;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;
import hb403.geoexplore.datatype.MarkerTag;
import hb403.geoexplore.datatype.marker.AlertMarker;
import hb403.geoexplore.datatype.marker.EventMarker;
import hb403.geoexplore.datatype.marker.repository.EventRepository;
import hb403.geoexplore.datatype.marker.repository.MarkerTagRepository;
import hb403.geoexplore.datatype.request.Location;
import hb403.geoexplore.datatype.request.LocationRange;
import hb403.geoexplore.util.GeometryUtil;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class EventController {
	
	@Autowired
	protected EventRepository events_repo;
	@Autowired
	protected MarkerTagRepository tags_repo;
	@Autowired
	protected UserRepository users_repo;
	@Autowired
	protected CommentRepository commentRepository;


	/** [C]rudl - Add a new event to the database */
	@Operation(summary = "Add a new event to the database")
	@PostMapping(path = "geomap/events")
	public @ResponseBody EventMarker saveEvent(@RequestBody EventMarker event) {
		if(event != null) {
			event.nullifyId();
			event.enforceLocationIO();
			event.applyNewTimestamp();
			final EventMarker e = this.events_repo.save(event);
			e.enforceLocationTable();
			return e;
		}
		return null;
	}
	/** c[R]udl - Get an event from the database from its id */
	@Operation(summary = "Get an event from the database from its id")
	@GetMapping(path = "geomap/events/{id}")
	public @ResponseBody EventMarker getEventById(@PathVariable Long id) {
		if(id != null) {
			try {
				final EventMarker e = this.events_repo.findById(id).get();
				e.enforceLocationTable();
				return e;
			} catch(Exception e) {
				// continue >>> (return null)
			}
		}
		return null;
	}
	/** cr[U]dl - Update an event already in the database by its id */
	@Operation(summary = "Update an event already in the database by its id")
	@PutMapping(path = "geomap/events/{id}")
	public @ResponseBody EventMarker updateEventById(@PathVariable Long id, @RequestBody EventMarker event) {
		if(id != null && event != null) {
			event.setId(id);
			event.enforceLocationIO();
			event.applyUpdatedTimestamp();
			final EventMarker e = this.events_repo.save(event);
			e.enforceLocationTable();
			return e;
		}
		return null;
	}
	/** cru[D]l - Delete an event in the database by its id */
	@Operation(summary = "Delete an event in the database by its id")
	@DeleteMapping(path = "geomap/events/{id}")
	public @ResponseBody EventMarker deleteEventById(@PathVariable Long id) {
		if(id != null) {
			try {
				final EventMarker ref = this.getEventById(id);
				this.events_repo.deleteById(id);
				ref.enforceLocationTable();
				return ref;
			} catch(Exception e) {
				// continue >>> (return null)
			}
		}
		return null;
	}

	/** crud[L] - Get a list of all the events in the database */
	@Operation(summary = "Get a list of all the events in the database")
	@GetMapping(path = "geomap/events")
	public @ResponseBody List<EventMarker> getAllEvents() {
		final List<EventMarker> events = this.events_repo.findAll();
		for(EventMarker e : events) {
			e.enforceLocationTable();
		}
		return events;
	}



	/** Returns the list of events within the bounds generated by the provided WKT geometry string */
	@Operation(summary = "Get the set of the events whose locations are bounded by the provided WKT geometry string")
	@GetMapping(path = "geomap/events/within/poly")
	public @ResponseBody Set<EventMarker> getEventsWithinPoly(@RequestBody String wkt_bounds_geom) {	// takes in 'well known text' for the bounding geometry --> may define special json formats for predefined bounds later
		try {
			final Set<EventMarker> bounded = this.events_repo.findWithin( GeometryUtil.getGeometry(wkt_bounds_geom) );
			// System.out.println("Recieved " + bounded.size() + " bounded events");
			for(EventMarker e : bounded) {
				e.enforceLocationTable();
			}
			return bounded;
		} catch(Exception e) {
			System.out.println("EventMarker.getEventsWithinPoly(): Encountered exception! -- " + e.getMessage());
			// continue >>> (return null)
		}
		return null;
	}

	/**  */
	@Operation(summary = "Get the set of the events whose locations are bounded by the provided location window")
	@GetMapping(path = "geomap/events/within/rect")
	public @ResponseBody Set<EventMarker> getEventsWithinRect(@RequestBody LocationRange range) {	// takes in 'well known text' for the bounding geometry --> may define special json formats for predefined bounds later
		if(range == null || range.isInvalid()) return null;
		try {
			final Set<EventMarker> bounded = this.events_repo.findWithin( range.getRect() );
			// System.out.println("Recieved " + bounded.size() + " bounded events");
			for(EventMarker e : bounded) {
				e.enforceLocationTable();
			}
			return bounded;
		} catch(Exception e) {
			System.out.println("EventMarker.getEventsWithinRect(): Encountered exception! -- " + e.getMessage());
			// continue >>> (return null)
		}
		return null;
	}

	/** */
	@Operation(summary = "Get the distance to an event from a specified location (IN MILES)")
	@GetMapping(path = "geomap/events/{id}/distance")
	public @ResponseBody Double getDistanceToMarkerById(@PathVariable Long id, @RequestBody Location src) {
		if(id != null && src != null && src.isValid()) {
			final EventMarker m = this.getEventById(id);
			if(m != null) {
				m.enforceLocationTable();
				return GeometryUtil.EARTH_RADIUS_MILES * GeometryUtil.arcangleDegInRad(src.longitude, src.latitude, m.getIo_longitude(), m.getIo_latitude());	// longitude is theta, latitude is phi
			}
		}
		return null;
	}



	@Operation(summary = "Add a prexisting tag by its id to a marker by its id")
	@PostMapping(path = "geomap/events/{id}/tags")
	public @ResponseBody EventMarker addTagToMarkerById(@PathVariable Long id, @RequestBody Long tag_id) {
		if(id != null && tag_id != null) {
			try {
				final MarkerTag t = this.tags_repo.findById(tag_id).get();
				final EventMarker m = this.events_repo.findById(id).get();
				if(m.getTags().add(t)) {
					m.applyUpdatedTimestamp();
					this.events_repo.save(m);
					return m;
				}
			} catch(Exception e) {

			}
		}
		return null;
	}

	@Operation(summary = "Add a prexisting user by id as an attendee to a marker by its id")
	@PostMapping(path = "geomap/events/{id}/attendees")
	public @ResponseBody EventMarker addUserToAttendeesById(@PathVariable Long id, @RequestBody Long user_id) {
		if(id != null && user_id != null) {
			try {
				final EventMarker m = this.events_repo.findById(id).get();
				final User u = this.users_repo.findById(user_id).get();
				if(m.getAttendees().add(u)) {
					m.applyUpdatedTimestamp();
					this.events_repo.save(m);
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
