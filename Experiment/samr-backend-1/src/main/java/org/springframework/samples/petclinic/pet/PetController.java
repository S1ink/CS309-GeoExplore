package org.springframework.samples.petclinic.pet;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PetController {

	@Autowired
	PetRepository pets_repository;


	@RequestMapping(method = RequestMethod.POST, path = "/pets/new")
	public String savePet(Pet pet) {
		pets_repository.save(pet);
		return "New pet " + pet.name + " saved to database";
	}

	@RequestMapping(method = RequestMethod.GET, path = "pets")
	public List<Pet> allPets() {
		return pets_repository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, path = "pets/{petId}")
	public Optional<Pet> findPetById(@PathVariable("petId") int id) {
		return pets_repository.findById(id);
	}

	@RequestMapping(method = RequestMethod.GET, path = "pets/{petId}/owner")
	public Optional<Owners> findPetByIdOwner(@PathVariable("petId") int id) {
		Optional<Pet> pet = findPetById(id);
		if(pet.isPresent()) {
			
		}
	}

}
