package ecosim;


import java.util.ArrayList;

public class Animal extends Organism {

	public Class foodType;

	public Organism target;
	
	float speed = 1;

	public Animal(float x, float y, float size) {
		super(x, y, size);
		isAnimal = true;
	}

	public void findFood(ArrayList<Organism> organisms) {

		ArrayList<Organism> nearby = new ArrayList<Organism>();

		for(Organism o : organisms) {
			if(o.getClass().equals(foodType)) {
				nearby.add(o);
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

	float lastx = 0;
	float lasty = 0;

	public void moveToTarget() {
		// Make a vector between self and target
		float vecX = target.x - x;
		float vecY = target.y - y;
		// Get magnitude of vector
		float mag = (float)Math.hypot(vecX, vecY);
		// Normalize the vector
		float normX = vecX / mag;
		float normY = vecY / mag;
		// If the organism is closer than its speed
		if(Math.abs(mag) < speed) {
			x = target.x;
			y = target.y;
		} else {
			// Move self along vector to target, at speed
			x += speed * normX;
			y += speed * normY;
		}
	}
	
}
