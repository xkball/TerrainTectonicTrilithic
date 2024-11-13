package com.xkball.terrain_tectonic_trilithic.test;

import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class SkyColorTest {
    
    private static final float[] sunriseCol = new float[4];
    
    public static Vec3 getSkyColor(Vec3 pos, float timeOfDay) {
        Vec3 vec3 = pos.subtract(2.0, 2.0, 2.0).scale(0.25);
        Vec3 vec31 = CubicSampler.gaussianSampleVec3(
                vec3, (p_194161_, p_194162_, p_194163_) -> Vec3.fromRGB24(calculateSkyColor())
        );
        float f1 = Mth.cos(timeOfDay * (float) (Math.PI * 2)) * 2.0F + 0.5F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        float f2 = (float) vec31.x * f1;
        float f3 = (float) vec31.y * f1;
        float f4 = (float) vec31.z * f1;
        
        return new Vec3(f2, f3, f4);
    }
    
    protected static int calculateSkyColor() {
        return Mth.hsvToRgb(0.6055556f, 0.53333336f, 1.0F);
    }
    
    @Nullable
    public static float[] getSunriseColor(float timeOfDay) {
        float f = 0.4F;
        float f1 = Mth.cos(timeOfDay * (float) (Math.PI * 2)) - 0.0F;
        float f2 = -0.0F;
        if (f1 >= -0.4F && f1 <= 0.4F) {
            float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
            float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float) Math.PI)) * 0.99F;
            f4 *= f4;
            sunriseCol[0] = f3 * 0.3F + 0.7F;
            sunriseCol[1] = f3 * f3 * 0.7F + 0.2F;
            sunriseCol[2] = f3 * f3 * 0.0F + 0.2F;
            sunriseCol[3] = f4;
            return sunriseCol;
        } else {
            return null;
        }
    }
    
    public static double blend(double a, double b, double alpha) {
        return a * alpha + b * (1 - alpha);
    }
    
    public static Vec3 skyColor(float timeOfDay) {
        var sky = getSkyColor(new Vec3(0.0F, 100.0F, 0.0F), timeOfDay);
        var sun = getSunriseColor(timeOfDay);
        if (sun != null) {
            var x = blend(sun[0], sky.x, sun[3]);
            var y = blend(sun[1], sky.y, sun[3]);
            var z = blend(sun[2], sky.z, sun[3]);
            return new Vec3(x, y, z);
        }
        return sky;
    }
    
    public static double v(int f){
        return Math.toDegrees(Math.atan((f*0.01-1d/(f*0.00001))/100d));
    }
    
    public static void main(String[] args) {
        //System.out.println(LocalDateTime.now(ZoneOffset.ofHours(8)).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        for (int i = 0; i < 1000; i++) {
            var c = skyColor(0.001f * i);
          //  System.out.println(c.x + ", " + c.y + ", " + c.z);
        }
        var arr = new int[]{600,1200,2500,3700,4000,4200,4500,5000,5500,5700,6200,7500,8800,11000};
        for(var i : arr){
            System.out.println(v(i));
        }
    }
}
