ModTheMod API
=============
The __ModTheMod API__ seeks to allow anyone to be able to write mods.

ModManager
------------

The central manager for all mods. Handles defining of mods and Component types. Accessed by a global variable `manager`.

### Methods

* `Prototype registerPrototype(Object definition)`  -- Register a new prototype with the Mod Manager.  The prototype definition is a Javascript object that has proprties for the `id`, prototype `properties`, event `handlers`, parent `prototypes`.

Example:
```javascript
var ItemComponent = manager.registerPrototype({
    "id": "item",
    "properties": {
        "name":  manager.stringType,
        "renderWithIcon": manager.booleanType,
        "stack_size": manager.numberType,
    	"texture": manager.textureType
    }
});
```

* `Prototype registerPrototype(Prototype parent, Object definition)`  -- Register a new prototype with the Mod Manager.  The prototype definition is a Javascript object that has proprties for the `id`, prototype `properties`, event `handlers`, parent `prototypes`








* Mod getMod `(name)` -- Gets a mod with the given name.

* Mod defineMod `(name, params, initialize(mod))` -- Defines a new mod with the given name and params. Initialize takes in the mod created as a parameter and 
should add everything to the mod.

* void include `(name)` -- Includes a mod to be downloaded.

* void require `(name)` -- Forces a mod to be downloaded. The mod will not be loaded if this mod is not found or downloaded.

* Mod[] getMods `()` -- Gets a list of all currently loaded mods.

Mod
----------
A Mod represents, well, a mod.

Mods are created when they are loaded. The Mod object can be accessed within the mod itself with the `mod` variable in the mod�s global scope.

### Properties

* name `<String>` -- The name of the mod.

* description `<String>` -- Javascript object containing a description, url, author, revision, and an array of strings called �uses� which contains all of the mods the component depends on.

### Methods

None so far.

Composite
----------
A Composite is a section of a mod that provides certain functionality. Composites are created by giving them certain properties which are put through a Factory to give them functionality.

Both properties and methods are accessed directly through the dot operator on the Component.

### Params



* properties -- Javascript object containing the default properties of the component. Null means the property must be set.

* methods -- Javascript object containing the methods to be used. The �this� keyword refers to the part that the method will be part of.

### Methods

* Object getProperty(property) -- Gets the value of the given property.

* boolean composedOf(String factory) -- Checks if the Component is composed of a component of the given factory.

* [Component] create[Component](name, params, initialize(component)) -- Creates a component with the given name, params, and runs the initializer after the Component is created via the associated factory. Components are accessed through the fields of the component.

Component
----------
A Component is an object containing the behaviors of how a component will work. (TODO: change the terminology on this) Example of creation:

```javascript
var liquidComponent = Game.defineComponent({
    name: "liquid",
    components: {
        block: {
            mesh: "deform",
            collision: "liquid",
            notchId: this.notchId
        }
    },
    properties: {
        placementObstacle: false,
        notchId: 1
    }
});
```

By defining a component as so, a new method in Game is created. This method would be `Game.register[Component]` For example, a component named �liquid� would now be able to be created with the `Game.registerLiquid` method.

The `Game.register` method has two forms which are described in `Game`.

Game
----------
The Game object represents the state of the engine. It is an abstraction over the underlying engine. It can be accessed through the global variable �game�. There is one Game object per mod; one scope per mod.

### Methods

* boolean addEventListener(name, callback) -- Adds an event listener to the specified event with the specified callback. The name should be something like �item_use�; the first Component of the name is the type of event, and the second is the actual action of the event.

* Player getPlayer(name) -- Gets a Player by their name.

* Event fireEvent(event) -- Calls an event. Events look like this: { type: �Event name�, myvar: �blah� }.

* Composite register(name, components) -- Registers a composite with the mod under the given name with the given components. Components is an object formatted as so: `{ component: { property: value }, component2: {}}�`

* Composite registerComposite(type, name, properties) -- Registers a composite with the mod under the given name with the given properties. The properties are the properties of the given component. The composite created is a composite consisting of one component -- the component given.

* Composite registerComposite(type, name, properties, components) -- Registers a composite with the mod under the given name with the given properties and components. The properties are the properties of the given component. The composite created is a composite consisting of one component -- the component given. Components is an object formatted as so: `{ component: { property: value }, component2: {}}` These components override any other components if they are set.

Command
----------
A command.

### Properties
* String[] aliases -- Potential aliases for the command.

Item
----------
An item.

Block < Item
----------

Entity
----------





Objects


Game 

The Game object represents the engine state.  In fact an alternate name for this object could be �Engine�.

Methods
addBlock( /*block definition */ )
Adds a new type of block to the game.  A reference to the new block type is returned.

addItem( /*Item defintion*/ )
Adds a new type of item to the game.  A reference to the new item type is returned.

addEvent(EventName, Callback)
Add a callback to be executed when a particular event occurs

addBinding( /* keybind definition */ )
Adds a new event that the user can bind to a key. 

Events
* Game events (new, load, start, save, quit)
* Window Events (resize, minimize, restore, maximize, getfocus, losefocus)
* World events (enter, leave)
* Chunk events (load, unload)
* UI Events (mousedown, mouseup, click, dblclick, mousemove, mouseover, mouseout, keydown, keyup) 
* Block events (create, place, added, remove, interact)
* Item events (place, pickup, drop, interact)

Window

The Window object represents the viewport into the world. There could potentially be multiple windows for the same game if the screen is split, or another window is opened.

doMouse(MouseEvent)
Simulate a mouse event as if enacted by the player.

doKey(KeyEvent)
Simulate a keyboard event as if enacted by the player.

setCamera( /* positioning information */ )
Change the camera view somehow.  Might be useful for a cutscene, for a deathcam, for switch to 3rd person, to simulate optics (telescope, binoculars)


Events
* Window Events (resize, minimize, restore, maximize, getfocus, losefocus)
* UI Events (mousedown, mouseup, click, dblclick, mousemove, mouseover, mouseout, keydown, keyup) 

World

The world is the physical collection of blocks that the player navigates through.  There could be multiple worlds for the same game (different dimensions, etc.)

Events
* World events (enter, leave)
* Chunk events (load, unload)
* Block events (create, place, added, remove, interact)

Region

A region is a volume of blocks in the World.  The purpose of this object is to act as a bounding box for a subset of the World.  World modification events that occur within the region are available for capture. This can help a mod writer limit the number of events fired, assuming the mod is able to define a locality for the part of the world it operates in.

Events
* Chunk events (load, unload)
* Block events (create, place, added, remove, interact)

Chunk

A chunk is a special-purpose region that is used for persistence and segmenting.

Events
* Chunk events (load, unload)
* Block events (create, place, added, remove, interact)


Blockset

A blockset is a collection of blocks for iteration and set operations.

Events
* Block events (create, place, added, remove, interact)


Block

A block is a physical block located in the world

Events
* Block events (create, place, added, remove, interact)

Item

An Item can be present on the ground in the world, or can be picked up, carried, or used by a player (or npc)

Events
* Item events (place, pickup, drop, interact)

UIElement

A UIElement is used to create custom screens for the player to interact with, for crafting, or NPC dialog, etc.




Glimpse

(Note this needs a better name)  Imagine a ray cast from the current camera position, and extending out to infinity.  The block in which the camera is currently positioned has an exit point for this ray, and every subsequent block has both an entrance and exit point.  An object is created when a UI event needs to determine what block(s) the user was pointing at when the event was fired.  This object can be used to determine the exact camera positioning, as well as the exact points in a block that the camera is looking at. 

Properties
block - The block.
entry - The facet the ray enters (will be null for the first block in an event)
exit - The facet the ray exits
points - An ordered array of points where the ray �intersects� the block and block contents.  This is only two points unless the block contains a collision mesh that does not fill the entire block, in which case it may be an arbitrary number of points.


Event

The event object 

Properties
cancelable - A boolean indicating whether the event is cancelable.
handler - The instance that the event handler was bound to.  A single event can be handled by multiple handlers, belonging to one or more different instances, and the handler property may change for each invocation.
selection - The block(s) or facet(s) or item(s) currently selected. (or null if there is no current selection)
target - The instance that the event is targeted upon.  This will vary depending on event type.
type - A string indicating event type.

Methods
stopPropagation() - Stops the propagation of this event to subsequent handlers.


UIEvent (extends Event)

The UIEvent is fired in response to a keyboard or mouse (or joystick?  gamepad?) action.  The UIEvent object inherits all the properties and methods of the Event object.  

Properties
target - The target of a UIEvent is *always* the Window instance that received the event.

Methods
nextGlimpse() - Returns the next Glimpse object 
nextVisibleGlimpse() - Returns the next Glimpse object for a non-transparent block
nextOpaqueGlimpse() - Returns the next Glimpse object for a non-translucent block



KeyEvent (extends UIEvent)

The event object 

Properties
screenX - The X coordinate of the mouse pointer on the user�s monitor
screenY - The Y coordinate of the mouse pointer on the user�s monitor
windowX - The X coordinate of the mouse pointer within Window instance that received the event.
windowY - The Y coordinate of the mouse pointer within Window instance that received the event.
ctrlKey - True if the control key was down when the event was fired
shiftKey - True if the shift key was down when the event was fired
altKey - True if the alt key was down when the event was fired
metaKey - True if the meta key was down when the event was fired
button1 - True if the mouse button 1 was down when the event was fired
button2 - True if the mouse button 2 was down when the event was fired
button3 - True if the mouse button 3 was down when the event was fired
button4 - True if the mouse button 4 was down when the event was fired
button5 - True if the mouse button 5 was down when the event was fired
key - A code representing the key that fired the event.


MouseEvent



Keybind

A keybind object is an event that was added to the game at runtime, and the user can fire this event by binding the keybind event to a key, and then pressing the key.

Methods
press()
Simulate the user pressing the key bound to this keybind.

Events
activate




Events

load(Chunk)

The load event fires when a chunk is loaded into memory.  This can be either by loading it from disk (or over the network), or by generating a previously nonexistent chunk.  The primary parameter is the chunk object.
generate(Chunk)

The generat event fires when a chunk is loaded into memory.  This can be either by loading it from disk (or over the network), or by generating a previously nonexistent chunk.The primary parameter is the chunk object.
unload(Chunk)

The unload event fires when a chunk is flushed from memory.  The primary parameter is the chunk object.
place(ItemInstance)

The placed event fires when an item in the player inventory is is being placed into the game world.  Returning �false� from the callback will cancel the placement of the item, and the item will not leave the player�s inventory.  The primary parameter is the item object.
create(BlockType)

The placed event fires when a block has been created to a mechanism other than the player placing an object from player inventory.  Returning �false� from the callback will cancel the creation of the block.  The primary parameter is the type object of the block being created.
added(BlockInstance)

The placed event fires after a block has added to the world.  The primary parameter is the newly placed block instance.
remove(BlockInstance)

The placed event fires before a block has been removed from the world.  Returning �false� from the callback will cancel the removal of the block. The primary parameter is the newly placed block instance.

interact(BlockInstance)

The interact event fires when the UI decides a player (or npc) is interacting with an �interactable� block.  This might be a button or switch or door.  The primary parameter is the block instance being interacted with.

mousedown(MouseEvent)

mouseup(MouseEvent)

click(MouseEvent)

dblclick(MouseEvent)

mousemove(MouseEvent)

mouseover(MouseEvent)

mouseout(MouseEvent)



mousedown, mouseup and click,dblclick, mousemove and finally mouseover and mouseout.
interact

mouse

key

click
key

place
destroy
interact

Chunk

MouseEvent

Region

Item

Block

MouseEvent

UI
pickup
drop



// Game.addBlock accepts an object, reads the properties and creates a new
// block type and adds it to the game world.  It returns a reference to the
// newly created block type.

Torch = Game.addBlock({
   name: "Torch",
   placedAs: "wallItem",
   model: "litTorch"
});

UnlitTorch = Game.addBlock({
   name: "Burnt Out Torch",
   placedAs: "wallItem",
   model: "unlitTorch"
});

// Blocks have a number of event handlers.  Here, we are referencing the
// "onPlace" event handler, called when a player (or NPC) places this
// block

Torch.addEvent("onPlace", function(){
    // Note that "this" refers to the current torch being placed
    this.luminescence = 15;
    this.fuel = 256;
});

// BlockSet is a Rhino Java Host Object that can contain arbitrary sets of
// blocks.  It exists for the convenience of the modder for easy membership
// testing and iteration.
torches = new BlockSet();


// The Game object also has a number of event handlers, "loadChunk" is called
// every time a chunk is loaded into the game world (either by being newly
// created, or by being loaded from network or disk)
Game.addEvent("loadChunk", function() {
    // Note that "this" refers to the current chunk being loaded
    
    // this.blocks returns a read-only BlockSet
    blocks = this.blocks;
    for (var i = 0; i < blocks.length; i++) {
        if (blocks.typeId == Torch.typeId) {
            torches.add(block);
        }
    }
});

Game.addEvent("loadChunk", function() {
    // BlockSet.removeAll removes all blocks from the blockset that are also
    // present in the collection passed as a parameter.
    torches.removeAll(this.blocks);
});

// setTimeout and setInterval work exactly as expected
setInterval(function() {
    for (var i = 0; i < torches.length; i++) {
        torch = torches[i];
        torch.fuel--;
        torch.luminescence = Math.max(torch.fuel, 15);
        if(torch.fuel == 0) {
            torches.remove(torch);
            torch.replaceWith(UnlitTorch);
        }
    }
}, 100000);



Game.addEvent("onClick", function() {
    // Note that "this" refers to the current mouse event
    
    if (this.button == 2) {
        this.relatedTarget.break();
    }
});

// Block is the root 
Block




// Add a cubbyhole to represent the head equipment slot associated with the
// player
var headCub = game.createCubbyhole();

// adding the new cubbyhole to the player assigns the contents of the cubbyhole
// to the player for ownership purposes
player.addCubbyhole(headCub);

// Add an event handler that fires when an item is dropped into this cubbyhole
headCub.addEvent("drop", function(item) {

   // We test to make sure that any item dropped here must have a head slot
   // property
   if (item.slot != "head") {
      
      // returning false disallows the drop event.
      return false;
   }
   
   // if the item fits in this slot, have the player equip the item.  Note that
   // we are returning the value from the equip event, so if the equip event
   // handler prevents the equip action, we also prevent the item from being
   // dropped in this cubbyhole
   return player.equip(this);
});

// Similarly, add a cubbyhole for the chest
var chestCub = game.createCubbyhole();
player.addCubbyhole(chestCub);
headCub.addEvent("drop", function(item) {
   if (item.slot != "chest") {
      return false;
   }
   return player.equip(this);
});

// and legs
var legCub = game.createCubbyhole();
player.addCubbyhole(legCub);
headCub.addEvent("drop", function(item) {
   if (item.slot != "leg") {
      return false;
   }
   return player.equip(this);
});

// and feet
var feetCub = game.createCubbyhole();
player.addCubbyhole(feetCub);
headCub.addEvent("drop", function(item) {
   if (item.slot != "feet") {
      return false;
   }
   return player.equip(this);
});

// We need a global variable to store the currently equipped item
var equipped = 1;

// Now we create cubbyholes for the quickslots 1 through 0
var equipCubs = [];
for (var int i = 0; i <= 9; i++) {
   
   // execute the following in a closure to caputre the value of i
   function(i) {
      // create a new cubbyhole, and associate it with the player
      var cub = game.createCubbyhole();
      player.addCubbyhole(cub);
      
      // Add a new keybinding to the game engine.  When this keybinding is
      // invoked the item in the associated cubbyhole will be equipped.  The
      // default keys to bind to this actions are the number keys  through 0
      var bind = game.createBinding("Equip Item " + i, i);
      bind.addEvent("invoke", function() {
         player.equip(cub);
         equipped = i;
      });
      
      equipCubs[i] = cub;
   }(i);   
}

// when the mousewheel is scrolled up, switch to the previous item
game.addEvent("mousescrollup", function() {
   eqippped = --equipped % 10;
   player.equip(equipCubs[equipped])
});

// when the mousewheel is scrolled down, switch to the next item
game.addEvent("mousescrolldown", function() {
   eqippped = ++equipped % 10;
   player.equip(equipCubs[equipped])
});


// Let's add the 4 craft cubbyholes
var craftCubs = [];
for (var int i = 0; i < 4; i++) {
   var cub = game.createCubbyhole();
   player.addCubbyhole(cub);
   craftCubs[i] = cub;
}


// And finally we make the inventory cubbyholes
var invCubs = [];
for (var int i = 0; i < 40; i++) {
   var cub = game.createCubbyhole();
   player.addCubbyhole(cub);
   invCubs[i] = cub;
}


// Now we need to build the UI.  We start by creating all of the individual
// panels, that will be assembled into the full interface

// create a UI panel to hold the 4 armor cubbyholes
var armorPanel = new LayoutGrid(4, 1);
armorPanel.append(headCub);
armorPanel.append(chestCub);
armorPanel.append(legCub);
armorPanel.append(feetCub);

// create a UI panel to hold the 4 crafting input cubbyholes
var craftInputPanel = new LayoutGrid(2, 2);
for (var int i = 0; i < 4; i++) {
   craftInputPanel.append(craftCubs[i]);
}

// create a UI panel to hold the crafting output cubbyhole
var craftOutputPanel = new LayoutGrid(1, 1);
craftOutputPanel.append(Crafting.createOutputCubby(craftCubs, 2));

// create a UI panel to hold the player inventory
var invPanel = new LayoutGrid(4, 10);
for (var int i = 0; i < 40; i++) {
   invPanel.append(invCubs[i]);
}

// create a UI panel to hold the player equipment quick slots
var equipPanel = new LayoutGrid(1, 10);
for (var int i = 0; i < 10; i++) {
   equipPanel.append(equipCubs[i]);
}

// arrange the invididual items at the top of the UI into a single panel
var topPanel = new LayoutGrid(1,5);
topPanel.append(armorPanel);
topPanel.append(player.getModelPanel());
topPanel.append(craftInputPanel);
topPanel.append("arrow.png");
topPanel.append(craftOutputPanel);

// and finally arrange everything into the final layout
var charPanel = new LayoutGrid(3,1);
charPanel.append(topPanel);
charPanel.append(invPanel);
charPanel.append(equipPanel);

// create a key binding for the inventory screen, and we're done!
var bind = game.createBinding("Show character", "e");
bind.addEvent("invoke", function() {
   charPanel.show();
});
