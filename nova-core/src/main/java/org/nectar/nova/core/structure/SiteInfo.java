/* (C)2023*/
package org.nectar.nova.core.structure;

import java.util.List;
import java.util.Locale;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@Builder
public class SiteInfo {

	private Locale locale;

	private String siteTitle;

	private String subTitle;

	private List<MetaInfo> metaInfos;
}
