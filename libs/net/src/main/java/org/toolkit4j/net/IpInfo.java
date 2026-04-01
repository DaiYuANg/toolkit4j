package org.toolkit4j.net;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record IpInfo(String ip, IpVersion version) {}
