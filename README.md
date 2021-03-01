# EcoSim


## What is EcoSim?
#### Ecosim is an ecosystem simulator, ideally being able to reach a point of sustainability. All of the organisms in the ecosystem create offspring and pass down their genes, intended to simulate the process of natural selection. 
The main goal of EcoSim is to simulate an ecosystem that can evolve to sustain itself for extended periods of time.

## Details
### Current Organism Types
- Plants
- Herbivores
- Carnivores

### Genetics 
Animals have limited lifespans, and must find a mate to pass on their genes to offspring and additionally keep the ecosystem alive. The offspring have random mixture of genes from their two parents

## Notes
Both the EcoSim client and server will be written in java. The client uses OpenGL as a renderer using the LWJGL library. The used LWJGL library is included in the project meaning no additionally libaries must be installed other than OpenGL. 

## ToDo
- Setup OpenGL
  - [X] Render rganisms
  - [X] Basic camera controller
-  Basic Organism Behavior
    - [X] Target food
    - [X] Move along vectors
    - [X] Herbivores consume plants and find new plants
    - [ ] Carnivores attack Herbivores
    - [ ] Herbivores respond to carnivore attacks
    - [ ] Animals find mates
    - [ ] Plants create offspring
- Hosting
    - [ ] Server sends client data through UDP server
    - [ ] Client loads data in real time
- Quality of Life
    - [ ] Click organisms for genetic details display
    - [ ] Random name generation for species
    - [ ] Optimize rendering for speed