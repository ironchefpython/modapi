console.log(1);

console.log(console.mm);
console.log(2);


manager.registerEvent({
	"type": "custom",
	"properties": {"foo": manager.stringType }
});

// game.addEventListener("custom", function(event) {
// console.log(event.get("foo")); });
// game.eventManager.propogateEvent("custom", {"foo": "bar"});

var ItemComponent = manager.registerPrototype({
    "id": "item",
    "properties": {
        "name":  manager.stringType,
        "renderWithIcon": manager.booleanType,
        "stack_size": manager.numberType,
    	"texture": manager.textureType
    }
});

var Material = manager.registerPrototype({
	"id": "material",
	"properties": {
		"color": manager.colorType,
		"hardness": manager.numberType
	}
});

var Metal = manager.registerPrototype(Material, {
	"id": "metal"
});

console.log(Metal.type);
console.log(manager.getPrototype("metal").type);

var MetalIngot = manager.registerPrototype(ItemComponent, {
	"id": "metal_ingot",
	"name": "${metal.name} Ingot",
    "renderWithIcon": true,
    "stack_size": 64,
	"properties": {
		"material": Metal.type
	},
	"events": {
		"init": function(event) {
			var $this = event.target;  
			$this.texture = game.colorizeTexture("ingot.png", $this.metal.color);
		}
	}
});

Iron = manager.registerPrototype(Metal, {
	"id": "iron",
	"name": "Iron",
	"color": "#E6E7E8",
	"hardness": 4,
	"durability": 5
});

Copper = manager.registerPrototype(Metal, {
	"id": "copper",
	"name": "Copper",
	"color": "#B87333",
	"hardness": 3,
	"durability": 5
});

var Tool = manager.registerPrototype(ItemComponent, {
	"id": "tool",
	"properties": {
        "material": Material.type,
        "durability": manager.numberType,
        "bonus_target": manager.functionType,
        "bonus": function() {
        	return this.hardness;
        },
	},
	"events": {
		"init": function(event) {
			var $this= event.target;  
			$this.durability = Math.pow($this.material.durability, 3);
		},
		"use": function(event) {
			var $this = event.equipped;  
			if ($this.bonus_target(event.target)) {
				event.action.time /= $this.bonus;
			} 
		},
		"harvest": function(event) {
			var $this = event.equipped;  
			$this.durability--;
			if ($this.durability == 0) {
				$this.dispatchEvent("break");
			}
		},
		"break": function(event) {
			event.target.destroy();  
		},
		
	}
});


var Axe = manager.registerPrototype(Tool, {
    "id" : "axe",

    "components": {
        "item": {
            "name" : "${material.name} Axe",
            "renderWithIcon": true,
            "stack_size": 1,
        },
        "craftable": {
            "recipe": "$material under $material over $material over blah",
        },
        "tool": {
                "bonus_target": function(obj) {
                	return obj.hasComponent(manager.registerPrototype("vanilla.timber"))
                }
        }
    },
	"events": {
		"init": function(event) {
			var $this = event.target;  
			$this.texture = game.colorizeTexture("axe.png", $this.material.color);
		}
	}
});

CopperIngot = manager.registerPrototype(MetalIngot, {"id": "copper_ingot", "material": Copper});
IronIngot = manager.registerPrototype(MetalIngot, {"id": "iron_ingot", "material": Iron});
CopperAxe = manager.registerPrototype(Axe, {"id": "copper_axe", "material": Copper});
IronAxe = manager.registerPrototype(Axe, {"id": "iron_axe", "material": Iron});


// var MetalOre = manager.registerPrototype({
// "id": "metal_ore",
// "listeners": {
// "harvest": function(event) {
// game.addItem(this.getProperty("drop"), event.location);
// event.block.location.world.dropItem(this, event.block.location);
// },
// },
// "properties": {
// "smelt_to": "metal_ingot"
// },
// "components": { "item": {} }
// });



// var Copper = OreComponent.registerPrototype({
// "name": "copper_ore",
// "texture": "copper_ore.png",
// "ingot_type": manager().getPrototype("copper_ingot")
// });
//
// var Iron = ItemComponent.registerPrototype({
// "name": "iron_ore",
// "texture": "iron_ore.png",
// "components": {
// "ore": {
// "ingot_type": manager().getPrototype("iron_ingot")
// }
// }
// });
//
// var Tin = manager.registerPrototype({
// "name": "tin_ore",
// "components": {
// "item": {
// "texture": "tin_ore.png",
// },
// "ore": {
// "ingot_type": "copper_bar"
// }
// }
// });

//
// var Actor = manager.registerPrototype("Actor");
//	
// ConsumeEvent = manager.registerEvent({
// "type": "use",
// "properties": {
// "actor": manager().getComponent("Actor")
// }
// });
//
// ConsumeEvent = manager.registerEvent({
// "type": "consume",
// "properties": {
// "actor": manager().getComponent("Actor")
// }
// });
//
//
// var clickableItem = manager.registerPrototype("clickable");
// OreComponent2.addEventListener("click", function(event) {
// // event.target.dispatchEvent(consumeEventFactory, )
// });
//


//
// var foodComponent = manager.registerPrototype({
// "name": "food",
// "listeners": {
// "consume": [ function(event) {
// event.actor.health += event.target.getProperty("health");
// if (event.entity.type == "player") {
// player.sendSound("omnom.ogg");
// }
// }],
// "use": [function(event) {
// event.target.dispatchEvent(consumeEventFactory, {
// "actor": player,
// "target": event.target
// });
// }]
// }
//
// });


// createFood(name, texture, health) {
// return manager.registerItem({
// "name": name,
// "texture": texture,
// "components": {
// "food": {
// "health": health
// }
// }
// });
// }

// Some stuff might not be implemented below
// var chicken = createFood("Chicken", "chicken.png", 50);




console.log("The registered components are: " + [c.id for each (c in manager.prototypes)]);

console.log("The color of Copper is : " + manager.getPrototype("copper").getPropertyValue("color"));
