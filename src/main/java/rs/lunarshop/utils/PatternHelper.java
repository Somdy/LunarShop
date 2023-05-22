package rs.lunarshop.utils;

import java.util.regex.Pattern;

public class PatternHelper {
    public static final Pattern RELIC_COOLDOWN = Pattern.compile("\\s(#[t])\\([^#\\n]+(?<=\\))(t#)\\s",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    public static final Pattern RELIC_STACK_INFO = Pattern.compile("\\s(#[t])\\[[^#\\n]+(?<=])(#)\\s",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    public static final Pattern POWER_NAME = Pattern.compile("\\[(owner_name)]", Pattern.MULTILINE);
    public static final Pattern POWER_AMT = Pattern.compile("\\[(amt_(\\d))]", Pattern.MULTILINE);
    public static final Pattern POWER_TARGET = Pattern.compile("\\[(crt_(\\d))]", Pattern.MULTILINE);
    public static final Pattern PRVD_CURSE_PREFIX = Pattern.compile("\\$\\d{2}\\s", Pattern.MULTILINE);
}