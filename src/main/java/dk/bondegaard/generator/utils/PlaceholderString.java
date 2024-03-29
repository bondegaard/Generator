/*
 * Copyright (c) 2023 bondegaard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dk.bondegaard.generator.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class PlaceholderString implements Cloneable {

    private String string;

    private List<String> placeholderNames = Lists.newArrayList();

    private List<Object> placeholderValues = Lists.newArrayList();

    public PlaceholderString(List<String> placeholderNames) {
        this.placeholderNames(placeholderNames);
    }

    public PlaceholderString(String string, String... placeholderNames) {
        this(string, Arrays.asList(placeholderNames));
    }

    public PlaceholderString(String string, List<String> placeholderNames) {
        this(placeholderNames);
        this.string = string;
    }

    public PlaceholderString string(String string) {
        this.string = string;
        return this;
    }

    public PlaceholderString placeholders(final @NotNull Map<String, Object> placeholders) {
        if (placeholders.size() < 1)
            return this;

        int i = 0;

        for (Map.Entry<String, Object> placeholder : placeholders.entrySet()) {
            if (this.placeholderNames.size() <= placeholders.size())
                this.placeholderNames.add(placeholder.getKey());
            else
                this.placeholderNames.set(i, placeholder.getKey());

            if (this.placeholderValues.size() <= placeholders.size())
                this.placeholderValues.add(placeholder.getValue());
            else
                this.placeholderValues.set(i, placeholder.getValue());

            i++;
        }

        return this;
    }

    public PlaceholderString placeholderNames(String... placeholderNames) {
        return this.placeholderNames(Arrays.asList(placeholderNames));
    }

    public PlaceholderString placeholderNames(Collection<String> placeholderNames) {
        return this.placeholderNames(Lists.newArrayList(placeholderNames));
    }

    public PlaceholderString placeholderNames(List<String> placeholderNames) {
        this.placeholderNames = new ArrayList<>(placeholderNames);
        return this;
    }

    public PlaceholderString addPlaceholderNames(String... placeholderNames) {
        this.placeholderNames.addAll(Arrays.asList(placeholderNames));
        return this;
    }

    public PlaceholderString placeholderValues(Object... placeholderValues) {
        return this.placeholderValues(Arrays.asList(placeholderValues));
    }

    public PlaceholderString placeholderValues(Collection<Object> placeholderValues) {
        this.placeholderValues = Lists.newArrayList(placeholderValues);
        return this;
    }

    public PlaceholderString addPlaceholderValues(Object... placeholderValues) {
        this.placeholderValues.addAll(Arrays.asList(placeholderValues));
        return this;
    }

    public String parse() {
        String parsedResult = this.string;

        for (Map.Entry<String, Object> placeholder : this.toPlaceholderValuesByName().entrySet())
            parsedResult = parsedResult.replace(String.valueOf(placeholder.getKey()), String.valueOf(placeholder.getValue()));

        parsedResult = StringUtil.colorize(parsedResult);
        return parsedResult;
    }

    public List<String> parseStrings(final List<String> strings) {
        return strings.stream()
                .map(elm -> this.string(elm).parse())
                .collect(Collectors.toList());
    }

    private Map<String, Object> toPlaceholderValuesByName() {
        Map<String, Object> placeholderValuesByName = Maps.newHashMap();

        for (int i = 0; i < this.placeholderNames.size(); i++) {
            Object value = null;

            if (i < this.placeholderValues.size())
                value = this.placeholderValues.get(i);

            placeholderValuesByName.put(this.placeholderNames.get(i), value);
        }

        return placeholderValuesByName;
    }

    @Override
    public PlaceholderString clone() {
        try {
            return (PlaceholderString) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
