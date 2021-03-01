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

	@Override
	public void huntTarget() {
		if(target.size > 0.05) {
			target.size -= 0.05;
			this.size += 0.05;
		} else {
			target = null;
		}
//		System.out.println(target.size);
	}


}
