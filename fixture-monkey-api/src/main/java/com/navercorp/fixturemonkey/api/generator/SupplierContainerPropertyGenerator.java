package com.navercorp.fixturemonkey.api.generator;

import com.navercorp.fixturemonkey.api.property.ElementProperty;
import com.navercorp.fixturemonkey.api.property.Property;
import com.navercorp.fixturemonkey.api.type.Types;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.function.Supplier;

public final class SupplierContainerPropertyGenerator implements ContainerPropertyGenerator {
	public static final SupplierContainerPropertyGenerator INSTANCE = new SupplierContainerPropertyGenerator();
	private static final ArbitraryContainerInfo CONTAINER_INFO = new ArbitraryContainerInfo(1, 1);

	@Override
	public ContainerProperty generate(ContainerPropertyGeneratorContext context) {
		Property property = context.getProperty();

		AnnotatedType valueAnnotatedType = getValueAnnotatedType(property);
		Property valueProperty = new ElementProperty(
			property,
			valueAnnotatedType,
			0,
			0
		);

		return new ContainerProperty(
			Collections.singletonList(valueProperty),
			CONTAINER_INFO
		);
	}

	private AnnotatedType getValueAnnotatedType(Property optionalProperty) {
		Class<?> type = Types.getActualType(optionalProperty.getType());
		if (type != Supplier.class) {
			throw new IllegalArgumentException(
				"type is not Supplier type. propertyType: " + type
			);
		}

		List<AnnotatedType> genericsTypes = Types.getGenericsTypes(optionalProperty.getAnnotatedType());
		if (genericsTypes.size() != 1) {
			throw new IllegalArgumentException(
				"Supplier genericTypes must be have 1 generics type for value. "
					+ "propertyType: " + optionalProperty.getType()
					+ ", genericsTypes: " + genericsTypes
			);
		}

		return genericsTypes.get(0);
	}
}
