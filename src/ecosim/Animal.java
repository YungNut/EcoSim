package ecosim;


import java.util.ArrayList;

/* FOR TESTING ONLY */


public class Animal extends Organism {

	public Class foodType;

	public Organism target;
	
	float speed = 5;

	public Animal(float x, float y, float size) {
		super(x, y, size);
		isAnimal = true;
	}

	public void findFood(ArrayList<Organism> organisms) {

		ArrayList<Organism> nearby = new ArrayList<Organism>();

		for(Organism o : organisms) {
			if(o.getClass().equals(foodType)) {
				nearby.add(o);
				System.out.println("Found a " + foodType.getSimpleName());
			}
		}
		if(nearby.size() > 1) {
			Organism closest = null;
			float closestDist = Float.MAX_VALUE;

			for(Organism o : nearby) {
				float dist = getDistance(o, this);
				if (dist < closestDist) {
					closest = o;
					closestDist = dist;
				}
			}

			target = closest;

		} else if (nearby.size() == 1) {
			target = (Plant)nearby.get(0);
		} else {
			target = this;
			System.out.println("failed");
		}
	}
	
}
