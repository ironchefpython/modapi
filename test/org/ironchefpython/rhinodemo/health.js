var fallingDamageSpeedThreshold = 20;
var excessSpeedDamageMultiplier = 10;

var FULL_HEALTH_EVENT = manager.getEvent("full_health");
var NO_HEALTH_EVENT = manager.getEvent("no_health");

var applyDamage = function(entity, amount, instigator) {
    if (this.currentHealth <= 0) {
        return;
    }
    this.timeSinceLastDamage = 0;
    this.currentHealth -= amount;
    if (this.currentHealth <= 0) {
        entity.send(NO_HEALTH_EVENT, instigator);
    }
}

Health = manager.registerPrototype({
    "id": "Health",
    "properties": {
        "maxHealth": manager.numberType,
        "regenRate": manager.numberType,
        "waitBeforeRegen": manager.numberType,
        "currentHealth": manager.numberType,
        "timeSinceLastDamage": manager.numberType,
        "partialRegen": manager.numberType,
        "ratio": manager.calculatedType(manager.numberType, function() {
        	return this.currentHealth / this.maxHealth;
        })
    },
    "constructor": manager.makeConstructor(["maxHealth", "regenRate", "waitBeforeRegen"], function() {
        this.timeSinceLastDamage = 0;
        this.partialRegen = 0;
        this.currentHealth = this.maxHealth;
    }),
    "handlers": {
        "damage": function(event) {
            applyDamage.call(this, event.target, event.amount, event.instigator);
        },
        "land": function(event) {
            if (event.velocity.y < 0 && -event.velocity.y > thisfallingDamageSpeedThreshold) {
                var damage = Math.int((-event.velocity.y - fallingDamageSpeedThreshold) * excessSpeedDamageMultiplier);
                if (damage > 0) {
                    applyDamage.call(this, entity.target, damage, null);
                }
            }
        },
    	"healFull": function(event) {
   		 	this.currentHealth = this.maxHealth;
    	}
    },
    "update": function(delta, entity) {
        if (health.currentHealth <= 0 || health.currentHealth == health.maxHealth || health.regenRate == 0) {
            return;
        }
        this.timeSinceLastDamage += delta;
        if (this.timeSinceLastDamage >= this.waitBeforeRegen) {
            this.partialRegen += delta * this.regenRate;
            if (this.partialRegen >= 1) {
                this.currentHealth = Math.round(Math.min(this.maxHealth, this.currentHealth + this.partialRegen));
                this.partialRegen %= 1;
                if (this.currentHealth == this.maxHealth) {
                    entity.send(FULL_HEALTH_EVENT);
                }
            }
        }
    }
});

