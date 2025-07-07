package com.translated.lara.translator;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextResult {

    public static class Value {

        private final String translation;
        private final List<String> translations;
        private final List<TextBlock> translationBlocks;

        public Value(String translation) {
            this.translation = translation;
            this.translations = null;
            this.translationBlocks = null;
        }

        public Value(String[] translation) {
            this.translation = null;
            this.translations = Collections.unmodifiableList(Arrays.asList(translation));
            this.translationBlocks = null;
        }

        public Value(TextBlock[] translation) {
            this.translation = null;
            this.translations = null;
            this.translationBlocks = Collections.unmodifiableList(Arrays.asList(translation));
        }

        public String getTranslation() {
            if (translation != null)
                return translation;

            if (translations != null && !translations.isEmpty()) {
                if (translations.size() != 1)
                    throw new IllegalStateException("Cannot get translation from multiple elements (" + translations.size() + ")");
                return translations.get(0);
            }

            if (translationBlocks != null && !translationBlocks.isEmpty()) {
                if (translationBlocks.size() != 1)
                    throw new IllegalStateException("Cannot get translation from multiple elements (" + translationBlocks.size() + ")");
                return translationBlocks.get(0).getText();
            }

            return null;
        }

        public List<String> getTranslations() {
            if (translations != null)
                return translations;

            if (translation != null)
                return Collections.singletonList(translation);

            if (translationBlocks != null && !translationBlocks.isEmpty()) {
                List<String> result = new ArrayList<>(translationBlocks.size());
                for (TextBlock block : translationBlocks)
                    result.add(block.getText());

                return result;
            }

            return null;
        }

        public List<TextBlock> getTranslationBlocks() {
            if (translationBlocks != null)
                return translationBlocks;

            if (translation != null)
                return Collections.singletonList(new TextBlock(translation));

            if (translations != null && !translations.isEmpty()) {
                List<TextBlock> result = new ArrayList<>(translations.size());
                for (String translation : translations)
                    result.add(new TextBlock(translation));

                return result;
            }

            return null;
        }

        @Override
        public String toString() {
            if (translation != null) return translation;
            if (translations != null) return translations.toString();
            if (translationBlocks != null) return translationBlocks.toString();
            return null;
        }
    } 

    @JsonAdapter(AdaptedToMatchesValueDeserializer.class)
    public static class AdaptedToMatchesValue {
        private final List<NGMemoryMatch> matches;
        private final List<List<NGMemoryMatch>> matchesList;

        public AdaptedToMatchesValue(List<NGMemoryMatch> matches) {
            this.matches = matches;
            this.matchesList = null;
        }

        public AdaptedToMatchesValue(List<List<NGMemoryMatch>> matchesList, boolean isList) {
            this.matches = null;
            this.matchesList = matchesList;
        }

        public List<NGMemoryMatch> getMatches() {
            return matches;
        }

        public List<List<NGMemoryMatch>> getMatchesList() {
            return matchesList;
        }

        @Override
        public String toString() {
            if (matches != null) {
                return matches.toString();
            } else if (matchesList != null) {
                return matchesList.toString();
            }
            return null;
        }
    }
    public static class AdaptedToMatchesValueDeserializer implements JsonDeserializer<AdaptedToMatchesValue> {
        @Override
        public AdaptedToMatchesValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonArray()) {
                throw new JsonParseException("Expected JSON array for adapted_to_matches");
            }

            JsonArray array = json.getAsJsonArray();
            if (array.size() == 0) {
                return new AdaptedToMatchesValue((List<NGMemoryMatch>) null);
            }

            JsonElement firstElement = array.get(0);
            if (firstElement.isJsonArray()) {
                Type listOfListType = new TypeToken<List<List<NGMemoryMatch>>>(){}.getType();
                List<List<NGMemoryMatch>> matchesList = context.deserialize(json, listOfListType);
                return new AdaptedToMatchesValue(matchesList, true);
            } else {
                Type listType = new TypeToken<List<NGMemoryMatch>>(){}.getType();
                List<NGMemoryMatch> matches = context.deserialize(json, listType);
                return new AdaptedToMatchesValue(matches);
            }
        }
    }

    @JsonAdapter(GlossariesValueMatchesDeserializer.class)
    public static class GlossariesMatchesValue {
        private final List<NGGlossaryMatch> matches;
        private final List<List<NGGlossaryMatch>> matchesList;

        public GlossariesMatchesValue(List<NGGlossaryMatch> matches) {
            this.matches = matches;
            this.matchesList = null;
        }

        public GlossariesMatchesValue(List<List<NGGlossaryMatch>> matchesList, boolean isList) {
            this.matches = null;
            this.matchesList = matchesList;
        }

        public List<NGGlossaryMatch> getMatches() {
            return matches;
        }

        public List<List<NGGlossaryMatch>> getMatchesList() {
            return matchesList;
        }

        @Override
        public String toString() {
            if (matches != null) {
                return matches.toString();
            } else if (matchesList != null) {
                return matchesList.toString();
            }
            return null;
        }
    }
    public static class GlossariesValueMatchesDeserializer implements JsonDeserializer<GlossariesMatchesValue> {
        @Override
        public GlossariesMatchesValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonArray()) {
                throw new JsonParseException("Expected JSON array for glossaries_matches");
            }

            JsonArray array = json.getAsJsonArray();
            if (array.size() == 0) {
                return new GlossariesMatchesValue((List<NGGlossaryMatch>) null);
            }

            JsonElement firstElement = array.get(0);
            if (firstElement.isJsonArray()) {
                Type listOfListType = new TypeToken<List<List<NGGlossaryMatch>>>(){}.getType();
                List<List<NGGlossaryMatch>> matchesList = context.deserialize(json, listOfListType);
                return new GlossariesMatchesValue(matchesList, true);
            } else {
                Type listType = new TypeToken<List<NGGlossaryMatch>>(){}.getType();
                List<NGGlossaryMatch> matches = context.deserialize(json, listType);
                return new GlossariesMatchesValue(matches);
            }
        }
    }

    private final String contentType;
    private final String sourceLanguage;
    private final List<String> adaptedTo;
    private final List<String> glossaries;

    private final Value translation;
    private final AdaptedToMatchesValue adaptedToMatches;
    private final GlossariesMatchesValue glossariesMatches;

    public TextResult(String contentType, String sourceLanguage, String translation, String[] adaptedTo, String[] glossaries) {
        this.contentType = contentType;
        this.sourceLanguage = sourceLanguage;
        this.adaptedTo = adaptedTo == null ? null : Collections.unmodifiableList(Arrays.asList(adaptedTo));
        this.glossaries = glossaries == null ? null : Collections.unmodifiableList(Arrays.asList(glossaries));

        this.translation = new Value(translation);
        this.adaptedToMatches = null;
        this.glossariesMatches = null;
    }

    public TextResult(String contentType, String sourceLanguage, String[] translation, String[] adaptedTo, String[] glossaries) {
        this.contentType = contentType;
        this.sourceLanguage = sourceLanguage;
        this.adaptedTo = adaptedTo == null ? null : Collections.unmodifiableList(Arrays.asList(adaptedTo));
        this.glossaries = glossaries == null ? null : Collections.unmodifiableList(Arrays.asList(glossaries));

        this.translation = new Value(translation);
        this.adaptedToMatches = null;
        this.glossariesMatches = null;
    }

    public TextResult(String contentType, String sourceLanguage, TextBlock[] translation, String[] adaptedTo, String[] glossaries) {
        this.contentType = contentType;
        this.sourceLanguage = sourceLanguage;
        this.adaptedTo = adaptedTo == null ? null : Collections.unmodifiableList(Arrays.asList(adaptedTo));
        this.glossaries = glossaries == null ? null : Collections.unmodifiableList(Arrays.asList(glossaries));

        this.translation = new Value(translation);
        this.adaptedToMatches = null;
        this.glossariesMatches = null;
    }
 

    public String getContentType() {
        return contentType;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public List<String> getAdaptedTo() {
        return adaptedTo;
    }

	public List<String> getGlossaries() {
		return glossaries;
	}
	
    public String getTranslation() {
        return translation.getTranslation();
    }

    public List<String> getTranslations() {
        return translation.getTranslations();
    }

    public List<TextBlock> getTranslationBlocks() {
        return translation.getTranslationBlocks();
    }

    public List<NGMemoryMatch> getAdaptedToMatches() {
        return adaptedToMatches != null ? adaptedToMatches.getMatches() : null;
    }

    public List<List<NGMemoryMatch>> getAdaptedToMatchesList() {
        return adaptedToMatches != null ? adaptedToMatches.getMatchesList() : null;
    }

    public List<NGGlossaryMatch> getGlossariesMatches() {
        return glossariesMatches != null ? glossariesMatches.getMatches() : null;
    }

    public List<List<NGGlossaryMatch>> getGlossariesMatchesList() {
        return glossariesMatches != null ? glossariesMatches.getMatchesList() : null;
    }

    @Override
    public String toString() {
        return this.translation.toString();
    }
}
