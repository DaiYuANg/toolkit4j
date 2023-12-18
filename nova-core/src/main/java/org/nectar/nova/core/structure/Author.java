/* (C)2023*/
package org.nectar.nova.core.structure;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@Builder
public class Author {

	private String authorName;

	private String email;

	private String github;
}
