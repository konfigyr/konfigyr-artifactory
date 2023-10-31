package com.konfigyr.artifactory;

/**
 * Enumeration that defines the actual data type of the value, that can be assigned to a
 * single {@link PropertyDescriptor}.
 *
 * @author : Vladimir Spasic
 * @since : 30.10.22, Mon
 **/
public enum DataType {

	/**
	 * Value type that describes that the given {@link PropertyDescriptor} can only have a
	 * single primitive value assigned to it.
	 */
	ATOMIC,

	/**
	 * Value type that describes that the given {@link PropertyDescriptor} can have
	 * multiple primitive values assigned to it.
	 * <p>
	 * In data science the <code>collection</code> data type is considered as composite,
	 * or compound, data type but in the Konfigyr Artifactory world we need to distinct
	 * these two types. Property values with this type can be used to construct arrays,
	 * lists or sets, whereas property with the type of {@link #COMPOSITE} can be used to
	 * construct maps or custom object types.
	 */
	COLLECTION,

	/**
	 * Value type that describes that the given {@link PropertyDescriptor} is the root
	 * property which can be used to construct.maps or custom object types.
	 */
	COMPOSITE

}
