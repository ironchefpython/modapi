/*
 * This is another mod. Things are best explained in code.
 */

// Requiring some stuff.
// Should the SDK be required automatically?
var SDK = require("sdk"),
    Gun = require("gunmod").Gun;

// Event definition.
var ToolBreakEvent = exports.ToolBreakEvent = mm.defineEvent({
    "name": "tool:break",
    "attributes": {
        "tool": "Entity < Tool" // Defined the same as tool properties
    }
});

// Prototype definition.
var tool = exports.tool = SDK.Item.extend({
    // Id is tool
    "id": "tool",
    
    // Properties
    "properties": {
        "durability": "Number", // The engine will call Type.valueOf("Number") or something
                               // and will make sure that all of these will be Number.
        "color": "String"
    },
    
    // Handlers: Event handlers for the tool, called when an Entity
    "handlers": {
        "item:use": function(event) {
            var entity = event.target;
            if (!event.clicked.matches("Entity < Block")) {
                return;
            }
            entity.durability -= 3;
            
            // Example event cancellation
            if (event.clicked.name.equalsIgnoreCase("Obsidian")) {
                event.cancelled = true;
            }
        }
    },
    
    // Bind: Function called after everything is set.
    // If after this function is called there are unset properties,
    // the entity will not be created and a runtime exception will be thrown.
    "bind": function(entity) {
        if (!entity.texture) {
            var texture = SDK.Texture.generateFromColor("#ff0000");
            entity.texture = texture;
        }
    }
});

// Example of a function that registers 4 prototypes.
var createToolSet = exports.createToolSet = function(material) {
    return {
        axe: SDK.Axe.define({
        	id: material.id + "_axe",
            name: material.name + " Axe",
            material: material
        }),
        hoe: SDK.How.define({
        	id: material.id + "_hoe",
            name: material.name + " Hoe",
            material: material
        }),
        sword: SDK.Sword.define({
        	id: material.id + "_sword",
            name: material.name + " Sword",
            material: material
        }),
        shovel: SDK.Shovel.define({
        	id: material.id + "_shovel",
            name: material.name + " Shovel",
            material: material
        })
    };
};


// Example of defining a new Gun to use in the game.
var SuperGun = exports.SuperGun = Gun.define({
    name: "Super Gun",
    texture: "supergun.png", // If instance of string, alias of SDK.Texture.textureFromFile("supergun.png")
    sound: "supergun.ogg"
});

// Example of listening to an event.
Game.listen("player:jump", function(event) {
    var player = event.player; // Alias of event.target.
    if (!player.inventory.contains(SuperGun)) {
        player.inventory.add(SuperGun.create());
    }
});

// Add the listener to players, and not all entities that can jump.
SDK.Player.addEventListener("jump", function(event) {
    if (!this.inventory.contains(manager.getPrototype("SuperGun"))) {
        game.giveItem("SuperGun", this);
    }
});

// Example of listening to an event again. Alternative to explicitly specifying handlers.
SuperGun.listen("gun:fire", function(event) {
    console.log("The gun " + event.target + " was fired.");
});

// Example of extending a Gun that will be extended more.
var MachineGun = exports.MachineGun = Gun.extend({
    id: "MachineGun",
    properties: {
        awesomeness: "Number"
    }
});

// Example of overriding and extending something. (define)
var SuperDuperGun = SuperGun.define({
    id: "SuperDuperGun",
    name: "Super Duper Gun",
    texture: "superdupergun.png"
});

// Example of overriding and extending something. (extend)
var Cannon = SuperGun.extend({
    "id": "cannon",
    "attributes": { //Instance-specific
        "cooldown": "Number"
    },
    "properties": {
        "ship_compatible": "[Prototype < Ship]" // Array of Prototypes < Ship
    },
    "events": {
        "gun:fire" : function(event) {
            var cannon = event.gun; // Alias for event.target
            if (cannon.cooldown > 0) {
                event.cancelled = true;
                return;
            }
            cannon.cooldown = 6000;
        },
        "entity:update": function(event) { // Incredibly inefficient, but I
            // want to demonstrate this event.
            var cannon = event.gun;

            var oldCooldown = cannon.cooldown;
            cannon.cooldown--;

            var cannonState = cannon.cooldown % 600;
            var oldState = oldCooldown % 600;

            if (cannonState == oldState) {
                return;
            }

            // Would go to a 45 degree angle. Makes no sense.
            cannon.transform.orientation.setPitch(cannonState * 4.5);
        }
    },
    "init": function(cannon) {
        if (!cannon.gauge) {
            cannon.gauge = 4; // Default 4 gauge.
        }
    }
});
