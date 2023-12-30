/* (C)2023*/
package org.nectar.refined.container;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nectar.refined.exception.StringEmptyOrBlank;

/**
 * This class represents a container for holding and manipulating character sequences (e.g., strings).
 * It extends the BasicContainer class and provides additional methods for string operations.
 *
 * @author DaiYuAng
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
public class StringContainer extends BasicContainer<CharSequence, StringContainer> {

	private final StringBuilder reusableStringBuilder = new StringBuilder();

	/**
	 * Constructs a StringContainer with the specified character sequence value.
	 *
	 * @param value The character sequence value.
	 */
	protected StringContainer(CharSequence value) {
		super(value);
	}

	@Override
	public StringContainer filter(Predicate<CharSequence> predicate) {
		return isEmpty() ? this : predicate.test(value) ? this : empty();
	}

	@Override
	public <U extends Container<CharSequence, ?>> StringContainer map(Function<? super CharSequence, U> mapper) {
		return isEmpty() ? empty() : (StringContainer) mapper.apply(value);
	}

	@Override
	public <U> StringContainer flatMap(
			Function<? super CharSequence, ? extends Container<CharSequence, ? extends U>> mapper) {
		if (isEmpty()) {
			return empty();
		} else {
			val r = mapper.apply(value);
			return (StringContainer) r;
		}
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty()
				&& Objects.requireNonNull(value).isEmpty()
				&& value.toString().isBlank();
	}

	@NotNull @Contract(" -> new")
	public static StringContainer empty() {
		return new StringContainer("");
	}

	@Override
	public CharSequence orElseThrow() {
		if (isEmpty()) throw new StringEmptyOrBlank();
		return value;
	}

	@Override
	public CharSequence get() throws StringEmptyOrBlank {
		return orElseGet(() -> {
			throw new StringEmptyOrBlank();
		});
	}

	@Override
	public StringContainer or(Supplier<StringContainer> supplier) {
		return isEmpty() ? supplier.get() : this;
	}

	/**
	 * Concatenates the current container's value with the provided strings.
	 *
	 * @param concat The strings to concatenate.
	 * @return A new StringContainer with the concatenated value.
	 */
	public StringContainer concat(String... concat) {
		StringBuilder result = new StringBuilder(Objects.requireNonNull(value).toString());
		Arrays.stream(concat).forEach(result::append);
		return StringContainer.of(result.toString());
	}

	/**
	 * Reverses the characters of the current container's value.
	 *
	 * @return A new StringContainer with the reversed value.
	 */
	public StringContainer reverse() {
		reusableStringBuilder.setLength(0);
		reusableStringBuilder.append(value);
		return StringContainer.of(reusableStringBuilder.reverse().toString());
	}

	/**
	 * Counts the occurrences of a specific character in the container's value.
	 *
	 * @param charSequence The character to count.
	 * @return The count of occurrences.
	 */
	public long count(char charSequence) {
		return nonNullable().chars().filter(c -> c == charSequence).count();
	}

	/**
	 * Counts the occurrences of a specific character sequence in the container's value.
	 *
	 * @param target The character sequence to count.
	 * @return The count of occurrences.
	 */
	public long count(@NotNull CharSequence target) {
		int count;
		int targetLength = target.length();
		count = (int) IntStream.rangeClosed(0, Objects.requireNonNull(value).length() - targetLength)
				.filter(i -> value.subSequence(i, i + targetLength).equals(target))
				.count();
		return count;
	}

	/**
	 * Returns a new StringContainer with the distinctive characters of the current container's value.
	 *
	 * @return A new StringContainer with distinctive characters.
	 */
	public StringContainer distinct() {
		val distinctive =
				nonNullable().chars().distinct().mapToObj(String::valueOf).collect(Collectors.joining());
		return StringContainer.of(distinctive);
	}

	public List<String> toList() {
		return nonNullable().chars().mapToObj(String::valueOf).toList();
	}

	public StringContainer reduce(BinaryOperator<String> accumulator) {
		return StringContainer.of(nonNullable()
				.chars()
				.mapToObj(String::valueOf)
				.reduce(accumulator)
				.orElse(""));
	}

	public String[] toArray() {
		return nonNullable().chars().mapToObj(String::valueOf).toArray(String[]::new);
	}

	@Contract("_ -> new")
	@NotNull public static StringContainer of(@Nullable CharSequence value) {
		return new StringContainer(value);
	}

	@Override
	public String toString() {
		return nonNullable().toString();
	}
}
