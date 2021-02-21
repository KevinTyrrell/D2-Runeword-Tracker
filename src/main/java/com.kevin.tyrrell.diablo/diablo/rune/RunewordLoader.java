package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import com.kevin.tyrrell.diablo.util.Queryable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

/**
 * Defines an area where Runewords are loaded, stored, and sorted.
 *
 * @since 3.0
 */
public class RunewordLoader implements Queryable<Runeword>
{
    private final Map<String, Runeword> stringMap;

    /* Path to Runewords JSON file. */
    private static final String JSON_PATH = "src/main/resources/json/Runewords.json";

    public RunewordLoader()
    {
        stringMap = Queryable.createStringMap(
                loadRunewordJSON(), rw ->
                {
                    final String rwlc = rw.getName().toLowerCase();
                    /* Remove symbols and spaces. Replace spaces with underscores. */
                    return rwlc.replace("'", "").replace(" ", "_");
                });
    }

    /* Loads all Runewords from the JSON file. */
    private Stream<Runeword> loadRunewordJSON()
    {
        final JSONParser parser = new JSONParser();
        try (final FileReader reader = new FileReader(JSON_PATH))
        {
            final JSONObject obj = (JSONObject)parser.parse(reader);
            final JSONArray jsonRWs = (JSONArray)obj.get("runewords");
            return IntStream.range(0, jsonRWs.size())
                    .mapToObj(i -> loadRuneword((JSONObject)jsonRWs.get(i)));
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
            throw new MissingResourceException(
                    JSON_PATH, RunewordLoader.class.getSimpleName(), "Runeword JSON file is missing.");
        }
        catch (final IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /* Loads a Runeword from its JSON string. */
    @SuppressWarnings("unchecked")
    private Runeword loadRuneword(final JSONObject jsonRW)
    {
        assert jsonRW != null;
        final String name = (String)jsonRW.get("name");
        final int level = toIntExact(((Long)jsonRW.get("level")));
        final JSONArray jsonItemTypeArray = (JSONArray)jsonRW.get("bases");
        final Stream<ItemType> types = ((Stream<Object>)jsonItemTypeArray.stream())
                .map(obj -> ItemType.extension.fromString((String)obj));
        final JSONArray jsonRuneArray = (JSONArray)jsonRW.get("runes");
        final Stream<Rune> runes = ((Stream<Object>)jsonRuneArray.stream())
                .map(obj -> Rune.extension.fromOrdinal(toIntExact((Long)obj)));
        final String description = (String)jsonRW.get("description");
        System.out.println("Creating: " + name);
        return new Runeword(name, level, description, types, runes);
    }

    /**
     * Read-only map which associates string representations
     * of objects to their respective constant object values.
     * <p>
     * By default, map keys are set to Object#toString(). This behavior
     * can be changed by overloading StringToValue#createStringMap().
     * <p>
     * This method should be implemented such that the returned map is
     * an instance variable of the class which is implementing this method.
     *
     * @return Read-only map associating string representations to object values.
     */
    @Override public Map<String, Runeword> stringMap()
    {
        return stringMap;
    }
}
