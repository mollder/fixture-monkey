package com.navercorp.fixturemonkey.api.introspector;

import com.navercorp.fixturemonkey.api.arbitrary.CombinableArbitrary;
import com.navercorp.fixturemonkey.api.generator.ArbitraryGeneratorContext;
import com.navercorp.fixturemonkey.api.generator.ArbitraryProperty;
import com.navercorp.fixturemonkey.api.matcher.AssignableTypeMatcher;
import com.navercorp.fixturemonkey.api.matcher.Matcher;
import com.navercorp.fixturemonkey.api.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static com.navercorp.fixturemonkey.api.matcher.SingleGenericTypeMatcher.SINGLE_GENERIC_TYPE_MATCHER;

public final class SupplierIntrospector implements ArbitraryIntrospector, Matcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierIntrospector.class);
	private static final Matcher MATCHER = new AssignableTypeMatcher(Supplier.class);

	@Override
	public boolean match(Property property) {
		return SINGLE_GENERIC_TYPE_MATCHER.match(property) && MATCHER.match(property);
	}

	@Override
	public ArbitraryIntrospectorResult introspect(ArbitraryGeneratorContext context) {
		ArbitraryProperty property = context.getArbitraryProperty();
		if (!property.isContainer()) {
			return ArbitraryIntrospectorResult.NOT_INTROSPECTED;
		}

		CombinableArbitrary<?> elementArbitrary = context.getElementCombinableArbitraryList()
			.get(0)
			.map(this::toSupplier);

		return new ArbitraryIntrospectorResult(elementArbitrary);
	}

	private <T> Supplier<T> toSupplier(T value) {
		return () -> value;
	}
}
