The "objects"-file stores information about object-creation. In general it stores the decimal value of the char used to represent in the "level"-file.

example:

objects-file:
Mario = 77

the char value of the capital "M" is 77. So each time we find a capital M in the "level"-file, the loader will create a Mario.

the "objects"-file should be stored within a level-archive.