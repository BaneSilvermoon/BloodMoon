package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class MoreSpawningListener extends BaseListener<BloodMoon> {
	
	private int multiplier;
	
	public MoreSpawningListener(BloodMoon plugin){
		super(plugin);
		
		this.multiplier = plugin.config.getInt(Config.FEATURE_MORE_SPAWNING_MULTIPLIER);
		
		if (this.multiplier == 0 || this.multiplier > 100){
			this.multiplier = 1;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() != SpawnReason.NATURAL) return;
		
		EntityType type = event.getEntityType();
		Location location = event.getLocation();
		World world = location.getWorld();
		
		List<EntityType> types = Arrays.asList(EntityType.CREEPER, EntityType.ENDERMAN, EntityType.SKELETON, EntityType.ZOMBIE, EntityType.SPIDER);
		
		if (types.contains(type) && plugin.isActive(world)){
			for (int i = 0; i < this.multiplier; ++i){
				world.spawnCreature(location, type);
			}
		}
	}
	
}
