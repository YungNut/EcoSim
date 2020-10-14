package ecosim;

import java.util.Random;

public class Organism {
	
	public float x;
	public float y;
	public float size;
	
	public byte colorR;
	public byte colorG;
	public byte colorB;
	
	public Organism(float x, float y, float size) {
		this.x = x;
		this.y = y;
		this.size = size;
		
		Random r = new Random();
		colorR = (byte) ((r.nextInt() & Integer.MAX_VALUE) % 150);
		colorG = (byte) ((r.nextInt() & Integer.MAX_VALUE) % 150);
		colorB = (byte) ((r.nextInt() & Integer.MAX_VALUE) % 150);
	}
	
	
}
