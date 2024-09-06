package com.xkball.terrain_tectonic_trilithic.test;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.xkball.terrain_tectonic_trilithic.api.worldgen.FeatureReplacementData;
import com.xkball.terrain_tectonic_trilithic.common.datapack.TTDataPacks;
import com.xkball.terrain_tectonic_trilithic.utils.predicate.PredicateWithCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;

public class PredicateTest {
    
    public static void main(String[] args) {
        String testJson = """
{
    "type":"binary_op",
    "first": {
         "type":"list",
         "value":[{"type":"single_equal","value":1},{"type":"single_equal","value":2}],
         "list_op":"none_match"
    },
    "second":{
        "type":"single_equal",
        "value":2
    },
    "boolean_op":"or"
}
                """;
        var json = JsonParser.parseString(testJson);
        var codec = PredicateWithCodec.codec(Codec.INT,null);
        var result = codec.parse(JsonOps.INSTANCE, json);
        result.ifSuccess( p -> System.out.println(p.accept(2)));
    }
    
    public static void runTestAfterServerInit(MinecraftServer server){
        var list = server.registryAccess().registry(TTDataPacks.FEATURE_REPLACEMENT).orElseThrow().stream().toList();
        System.out.println(list);
        var str = """
{
  "featureName": "blue_ice",
  "replacementData": [
    {
      "originBlockWeight": 50,
      "predicate": {
        "type": "predicate",
        "value": {
          "blocks": "minecraft:blue_ice"
        }
      },
      "stateAndWeights": [
        {
          "block_state": {
            "Name": "minecraft:oak_log",
            "Properties": {
              "axis": "y"
            }
          },
          "weight": 50
        }
      ]
    }
  ]
}
                """;
        
        var json = JsonParser.parseString(str);
        var d1 = FeatureReplacementData.CODEC.decode(RegistryOps.create(JsonOps.INSTANCE,server.registryAccess()),json).getOrThrow().getFirst();
        var d2 = FeatureReplacementData.CODEC.decode(RegistryOps.create(JsonOps.INSTANCE,server.registryAccess()),json).getOrThrow().getFirst();
        System.out.println(d1.replacementData.getFirst().statesAndWeight().getFirst().getFirst().equals(d2.replacementData.getFirst().statesAndWeight().getFirst().getFirst()));
//        var blockPredicate = BlockPredicate.Builder.block()
//                .of(BlockTags.LOGS)
//
//                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.AXIS, Direction.Axis.X))
//                .build();
//        var jsonData = BlockPredicate.CODEC.encodeStart(RegistryOps.create(JsonOps.INSTANCE,server.registryAccess()),blockPredicate);
//        System.out.println(jsonData.getOrThrow());
    }
    
}
