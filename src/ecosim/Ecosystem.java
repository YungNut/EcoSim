package ecosim;

import java.util.ArrayList;
import java.util.Random;

public class Ecosystem {
	public ArrayList<Organism> organisms;

	public int worldWidth;
	public int worldHeight;

	public Ecosystem(int worldWidth, int worldHeight) {
		organisms = new ArrayList<Organism>();

		this.worldHeight = worldHeight;
		this.worldWidth = worldWidth;
	}

	public void createAllOrganisms(int herbivores, int carnivores, int plants) {
		
		Random r = new Random();
	
		for(int i = 0; i < herbivores; i++) 
				organisms.add(new Herbivore(r.nextInt(worldWidth), r.nextInt(worldHeight), 32));
			
		for(int i = 0; i < carnivores; i++)
			organisms.add(new Carnivore(r.nextInt(worldWidth), r.nextInt(worldHeight), 32));
		
		for(int i = 0; i < plants; i++)
			organisms.add(new Plant(r.nextInt(worldWidth), r.nextInt(worldHeight), 32));
			
			
	}
}
