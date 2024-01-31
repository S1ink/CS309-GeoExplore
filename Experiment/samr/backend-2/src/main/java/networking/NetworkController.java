package networking;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/network")
public class NetworkController {
	
	@Autowired
	NodeRepository node_repo;


	@GetMapping(path = "/nodes")
	public List<Node> getNodes() {
		return this.node_repo.findAll();
	}
	@GetMapping(path = "/node/{id}")
	public Optional<Node> getNode(@PathVariable("id") Integer id) {
		return this.node_repo.findById(id);
	}

	@PostMapping(path = "/add")
	public Integer addNode(@RequestBody Node n) {
		if(this.node_repo.findById(n.id).isEmpty()) {
			this.node_repo.save(n);
			return n.id;
		}
		return null;
	}
	// @PostMapping(path = "/node/{id}/link")
	// public Integer linkNewNode(@RequestBody Node n) {
	// 	Optional<Node> node = this.node_repo.findById(n.id);
	// 	if(node.isEmpty()) {
	// 		this.node_repo.save(n);
	// 	}
	// }

	@PostMapping(path = "/dummy")
	public void acceptDummy(@RequestBody String txt) {
		System.out.println("recieved text input!: " + txt);
	}
	@PostMapping(path = "/generate")
	public void genRandom() {
		Random r = new Random();
		for(int i = 0; i < 10; i++) {
			this.node_repo.save(new Node(r.nextInt(100)));
		}
	}


}
