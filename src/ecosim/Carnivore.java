package ecosim;

public class Carnivore extends Animal {

	public Carnivore(float x, float y, float size) {
		super(x, y, size);
		this.foodType = Herbivore.class;

		this.colorR = 127;
		this.colorG = 0;
		this.colorB = 0;
	}

	@Override
	public void huntTarget() {

	}
}
