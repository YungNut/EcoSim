package ecosim;

public class Herbivore extends Animal{

	//public Class foodType = Plant.class;

	public Herbivore(float x, float y, float size) {
		super(x, y, size);

		this.foodType = Plant.class;

		this.colorR = 0;
		this.colorG = 0;
		this.colorB = 127;
	}


}
