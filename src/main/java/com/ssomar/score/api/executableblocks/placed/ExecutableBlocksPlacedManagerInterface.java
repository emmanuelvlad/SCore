package com.ssomar.score.api.executableblocks.placed;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExecutableBlocksPlacedManagerInterface {

    /**
     * Get an ExecutableBlockPlaced from its location (Block location +0.5 +0.5 +0.5)
     *
     * @param location The location of the potential ExecutableBlockPlaced
     * @return The ExecutableBlockPlaced
     **/
    Optional<ExecutableBlockPlacedInterface> getExecutableBlockPlaced(Location location);

    /**
     * Get an ExecutableBlockPlaced from its location
     *
     * @param block The block of the potential ExecutableBlockPlaced
     * @return The ExecutableBlockPlaced
     **/
    Optional<ExecutableBlockPlacedInterface> getExecutableBlockPlaced(Block block);

    /**
     * Get all ExecutableBlockPlaceds present in a specific chunk
     *
     * @param chunk The chunk to get ExecutableBlockPlaceds from
     * @return The list of ExecutableBlockPlaced
     **/
    List<ExecutableBlockPlacedInterface> getExecutableBlocksPlaced(Chunk chunk);

    /**
     * Get an ExecutableBlockPlaced near a specific location
     *
     * @param location The location of the potential ExecutableBlockPlaced
     * @param distance The maximum distance to search
     * @return The list of ExecutableBlockPlaced
     **/
    List<ExecutableBlockPlacedInterface> getExecutableBlocksPlacedNear(Location location, double distance);

    /**
     * Get all the ExecutableBlocksPlaced
     *
     * @return The map with all the ExecutableBlocksPlaced
     **/
    Map<Location, ExecutableBlockPlacedInterface> getAllExecutableBlocksPlaced();
}
