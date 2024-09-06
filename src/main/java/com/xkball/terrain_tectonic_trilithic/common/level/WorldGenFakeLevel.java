package com.xkball.terrain_tectonic_trilithic.common.level;

import com.mojang.datafixers.util.Pair;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.FeatureReplacementData;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.LazyPlaceBlockWorldGenLevel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.LevelTickAccess;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

//FIXME 可以拿到chunkAccess再setBlock
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WorldGenFakeLevel implements WorldGenLevel, LazyPlaceBlockWorldGenLevel {
    
    private final WorldGenLevel inner;
    private final FeatureReplacementData featureReplacementData;
    private boolean finished = false;
    private final HashMap<BlockPos, Pair<BlockState,Pair<Integer,Integer>>> setBlockMap = new HashMap<>();
    
    public WorldGenFakeLevel(WorldGenLevel inner, FeatureReplacementData featureReplacementData) {
        this.inner = inner;
        this.featureReplacementData = featureReplacementData;
        if(inner instanceof WorldGenFakeLevel){
            throw new IllegalArgumentException("Can not use a WorldGenFakeLevel as a inner world of WorldGenFakeLevel.");
        }
    }
    
    @Override
    public void doAllSetBlock(RandomSource randomSource) {
        if(finished) throw new IllegalStateException("world gen should finished.");
        finished = true;
        for(var entry : setBlockMap.entrySet()) {
            var pos = entry.getKey();
            var state = entry.getValue().getFirst();
            var flag = entry.getValue().getSecond().getFirst();
            var recursionLeft = entry.getValue().getSecond().getSecond();
            assert inner.ensureCanWrite(pos);
            
            inner.setBlock(pos,featureReplacementData.getReplacementBlockState(state,randomSource), flag, recursionLeft);
        }
    }
    
    @Override
    public long getSeed() {
        return inner.getSeed();
    }
    
    @Override
    public ServerLevel getLevel() {
        return inner.getLevel();
    }
    
    @Override
    public long nextSubTickCount() {
        return inner.nextSubTickCount();
    }
    
    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return inner.getBlockTicks();
    }
    
    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return inner.getFluidTicks();
    }
    
    @Override
    public LevelData getLevelData() {
        return inner.getLevelData();
    }
    
    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
        return inner.getCurrentDifficultyAt(pos);
    }
    
    @Nullable
    @Override
    public MinecraftServer getServer() {
        return inner.getServer();
    }
    
    @Override
    public ChunkSource getChunkSource() {
        return inner.getChunkSource();
    }
    
    @Override
    public RandomSource getRandom() {
        return inner.getRandom();
    }
    
    @Override
    public void playSound(@Nullable Player player, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        inner.playSound(player,pos,sound,source,volume,pitch);
    }
    
    @Override
    public void addParticle(ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        inner.addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }
    
    @Override
    public void levelEvent(@Nullable Player player, int type, BlockPos pos, int data) {
        inner.levelEvent(player, type, pos, data);
    }
    
    @Override
    public void gameEvent(Holder<GameEvent> gameEvent, Vec3 pos, GameEvent.Context context) {
        inner.gameEvent(gameEvent, pos, context);
    }
    
    @Override
    public float getShade(Direction direction, boolean shade) {
        return inner.getShade(direction, shade);
    }
    
    @Override
    public LevelLightEngine getLightEngine() {
        return inner.getLightEngine();
    }
    
    @Override
    public WorldBorder getWorldBorder() {
        return inner.getWorldBorder();
    }
    
    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return inner.getBlockEntity(pos);
    }
    
    @Override
    public BlockState getBlockState(BlockPos pos) {
        if (!finished && setBlockMap.containsKey(pos)) {
            return setBlockMap.get(pos).getFirst();
        }
        return inner.getBlockState(pos);
    }
    
    @Override
    public FluidState getFluidState(BlockPos pos) {
        return inner.getFluidState(pos);
    }
    
    @Override
    public List<Entity> getEntities(@Nullable Entity entity, AABB area, Predicate<? super Entity> predicate) {
        return inner.getEntities(entity,area,predicate);
    }
    
    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entityTypeTest, AABB bounds, Predicate<? super T> predicate) {
        return inner.getEntities(entityTypeTest,bounds,predicate);
    }
    
    @Override
    public List<? extends Player> players() {
        return inner.players();
    }
    
    @Nullable
    @Override
    //todo
    public ChunkAccess getChunk(int x, int z, ChunkStatus chunkStatus, boolean requireChunk) {
        return inner.getChunk(x, z, chunkStatus, requireChunk);
    }
    
    @Override
    public int getHeight(Heightmap.Types heightmapType, int x, int z) {
        return inner.getHeight(heightmapType, x, z);
    }
    
    @Override
    public int getSkyDarken() {
        return inner.getSkyDarken();
    }
    
    @Override
    public BiomeManager getBiomeManager() {
        return inner.getBiomeManager();
    }
    
    @Override
    public Holder<Biome> getUncachedNoiseBiome(int x, int y, int z) {
        return inner.getUncachedNoiseBiome(x, y, z);
    }
    
    @Override
    public boolean isClientSide() {
        return inner.isClientSide();
    }
    
    @Override
    @Deprecated
    public int getSeaLevel() {
        return inner.getSeaLevel();
    }
    
    @Override
    public DimensionType dimensionType() {
        return inner.dimensionType();
    }
    
    @Override
    public RegistryAccess registryAccess() {
        return inner.registryAccess();
    }
    
    @Override
    public FeatureFlagSet enabledFeatures() {
        return inner.enabledFeatures();
    }
    
    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> state) {
        if (!finished && setBlockMap.containsKey(pos)) {
            return state.test(setBlockMap.get(pos).getFirst());
        }
        return inner.isStateAtPosition(pos, state);
    }
    
    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
        return inner.isFluidAtPosition(pos, predicate);
    }
    
    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        if (!inner.ensureCanWrite(pos)) return false;
        if(!finished){
            setBlockMap.put(pos,new Pair<>(state,new Pair<>(flags,recursionLeft)));
            return true;
        }
        return inner.setBlock(pos, state, flags, recursionLeft);
    }
    
    @Override
    public boolean removeBlock(BlockPos pos, boolean isMoving) {
        return inner.removeBlock(pos, isMoving);
    }
    
    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, @Nullable Entity entity, int recursionLeft) {
        return inner.destroyBlock(pos, dropBlock, entity, recursionLeft);
    }
}
