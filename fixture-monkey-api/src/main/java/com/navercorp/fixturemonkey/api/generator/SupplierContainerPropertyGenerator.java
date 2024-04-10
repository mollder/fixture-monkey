/*
 * Fixture Monkey
 *
 * Copyright (c) 2021-present NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.fixturemonkey.api.generator;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.apiguardian.api.API;

import com.navercorp.fixturemonkey.api.property.Property;
import com.navercorp.fixturemonkey.api.property.SingleElementProperty;
import com.navercorp.fixturemonkey.api.property.SupplierProperty;
import com.navercorp.fixturemonkey.api.type.Types;

@API(since = "1.0.16", status = API.Status.EXPERIMENTAL)
public final class SupplierContainerPropertyGenerator implements ContainerPropertyGenerator {
	public static final SupplierContainerPropertyGenerator INSTANCE = new SupplierContainerPropertyGenerator();
	private static final ArbitraryContainerInfo CONTAINER_INFO = new ArbitraryContainerInfo(1, 1);

	@Override
	public ContainerProperty generate(ContainerPropertyGeneratorContext context) {
		Property property = context.getProperty();

		AnnotatedType childAnnotatedType = getChildAnnotatedType(property);
		Type childType = childAnnotatedType.getType();

		Property childProperty = new SupplierProperty(childType, childAnnotatedType);
		SingleElementProperty singleElementProperty = new SingleElementProperty(childProperty, childAnnotatedType);

		return new ContainerProperty(
			Collections.singletonList(singleElementProperty),
			CONTAINER_INFO
		);
	}

	private AnnotatedType getChildAnnotatedType(Property supplierProperty) {
		Class<?> type = Types.getActualType(supplierProperty.getType());
		if (type != Supplier.class) {
			throw new IllegalArgumentException(
				"type is not Supplier type. propertyType: " + type
			);
		}

		List<AnnotatedType> genericsTypes = Types.getGenericsTypes(supplierProperty.getAnnotatedType());
		if (genericsTypes.size() != 1) {
			throw new IllegalArgumentException(
				"Supplier genericTypes must be have 1 generics type for value. "
					+ "propertyType: " + supplierProperty.getType()
					+ ", genericsTypes: " + genericsTypes
			);
		}

		return genericsTypes.get(0);
	}
}
