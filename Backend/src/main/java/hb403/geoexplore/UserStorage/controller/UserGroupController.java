package hb403.geoexplore.UserStorage.controller;

import java.util.List;

import hb403.geoexplore.UserStorage.repository.*;
import hb403.geoexplore.UserStorage.entity.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class UserGroupController {

	@Autowired
	UserGroupRepository group_repo;
	@Autowired
	UserRepository user_repo;

	
	@PostMapping(path = "user/groups")
	public @ResponseBody UserGroup addGroup(@RequestBody UserGroup group) {		// adds a group with full UserGroup serialized data
		if(group != null) {
			group.setId(-1L);
			return this.group_repo.save(group);
		}
		return null;
	}
	@PostMapping(path = "user/groups/create")
	public @ResponseBody UserGroup createGroup(@RequestBody String name) {		// creates and adds a group just from a name
		if(name != null && !name.isEmpty()) {
			final UserGroup g = new UserGroup();
			g.setTitle(name);
			g.setId(-1L);
			return this.group_repo.save(g);
		}
		return null;
	}

	@GetMapping(path = "user/groups/{id}")
	public @ResponseBody UserGroup getGroupById(@PathVariable Long id) {
		if(id != null) {
			try{
				return this.group_repo.findById(id).get();
			} catch(Exception e) {
				// ...
			}
		}
		return null;
	}

	@PutMapping(path = "user/groups/{id}")
	public @ResponseBody UserGroup updateGroupById(@PathVariable Long id, @RequestBody UserGroup group) {
		if(id != null && group != null) {
			group.setId(id);
			return this.group_repo.save(group);
		}
		return null;
	}

	@DeleteMapping(path = "user/groups/{id}")	// TODO: I think that deleting a group deletes all the users as well :(
	public @ResponseBody UserGroup deleteGroupById(@PathVariable Long id) {
		if(id != null) {
			try {
				final UserGroup g = this.getGroupById(id);
				this.group_repo.deleteById(id);
				return g;
			} catch(Exception e) {
				// ...
			}
		}
		return null;
	}

	@GetMapping(path = "user/groups")
	public @ResponseBody List<UserGroup> getAllGroups() {
		return this.group_repo.findAll();
	}

	@PostMapping(path = "user/groups/{group_id}/members")
	public @ResponseBody UserGroup addMemberToGroupById(@PathVariable Long group_id, @RequestBody Long user_id) {
		if(group_id != null && user_id != null) {
			try {
				final UserGroup g = this.group_repo.findById(group_id).get();
				final User u = this.user_repo.findById(user_id).get();
				if(g.getMembers().add(u)) {	// if successful add
					u.getGroups().add(g);
				} else {
					return g;	// maybe should return null to signify fail?
				}
				return this.group_repo.save(g);
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		return null;
	}

	// find by name?


}
