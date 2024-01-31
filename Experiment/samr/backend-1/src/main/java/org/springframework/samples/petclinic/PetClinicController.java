package org.springframework.samples.petclinic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetRepository;


@RestController
public class PetClinicController {
	
	@Autowired
	OwnerRepository owners_repo;
	@Autowired
	PetRepository pets_repo;


	/** Owners */

	@RequestMapping(method = RequestMethod.POST, path = "/owners/new")
	public String saveOwner(Owner owner) {
		owners_repo.save(owner);
		return "New Owner "+ owner.getFirstName() + " Saved";
	}
		// function just to create dummy data
	@RequestMapping(method = RequestMethod.GET, path = "/owner/create")
	public String createDummyData() {
		final Integer[] NO_PETS = new Integer[]{};
		owners_repo.save( new Owner(1, "John", "Doe", "404 Not found", "some numbers", NO_PETS) );
		owners_repo.save( new Owner(2, "Jane", "Doe", "Its a secret", "you wish", NO_PETS) );
		owners_repo.save( new Owner(3, "Some", "Pleb", "Right next to the Library", "515-345-41213", NO_PETS) );
		owners_repo.save( new Owner(4, "Chad", "Champion", "Reddit memes corner", "420-420-4200", NO_PETS) );
		return "Successfully created dummy data";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/owners")
	public List<Owner> getAllOwners() {
		return owners_repo.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/owners/{ownerId}")
	public Optional<Owner> findOwnerById(@PathVariable("ownerId") int id) {
		return owners_repo.findById(id);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/owners/{ownerId}/pets")
	public List<Pet> findPetsByOwnerId(@PathVariable("ownerId") int id) {
		Optional<Owner> owner = findOwnerById(id);
		final ArrayList<Pet> pets = new ArrayList<>();
		if(owner.isPresent()) {
			final Owner o = owner.get();
			for(Integer pet : o.pet_ids) {
				Optional<Pet> p = pets_repo.findById(pet);
				if(p.isPresent()) {
					pets.add(p.get());
				}
			}
		}
		return pets;
	}


	/** Pets */

	@RequestMapping(method = RequestMethod.POST, path = "/pets/new")
	public String savePet(Pet pet) {
		pets_repo.save(pet);
		return "New pet " + pet.name + " saved to database";
	}

	@RequestMapping(method = RequestMethod.GET, path = "pets")
	public List<Pet> allPets() {
		return pets_repo.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, path = "pets/{petId}")
	public Optional<Pet> findPetById(@PathVariable("petId") int id) {
		return pets_repo.findById(id);
	}

	@RequestMapping(method = RequestMethod.GET, path = "pets/{petId}/owner")
	public Optional<Owner> findOwnerByPetId(@PathVariable("petId") int id) {
		Optional<Pet> pet = findPetById(id);
		if(pet.isPresent()) {
			return this.findOwnerById(pet.get().id);
		}
		return Optional.empty();
	}


}
