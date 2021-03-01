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

    public void createAllOrganisms(int hSpecies, int hCount, int cSpecies, int cCount, int pSpecies, int pCount) {

        Random r = new Random();

        for (int i = 0; i < hSpecies; i++) {
            for (int n = 0; n < hCount; n++) {
                Herbivore h = new Herbivore(r.nextInt(worldWidth), r.nextInt(worldHeight), 32);
                h.species = (short) i;
                h.generation = 0;
                organisms.add(h);
            }
        }

        for (int i = 0; i < cSpecies; i++) {
            for (int n = 0; n < cCount; n++) {
                Carnivore c = new Carnivore(r.nextInt(worldWidth), r.nextInt(worldHeight), 32);
                c.species = (short) i;
                c.generation = 0;
                organisms.add(c);
            }
        }

        for (int i = 0; i < pSpecies; i++) {
            for (int n = 0; n < pCount; n++) {
                Plant p = new Plant(r.nextInt(worldWidth), r.nextInt(worldHeight), 32);
                p.species = (short) i;
                p.generation = 0;
                organisms.add(p);
            }
        }

    }


}
