package ecosim;

import java.util.Random;

public class Organism {
	
	public float x;
	public float y;
	public float size;
	public boolean isAnimal;
	public Organism target;
	
	public byte colorR;
	public byte colorG;
	public byte colorB;
	public byte colorA;
	
	public Organism(float x, float y, float size) {
		this.x = x;
		this.y = y;
		this.size = size;
		
		Random r = new Random();
		colorR = (byte) ((r.nextInt() & Integer.MAX_VALUE) % 150);
		colorG = (byte) ((r.nextInt() & Integer.MAX_VALUE) % 150);
		colorB = (byte) ((r.nextInt() & Integer.MAX_VALUE) % 150);
		colorA = (byte) 255;
	}

	public float getDistance(Organism a, Organism b) {
		return (float)(Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y),2)));

	}
	
}
