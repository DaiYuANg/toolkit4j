/* (C)2023*/
package org.nectar.nova.core.structure;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.nectar.nova.core.base.DocumentType;

@Data
@Builder
@ToString
@Accessors(chain = true)
public class DocumentSource {

	private String source;

	private DocumentType documentType;
}
