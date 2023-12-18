/* (C)2023*/
package org.nectar.nova.core.base;

public interface Processor<Input, Output> {

	Output processor(Input input);
}
