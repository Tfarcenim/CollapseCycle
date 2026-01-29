package tfar.collapsecycle;

import net.minecraftforge.common.ForgeConfigSpec;

public class CollapseCycleConfig {

    public static class Server {
        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.LongValue TIME_LIMIT;
        public static final ForgeConfigSpec.LongValue TIME_TO_0_0;

        public static final ForgeConfigSpec.LongValue CURRENT_SEED;
        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("general");
            TIME_LIMIT =builder.defineInRange("time_limit",20 * 60 * 20/10,5,Long.MAX_VALUE);
            TIME_TO_0_0 =builder.defineInRange("time_to_0_0",5 * 60 * 20,5,Long.MAX_VALUE);
            CURRENT_SEED = builder.defineInRange("current_seed",-69, Long.MIN_VALUE,Long.MAX_VALUE);
            builder.pop();

            SPEC = builder.build();
        }
    }
}
