package me.gepronix.decaliumcustomitems;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public final class ParticleTrail {
    private final Location from;
    private final Vector vector;
    private final double distance;
    private final ParticleBuilder builder;

    public ParticleTrail(Location from, Vector vector, double distance, ParticleBuilder builder) {

        this.from = from;
        this.vector = vector;
        this.distance = distance;
        this.builder = builder;
    }

    public ParticleTrail(Location from, Vector vector, double distance) {
        this(from, vector, distance, new ParticleBuilder(Particle.REDSTONE).color(Color.BLUE).allPlayers());
    }

    public void draw() {
        Location point = from.clone();
        Vector normalized = vector.clone().normalize();
        int count = (int) distance;
        for(int i = 0; i < count; i++) {
            point.add(normalized);
            builder.location(point).spawn();
        }
    }
}
