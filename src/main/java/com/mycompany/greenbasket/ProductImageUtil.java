package com.mycompany.greenbasket;

import java.util.Locale;

/**
 * Resolves reliable local catalog images. Seeded products use local JPEGs,
 * while future farmer products may still use their own URL. If that URL is
 * missing or is one of the old LoremFlickr demo URLs, a local image is used.
 */
public final class ProductImageUtil {

    private static final String ROOT = "assets/images/products/";

    private ProductImageUtil() {}

    public static String localPathFor(String productName, String categoryName) {
        String n = normalize(productName);

        if (has(n, "tomato")) return ROOT + "tomatoes.jpg";
        if (has(n, "potato")) return ROOT + "potatoes.jpg";
        if (has(n, "onion")) return ROOT + "onions.jpg";
        if (has(n, "spinach", "palak")) return ROOT + "spinach.jpg";
        if (has(n, "beetroot", "beet root")) return ROOT + "beetroot.jpg";

        if (has(n, "mango", "alphonso")) return ROOT + "mangoes.jpg";
        if (has(n, "banana")) return ROOT + "bananas.jpg";
        if (has(n, "guava")) return ROOT + "guava.jpg";
        if (has(n, "pomegranate", "anar")) return ROOT + "pomegranate.jpg";
        if (has(n, "orange", "santra")) return ROOT + "oranges.jpg";

        if (has(n, "brown rice")) return ROOT + "brown-rice.jpg";
        if (has(n, "basmati")) return ROOT + "basmati-rice.jpg";
        if (has(n, "jowar", "sorghum")) return ROOT + "jowar.jpg";
        if (has(n, "bajra", "pearl millet")) return ROOT + "bajra.jpg";

        if (has(n, "toor", "tur dal", "pigeon pea")) return ROOT + "toor-dal.jpg";
        if (has(n, "moong")) return ROOT + "moong-dal.jpg";
        if (has(n, "chana dal", "bengal gram")) return ROOT + "chana-dal.jpg";
        if (has(n, "rajma", "kidney bean")) return ROOT + "rajma.jpg";

        if (has(n, "turmeric", "haldi")) return ROOT + "turmeric.jpg";
        if (has(n, "red chilli", "red chili", "lal mirch")) return ROOT + "red-chilli.jpg";
        if (has(n, "coriander powder", "dhania powder")) return ROOT + "coriander-powder.jpg";
        if (has(n, "coriander", "cilantro", "dhania")) return ROOT + "coriander.jpg";

        if (has(n, "milk")) return ROOT + "milk.jpg";
        if (has(n, "curd", "dahi", "yogurt", "yoghurt")) return ROOT + "curd.jpg";
        if (has(n, "paneer")) return ROOT + "paneer.jpg";
        if (has(n, "egg")) return ROOT + "eggs.jpg";

        if (has(n, "almond", "badam")) return ROOT + "almonds.jpg";
        if (has(n, "cashew", "kaju")) return ROOT + "cashews.jpg";
        if (has(n, "flax", "alsi")) return ROOT + "flax-seeds.jpg";
        if (has(n, "sesame", "til")) return ROOT + "sesame.jpg";

        if (has(n, "groundnut oil", "peanut oil")) return ROOT + "groundnut-oil.jpg";
        if (has(n, "mustard oil")) return ROOT + "mustard-oil.jpg";
        if (has(n, "coconut oil")) return ROOT + "coconut-oil.jpg";
        if (has(n, "ghee")) return ROOT + "ghee.jpg";

        if (has(n, "honey")) return ROOT + "honey.jpg";
        if (has(n, "jaggery", "gur")) return ROOT + "jaggery.jpg";
        if (has(n, "date syrup", "khajur syrup")) return ROOT + "date-syrup.jpg";
        if (has(n, "coconut sugar")) return ROOT + "coconut-sugar.jpg";

        String c = normalize(categoryName);
        if (c.equals("vegetables")) return ROOT + "generic-vegetables.jpg";
        if (c.equals("fruits")) return ROOT + "generic-fruits.jpg";
        if (c.contains("grains")) return ROOT + "basmati-rice.jpg";
        if (c.contains("pulses")) return ROOT + "toor-dal.jpg";
        if (c.contains("spices")) return ROOT + "turmeric.jpg";
        if (c.contains("dairy")) return ROOT + "milk.jpg";
        if (c.contains("nuts")) return ROOT + "almonds.jpg";
        if (c.contains("oils")) return ROOT + "groundnut-oil.jpg";
        if (c.contains("honey")) return ROOT + "honey.jpg";

        return ROOT + "photo-placeholder.svg";
    }

    public static String resolveForWeb(String contextPath, String configuredImageUrl,
            String productName, String categoryName) {
        String fallback = localPathFor(productName, categoryName);
        String raw = configuredImageUrl == null ? "" : configuredImageUrl.trim();

        if (raw.isEmpty() || raw.contains("loremflickr.com")) {
            return withContext(contextPath, fallback);
        }
        if (raw.startsWith("assets/") || raw.startsWith("images/")) {
            return withContext(contextPath, raw);
        }
        return raw;
    }

    public static String localFallbackForWeb(String contextPath, String productName, String categoryName) {
        return withContext(contextPath, localPathFor(productName, categoryName));
    }

    private static String withContext(String contextPath, String path) {
        String ctx = contextPath == null ? "" : contextPath;
        if (path.startsWith("/")) return ctx + path;
        return ctx + "/" + path;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT).trim();
    }

    private static boolean has(String value, String... needles) {
        for (String needle : needles) {
            if (value.contains(needle)) return true;
        }
        return false;
    }
}
