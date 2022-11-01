package rs.lunarshop.items.relics.legacy;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Dualcast;
import com.megacrit.cardcrawl.cards.blue.Zap;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.green.Neutralize;
import com.megacrit.cardcrawl.cards.green.Survivor;
import com.megacrit.cardcrawl.cards.purple.Eruption;
import com.megacrit.cardcrawl.cards.purple.Vigilance;
import com.megacrit.cardcrawl.cards.red.Bash;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.cards.lunar.scepters.*;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.config.RelicConfigBuilder;
import rs.lunarshop.items.abstracts.LegacyRelic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

public class AncientScepter extends LegacyRelic {
    private static final Map<String, String> replacerMap = new HashMap<>();
    
    public AncientScepter() {
        super(87);
        presetInfo(s -> {
            if (!replacerMap.isEmpty()) {
                StringBuilder tip = new StringBuilder();
                for (String old : replacerMap.keySet()) {
                    tip.append(createInfo(s[0], old, replacerMap.get(old)));
                    tip.append(" NL ");
                }
                int lastDrop = tip.lastIndexOf("N");
                tip.delete(lastDrop, tip.length());
                s[0] = tip.toString();
            } else {
                s[0] = null;
            }
        });
    }
    
    @Override
    public void onEquip() {
        if (!cpr().masterDeck.isEmpty()) {
            Map<Integer, AbstractCard> replaceMap = new HashMap<>();
            cpr().masterDeck.group.stream().filter(c -> c.getClass().isAnnotationPresent(BeReplaced.class))
                    .forEach(c -> {
                        int index = cpr().masterDeck.group.indexOf(c);
                        BeReplaced beReplaced = c.getClass().getAnnotation(BeReplaced.class);
                        Class<? extends AbstractCard> replacer = beReplaced.replacer();
                        if (replacer != Madness.class) {
                            try {
                                AbstractCard card = replacer.newInstance();
                                replaceMap.put(index, card);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            if (!replaceMap.isEmpty()) {
                for (Integer index : replaceMap.keySet()) {
                    replacerMap.put(cpr().masterDeck.group.get(index).name, replaceMap.get(index).name);
                    cpr().masterDeck.group.set(index, replaceMap.get(index));
                }
            }
        }
    }
    
    @Override
    protected void saveThings(RelicConfigBuilder builder) {
        if (!replacerMap.isEmpty()) {
            int size = replacerMap.size();
            builder.map("ScepterMapSize", String.valueOf(size));
            int index = size;
            for (Map.Entry<String, String> entry : replacerMap.entrySet()) {
                builder.map("ScepterMap_" + index, entry.getKey() + "[,]" + entry.getValue());
                index--;
            }
        }
    }
    
    @Override
    protected void loadThings(LunarConfig config) {
        if (config.hasMapKey("ScepterMapSize")) {
            replacerMap.clear();
            int size = Integer.parseInt(config.getMapValue("ScepterMapSize"));
            for (int i = size; i > 0; i--) {
                if (config.hasMapKey("ScepterMap_" + i)) {
                    String[] kv = config.getMapValue("ScepterMap_" + i).split("\\[,]");
                    replacerMap.put(kv[0], kv[1]);
                }
            }
        }
    }
    
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BeReplaced {
        Class<? extends AbstractCard> replacer() default Madness.class;
    }
    
    @SpirePatch(clz = CardCrawlGame.class, method = "render")
    public static class VanillaStartingCardPatch {
        @SpireRawPatch
        public static void Raw(CtBehavior ctBehavior) throws Exception {
            ClassPool pool = ctBehavior.getDeclaringClass().getClassPool();
            makeAnnotation(pool.get(Bash.class.getName()), ASUBash.class.getName());
            makeAnnotation(pool.get(Survivor.class.getName()), ASUSurvivor.class.getName());
            makeAnnotation(pool.get(Neutralize.class.getName()), ASUNeutralize.class.getName());
            makeAnnotation(pool.get(Zap.class.getName()), ASUZap.class.getName());
            makeAnnotation(pool.get(Dualcast.class.getName()), ASUDualcast.class.getName());
            makeAnnotation(pool.get(Eruption.class.getName()), ASUEruption.class.getName());
            makeAnnotation(pool.get(Vigilance.class.getName()), ASUVigilance.class.getName());
        }
        
        private static void makeAnnotation(@NotNull CtClass src, String replacer) {
            ConstPool constPool = src.getClassFile().getConstPool();
            AnnotationsAttribute annoAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation brAnno = new Annotation(BeReplaced.class.getName(), constPool);
            brAnno.addMemberValue("replacer", new ClassMemberValue(replacer, constPool));
            annoAttr.addAnnotation(brAnno);
            src.getClassFile().addAttribute(annoAttr);
        }
    }
}