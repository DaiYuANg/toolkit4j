/* (C)2023*/
package org.nectar.nova.cli.config;

import java.io.File;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class SiteConfig {

	private File source;
}
