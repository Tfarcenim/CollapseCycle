package tfar.collapsecycle;

import net.minecraftforge.common.ForgeConfigSpec;

public class CollapseCycleConfig {

    public static class Server {
        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.LongValue TIME_LIMIT;
        public static final ForgeConfigSpec.LongValue TIME_TO_0_0;
        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("general");
            TIME_LIMIT =builder.defineInRange("time_limit",20 * 60 * 20/10,5,Long.MAX_VALUE);
            TIME_TO_0_0 =builder.defineInRange("time_to_0_0",20 * 60 * 20/10,5,Long.MAX_VALUE);
            builder.pop();

            SPEC = builder.build();
        }
    }
}
