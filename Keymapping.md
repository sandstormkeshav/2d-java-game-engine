# Purpose #

storing multiple keys and making it easier to handle them.

# Fields #

**key** (Key array) stores multiple keys

# Constructor #

takes one or more Key Objects.

# Methods #

**getKeyNumber** (returns Int, takes String). converts the specified Key-name to the corresponding char value.
this method returns -1 if the key does not exist in the Keymapping.

**getKeyName** (returns String, takes int). like getKeyNumber but vice versa.

**addKey** (takes Key or String and int). adds another key to the Keymapping.

**removeKey** (takes Key or String or int) removes the specified Key from the Keymapping.

**keyPressed** (returns boolean, takes String). returns true if the Key with the specified name is pressed. False otherwise (also false if not found!!)

**keyReleased** (returns boolean, takes String). like keyPressed but returns the release status.

# Usage Example #

Add a keymapping to an Object and implement methods which should trigger at key input. Different Object have different Keymappings ...