// This seems good. Would add a primary material settings here
var ItemComponent = game.modManager.registerComponent({
    "id": "item",
    "properties": {
        "name": game.modManager.StringType,
        "renderWithIcon": game.modManager.BooleanType,
        "stack_size": game.modManager.NumberType,
     "texture": game.modManager.TextureType,
        "primary_material": game.modManager.PrefabRefType
    }
});

// Nor do I don't mind this
var Material = game.modManager.registerComponent({
"id": "material",
"properties": {
"color": game.modManager.ColorType,
"hardness": game.modManager.NumberType
}
});

// I'll go with an additional component for the sake of example
var Metal = game.modManager.registerComponent({
"id": "metal"
"properties": {
"conductivity": game.modManager.NumberType
}
});

// Actual materials are prefabs composed of at least a material component
Iron = game.modManager.registerPrefab({
"id" = "Iron",
"components" = {
    "material" = {
        "name": "Iron",
        "color": "#E6E7E8",
        "hardness": 4,
        "durability": 5
    },
    "metal" = {
        "conductivity" = 0.7
    }
}
});

Copper = game.modManager.registerPrefab({
"id" = "Copper",
"components" = {
    "material" = {
        "name": "Copper",
        "color": "#B87333",
        "hardness": 3,
        "durability": 5
    },
    "metal" = {
        "conductivitiy" = 1
    }
});

// Metal Ingot is a prefab for an item
var MetalIngot = game.modManager.registerPrefab({
"id": "metal_ingot",
"components" : {
    "itemComponent" : {
        "name": "${material.name} Ingot",
        "renderWithIcon": true,
        "stack_size": 64
        // Skip primary material? Dunno what the "<metal" notation means.
    }
}
// For performance reason I probably wouldn't create a new texture for each instance of an ingot. Plus how would this work after save+load? Could be in some startup code for the mod to create these textures.
"events": {
"init": function(event) {
var $this = event.target;
$this.texture = game.colorizeTexture("ingot.png", $this.metal.color);
}
}
});

// Tool remains a component, but material is provided by item.
var Tool = game.modManager.registerComponent({
"id": "tool",
"properties" : {
    "durability": game.primitives.INTEGER,
    "bonus_target": game.modManager.Function,
    "bonus": "$material.hardness"
}
"events": {
"init": function(event) {
var $this= event.target; // Event target is the container, not the component.
$this.durability = Math.pow($this.item.primary_material.durability, 3);
},
"use": function(event) {
var $this = event.equipped;
if ($this.tool.bonus_target(event.target)) {
event.action.time /= $this.bonus;
}
},
"harvest": function(event) {
var $this = event.equipped;
$this.tool.durability--;
if ($this.durability == 0) {
$this.dispatchEvent("break");
}
},
"break": function(event) {
event.target.destroy();
},
}
});


var Axe = game.modManager.registerPrefab({
    "id" : "axe",
    "components" : {
        "item" : {
            "name" : "${material.name} Axe",
            "renderWithIcon": true,
            "stack_size": 1    
        },
        "craftable": {
            "recipe": "$material under $material over $material over blah",
        },
        "tool": {
                "bonus_target": function(obj) {
                 return obj.hasComponent(game.modManager.registerComponent("vanilla.timber"))
        }
    },
"events": {
"init": function(event) {
var $this = event.target;
$this.texture = game.colorizeTexture("axe.png", $this.material.color);
}
}
});

CopperIngot = game.modManager.registerPrefab({
    "id" = "CopperIngot",
    // Prefabs support parentage in the engine, in which case the components and their settings are applied as a delta to 
    // the parent prefab
    "parent" = "MetalIngot",
    "components" = {
        "item" : {
            "primary_material" : game.modManager.getPrefab("Copper"); // Although I guess we don't even need to look it up
        }
    }
});
IronIngot = game.modManager.registerPrefab({
    "id" = "IronIngot",
    "parent" = "MetalIngot",
    "components" = {
        "item" : {
            "primary_material" : game.modManager.getPrefab("Iron");
        }
    }
});
CopperAxe = game.modManager.registerPrefab({
    "id" = "CopperAxe",
    "parent" = "Axe",
    "components" = {
        "item" : {
            "primary_material" : game.modManager.getPrefab("Copper");
        }
    }
});
IronAxe = game.modManager.registerPrefab({
    "id" = "IronAxe",
    "parent" = "Axe",
    "components" = {
        "item" : {
            "primary_material" : game.modManager.getPrefab("Iron");
        }
    }
});