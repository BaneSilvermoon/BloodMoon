package uk.co.jacekk.bukkit.bloodmoon.entities;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.UnsafeList;
import org.bukkit.entity.Skeleton;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.events.SkeletonMoveEvent;
import uk.co.jacekk.bukkit.bloodmoon.pathfinders.BloodMoonPathfinderGoalArrowAttack;

import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.World;

public class BloodMoonEntitySkeleton extends net.minecraft.server.EntitySkeleton {
	
	public BloodMoonEntitySkeleton(World world, BloodMoon plugin){
		super(world);
		
		try{
			Field a = this.goalSelector.getClass().getDeclaredField("a");
			a.setAccessible(true);
			
			@SuppressWarnings("unchecked")
			UnsafeList<PathfinderGoal> goals = (UnsafeList<PathfinderGoal>) a.get(this.goalSelector);
			
			for (Object item : goals){
				Field goal = item.getClass().getDeclaredField("a");
				goal.setAccessible(true);
				
				if (goal.get(item) instanceof PathfinderGoalArrowAttack){
					goal.set(item, new BloodMoonPathfinderGoalArrowAttack(this, plugin, this.bb, 60));
				}
			}
			
			a.set(this.goalSelector, goals);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public BloodMoonEntitySkeleton(World world){
		this(world, (BloodMoon) Bukkit.getPluginManager().getPlugin("BloodMoon"));
	}
	
	@Override
	public void F_(){
		Skeleton skeleton = (Skeleton) this.getBukkitEntity();
		
		Location from = new Location(skeleton.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(skeleton.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		SkeletonMoveEvent event = new SkeletonMoveEvent(skeleton, from, to);
		
		this.world.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled() && skeleton.isDead() == false){
			return;
		}
		
		super.F_();
	}

}
